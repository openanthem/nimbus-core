/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.crossdomainresolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.session.TestSessionProvider;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.crossdomainresolver.view.VRSampleCrossDomain.SampleNested;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

/**
 * @author Swetha Vemuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CrossDomainPathResolverTest extends AbstractStateEventHandlerTests {
	
	private final static String ENG_LANG = "English";
	private final static String FRENCH_LANG = "French";
	private final static String SAMPLE_VIEW_ROOT = "sample_crossdomain_pathresolver";
	protected static final String SAMPLE_VIEW_PARAM_ROOT = PLATFORM_ROOT + "/" + SAMPLE_VIEW_ROOT;
	private final static String SAMPLE_VIEW_MULTI_ROOT = "sample_multi_domain";
	protected static final String SAMPLE_VIEW_MULTI_PARAM_ROOT = PLATFORM_ROOT + "/" + SAMPLE_VIEW_MULTI_ROOT;
	
	@Autowired
	TestSessionProvider sessionProvider;
	
	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_crossdomain_pathresolver/_new").getCommand();
		return cmd;
	}
	
	private MockHttpServletRequest createRequest(String domainPath, Action a) {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(domainPath)
				.addAction(a).getMock();
		
		return req;
	}

	@Test
	public void t00_init() {
		assertNotNull(_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language"));
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t01_resolve_label_name() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(ENG_LANG);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_new_resp).getState());
		Param<?> param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(param.findParamByPath("/attr1").getLabel("en-US"));
		assertEquals("This label is shown in {English}", param.findParamByPath("/attr1").getLabel("en-US").getText());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t02_resolve_parampath_rawPayload() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(ENG_LANG);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		Holder<MultiOutput> multi_action_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/attr2_action", Action._get), null);
		assertNotNull(multi_action_get_resp);
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> attr2_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(attr2_param);
		assertNotNull(attr2_param.findParamByPath("/attr3"));
		assertNotNull(attr2_param.findParamByPath("/attr3").getLeafState());		
		assertEquals("English Breakfast", attr2_param.findParamByPath("/attr3").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t03_resolve_parampath_conditional() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		final MultiOutput output = MultiOutput.class.cast(Holder.class.cast(multi_new_resp).getState());
		Param<?> param = (Param<?>) output.getSingleResult();
		param.findParamByPath("/attr1").setState("French");
		param.findParamByPath("/attr2_conditional_action").setState("test");
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> attr2_action_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(attr2_action_param);
		assertEquals("French Pastries", attr2_action_param.findParamByPath("/attr3").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t04_resolve_parampath_nested_attr_complex() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		SampleNested sample_nested = new SampleNested("blue", "green");
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/nested").setState(sample_nested);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/nested_attr4_action_complex", Action._get), null);
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertTrue(StringUtils.equalsIgnoreCase(sample_nested.getAttr_1(), (String)multi_root_param.findParamByPath("/nested_attr4/attr_1").getState()));
		assertTrue(StringUtils.equalsIgnoreCase(sample_nested.getAttr_2(), (String)multi_root_param.findParamByPath("/nested_attr4/attr_2").getState()));
		assertEquals("blue",(String)multi_root_param.findParamByPath("/nested_attr4/attr_1").getState());
		assertEquals("green",(String)multi_root_param.findParamByPath("/nested_attr4/attr_2").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t05_resolve_nested_attr_valid() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		SampleNested sample_nested = new SampleNested("blue", "green");
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/nested").setState(sample_nested);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/nested_attr4_action", Action._get), null);
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals("blue", multi_root_param.findParamByPath("/attr3").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t06_resolve_coll_attr_complex() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		List<String> str_lst = new ArrayList<String>(Arrays.asList("latin","greek","spanish"));
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/simple_coll").findIfCollection().addAll(str_lst);
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/coll_attr5_action_complex", Action._get), null);
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t07_resolve_coll_attr_valid() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		List<String> str_lst = new ArrayList<String>(Arrays.asList("latin","greek","spanish"));
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/simple_coll").findIfCollection().addAll(str_lst);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/coll_attr5_action", Action._get), null);
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals("greek", multi_root_param.findParamByPath("/attr3").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t08_resolve_nested_coll_attr_invalid() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		SampleNested sample_nested1 = new SampleNested("blue", "green");
		SampleNested sample_nested2 = new SampleNested("red", "yellow");
		SampleNested sample_nested3 = new SampleNested("grey", "black");
		List<SampleNested> sample_nested_coll = new ArrayList<>();
		sample_nested_coll.add(sample_nested1);
		sample_nested_coll.add(sample_nested2);
		sample_nested_coll.add(sample_nested3);
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/nested_coll").findIfCollection().addAll(sample_nested_coll);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/nested_coll_attr6_action_complex", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertTrue(StringUtils.equalsIgnoreCase(sample_nested_coll.get(0).getAttr_1(), (String)multi_root_param.findParamByPath("/nested_coll_attr6/0/attr_1").getState()));
		assertTrue(StringUtils.equalsIgnoreCase(sample_nested_coll.get(1).getAttr_1(), (String)multi_root_param.findParamByPath("/nested_coll_attr6/1/attr_1").getState()));
		assertTrue(StringUtils.equalsIgnoreCase(sample_nested_coll.get(2).getAttr_1(), (String)multi_root_param.findParamByPath("/nested_coll_attr6/2/attr_1").getState()));
		
		assertEquals("green",	(String)multi_root_param.findParamByPath("/nested_coll_attr6/0/attr_2").getState());
		assertEquals("yellow",	(String)multi_root_param.findParamByPath("/nested_coll_attr6/1/attr_2").getState());
		assertEquals("black",	(String)multi_root_param.findParamByPath("/nested_coll_attr6/2/attr_2").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t09_resolve_nested_coll_attr_valid() {
		Param<String> lang_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/language");
		lang_param.setState(FRENCH_LANG);
		SampleNested sample_nested1 = new SampleNested("blue", "green");
		SampleNested sample_nested2 = new SampleNested("red", "yellow");
		SampleNested sample_nested3 = new SampleNested("grey", "black");
		List<SampleNested> sample_nested_coll = new ArrayList<>();
		sample_nested_coll.add(sample_nested1);
		sample_nested_coll.add(sample_nested2);
		sample_nested_coll.add(sample_nested3);
		_q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/nested_coll").findIfCollection().addAll(sample_nested_coll);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/nested_coll_attr6_action", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals("yellow", multi_root_param.findParamByPath("/attr3").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t10_resolve_attr_long() {
		Param<Long> count_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/count");
		count_param.setState(new Long("99"));
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/attr7_long_action", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals(new Long("99"), multi_root_param.findParamByPath("/attr7").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t11_resolve_attr_date() {
		Param<LocalDate> date_param = _q.getRoot().findParamByPath("/sample_crossdomain_pathresolver/date_attr");
		LocalDate now = LocalDate.now();
		date_param.setState(now);
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/attr8_date_action", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals(now, multi_root_param.findParamByPath("/attr8").getState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t11_resolve_domain_with_refID() {
		Long id = new Random().nextLong();
		SampleCoreEntity sample_core = new SampleCoreEntity();
		sample_core.setId(id);
		sample_core.setAttr_String("orange");
		mongo.insert(sample_core, "sample_core");
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		final MultiOutput output = MultiOutput.class.cast(Holder.class.cast(multi_new_resp).getState());
		Param<?> param = (Param<?>) output.getSingleResult();
		param.findParamByPath("/attr9_id").setState(id);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/attr9_crossdomain_refId_action", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals("orange", multi_root_param.findParamByPath("/attr9_string").getLeafState());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t11_resolve_domain_with_refID_entity_replace() {
		Long id = new Random().nextLong();
		SampleCoreEntity sample_core = new SampleCoreEntity();
		sample_core.setId(id);
		sample_core.setAttr_String("orange");
		mongo.insert(sample_core, "sample_core");
		
		Holder<MultiOutput> multi_new_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._new), null);
		assertNotNull(multi_new_resp);
		final MultiOutput output = MultiOutput.class.cast(Holder.class.cast(multi_new_resp).getState());
		Param<?> param = (Param<?>) output.getSingleResult();
		param.findParamByPath("/attr9_id").setState(id);
		
		controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT+"/attr9_crossdomain_with_refId_nested_action", Action._get), null);	
		
		Holder<MultiOutput> multi_get_resp = (Holder<MultiOutput>) controller.handleGet(createRequest(SAMPLE_VIEW_MULTI_PARAM_ROOT, Action._get), null);
		assertNotNull(multi_get_resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(multi_get_resp).getState());
		Param<?> multi_root_param = (Param<?>) singleOut.getSingleResult();
		assertNotNull(multi_root_param);
		assertEquals(sample_core.getId(), multi_root_param.findParamByPath("/attr9/id").getLeafState());
		assertEquals(sample_core.getAttr_String(), multi_root_param.findParamByPath("/attr9/attr_String").getLeafState());
	}
}
