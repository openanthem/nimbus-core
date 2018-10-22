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
package com.antheminc.oss.nimbus.test.scenarios.s13;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ParamUtils;
import com.antheminc.oss.nimbus.test.scenarios.s13.view.S13SampleLineItem;
import com.antheminc.oss.nimbus.test.scenarios.s13.view.S13View;
import com.antheminc.oss.nimbus.test.scenarios.s13.view.S13View.VTTile;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CollectionsStateRetrievalTest extends AbstractFrameworkIngerationPersistableTests {
	
	public static final String CORE_DOMAIN_ALIAS = "s13_core";
	public static final String VIEW_DOMAIN_ALIAS = "s13_view";
	public static final String CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + CORE_DOMAIN_ALIAS;
	public static final String VIEW_PARAM_ROOT = PLATFORM_ROOT + "/" + VIEW_DOMAIN_ALIAS;
	
	private Long refId;
	private Param<S13View> rootParam;
	
	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		super.before();
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Holder<MultiOutput> home_newResp = (Holder<MultiOutput>) controller.handleGet(home_newReq, null);
		Assert.assertNotNull(home_newResp);
		
		this.refId = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		this.rootParam = (Param<S13View>) home_newResp.getState().getSingleResult();
	}
	
	/**
	 * <p>This test case addresses a scenario where invoking a _get call on a param that looks like the following:
	 * <pre>
	 * &#64;MapsTo.Path(linked = false)
	 * &#64;Config(url = "<!#this!>.m/_process?fn=_set&url=/p/s13_core/_search?fn=query&where=s13_core.attr_int.eq(42)")
	 * private List<S13SampleLineItem> grid;
	 * </pre>
	 * and given the result of the framework call {@code /p/s13_core/_search?fn=query&where=s13_core.attr_int.eq(42)} is returning something like:
	 * <pre>
	 * [{
	 *   "attr_String": "I'm the first!",
	 *   "attr_int": 42,
	 * }, {
	 *   "attr_String": "I'm the second!",
	 *   "attr_int": 42,
	 * }]
	 * </pre>
	 * then the returned leafState of the collection is returning a list of elements with the last element as empty, or something
	 * like the following:
	 * <pre>
	 * [{
	 *   "attr_String": "I'm the second!",
	 *   "attr_int": 42,
	 * }, {
	 * 
	 * }]
	 * </pre>
	 * This test case expects that the leaf state is similar to the result of the query call, or: 
	 * <pre>
	 * [{
	 *   "attr_String": "I'm the first!",
	 *   "attr_int": 42,
	 * }, {
	 *   "attr_String": "I'm the second!",
	 *   "attr_int": 42,
	 * }]
	 * </pre>
	 * @see com.antheminc.oss.nimbus.test.scenarios.s13.view.S13View
	 */
	@Test
	@SuppressWarnings({ "unused" })
	public void t01() throws JsonProcessingException {
		
		// Build params to inspect
		Param<VTTile> tileParam = this.rootParam.findParamByPath("/page/tile");
		Param<List<S13SampleLineItem>> gridParam = tileParam.findParamByPath("/section1/grid");
		
		// Create some sample entities to add via a _new call
		S13SampleLineItem sampleCore1 = new S13SampleLineItem("I'm the first!", 42);
		S13SampleLineItem sampleCore2 = new S13SampleLineItem("I'm the second!", 42);

		// Add sampleCore1 via _new call
		Holder<MultiOutput> newResponse1 = this.addViaNewAction(CORE_PARAM_ROOT, sampleCore1);
		
		// Show section1/grid by applying activate conditional logic on field: (see S13View.SampleFilterSection#filter)
		this.selectFilter(1);
		
		// Simulate the UI _get call on: section1/grid
		Holder<MultiOutput> getSection1GridResponse1 = this.getSectionGrid(1);
		
		// Validate the entries in the grid are as expected
		Assert.assertEquals(1, ((List<?>) tileParam.findParamByPath("/section1/grid").getLeafState()).size());
		Assert.assertEquals(sampleCore1.getAttr_String(), gridParam.getLeafState().get(0).getAttr_String());
		Assert.assertEquals(sampleCore1.getAttr_int(), gridParam.getLeafState().get(0).getAttr_int());
		
		// Show section2/grid by applying activate conditional logic on field: (see S13View.SampleFilterSection#filter)
		this.selectFilter(2);
		
		// Simulate the UI _get call on: section2/grid
		Holder<MultiOutput> getSectionGrid2Response1 = this.getSectionGrid(2);
		
		// Validate the entries in the grid are as expected
		Assert.assertEquals(0, ((List<?>) tileParam.findParamByPath("/section2/grid").getLeafState()).size());
		
		// Add sampleCore2 via _new call
		Holder<MultiOutput> newResponse2 = this.addViaNewAction(CORE_PARAM_ROOT, sampleCore2);
		
		// Show section1/grid by applying activate conditional logic on field: (see S13View.SampleFilterSection#filter)
		this.selectFilter(1);
		
		// Simulate the UI _get call on: section1/grid
		Holder<MultiOutput> getSection1GridResponse2 = this.getSectionGrid(1);
		
		// TODO Remove the following block of commented lines once the test case is passing.
		// NOTE: Enabling the following line of code will make the test pass. (making a 2nd _get call)
//		Holder<MultiOutput> getSection1GridResponse3 = this.getSectionGrid(1);
		
		// Validate the entries in the grid are as expected
		Assert.assertEquals(2, gridParam.getLeafState().size());
		Assert.assertEquals(sampleCore1.getAttr_String(), gridParam.getLeafState().get(0).getAttr_String());
		Assert.assertEquals(sampleCore1.getAttr_int(), gridParam.getLeafState().get(0).getAttr_int());
		Assert.assertEquals(sampleCore2.getAttr_String(), gridParam.getLeafState().get(1).getAttr_String());
		Assert.assertEquals(sampleCore2.getAttr_int(), gridParam.getLeafState().get(1).getAttr_int());
	}
	
	@SuppressWarnings("unchecked")
	private Holder<MultiOutput> selectFilter(int filterNum) throws JsonProcessingException {
		
		ModelEvent<String> event = new ModelEvent<>();
		event.setId("/" + VIEW_DOMAIN_ALIAS + ":" + this.refId + "/page/tile/filterSection/filter");
		event.setType(Action._update.name());
		event.setPayload(this.om.writeValueAsString(filterNum));
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handleEventNotify(MockHttpRequestBuilder.withUri(PLATFORM_ROOT).addNested("/event/notify").getMock(), event);
		
		final String sectionName = "/section" + filterNum;
		Param<?> tileParam = ParamUtils.extractResponseByParamPath(response, sectionName).findParamByPath("/..");
		
		// Validate the appropriate section and grid are active.
		Assert.assertTrue(tileParam.findParamByPath(sectionName).isActive());
		Assert.assertTrue(tileParam.findParamByPath(sectionName + "/grid").isActive());
		
		// Validate that the other sections are not active.
		for (int i = 1; i<= 2; i++) {
			if (i != filterNum) {
				Assert.assertFalse(tileParam.findParamByPath("/section" + i).isActive());
				Assert.assertFalse(tileParam.findParamByPath("/section" + i + "/grid").isActive());
			}
		}
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private Holder<MultiOutput> addViaNewAction(String path, Object toCreate) throws JsonProcessingException {
		MockHttpServletRequest newReq = MockHttpRequestBuilder.withUri(path)
				.addAction(Action._new)
				.getMock();
		return (Holder<MultiOutput>) controller.handlePost(newReq, this.om.writeValueAsString(toCreate));
	}
	
	@SuppressWarnings("unchecked")
	private Holder<MultiOutput> getSectionGrid(int filterNum) throws JsonProcessingException {
		MockHttpServletRequest getSectionGridRequest = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addRefId(this.refId)
				.addNested("/page/tile/section" + filterNum + "/grid")
				.addAction(Action._get)
				.getMock();
		return (Holder<MultiOutput>) controller.handlePost(getSectionGridRequest, null);
	}
}
