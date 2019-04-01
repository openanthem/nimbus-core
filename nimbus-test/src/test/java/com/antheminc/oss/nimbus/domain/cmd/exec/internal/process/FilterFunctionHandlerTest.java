/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreNestedEntity;

/**
 * 
 * @author Lance Staley
 * @author Tony Lopez
 *
 */
public class FilterFunctionHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private CommandExecutorGateway commandExecutorGateway;

	// /sample_view/attr_list_2_simple/_process?fn=_filter&expr=contains('pickme')
	@SuppressWarnings("unchecked")
	@Test
	public void testSimpleCollection() {
		Long refId = createOrGetDomainRoot_RefId();

		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_2_simple").addAction(Action._get).getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<List<String>> collectionParam = (Param<List<String>>) resp.getState().getSingleResult();

		List<String> initial = new ArrayList<>();
		initial.add("you should pickme");
		initial.add("should should not pick me");
		initial.add("you should also pickme");
		collectionParam.setState(initial);

		Command cmd = CommandBuilder.withUri(
				VIEW_PARAM_ROOT + ":" + refId + "/attr_list_2_simple/_process?fn=_filter&expr=contains('pickme')")
				.getCommand();
		commandExecutorGateway.execute(new CommandMessage(cmd, null));

		Assert.assertEquals(2, collectionParam.getState().size());
		Assert.assertFalse(collectionParam.getState().contains(initial.get(1)));
	}

	// /sample_view/arr/_process?fn=_filter&expr=contains('pickme')
	@SuppressWarnings("unchecked")
	@Test
	public void testSimpleArray() {
		Long refId = createOrGetDomainRoot_RefId();

		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId).addNested("/arr")
				.addAction(Action._get).getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<String[]> collectionParam = (Param<String[]>) resp.getState().getSingleResult();

		String[] initial = new String[] { "you should pickme", "should should not pick me", "you should also pickme" };
		collectionParam.setState(initial);

		Command cmd = CommandBuilder
				.withUri(VIEW_PARAM_ROOT + ":" + refId + "/arr/_process?fn=_filter&expr=contains('pickme')")
				.getCommand();
		commandExecutorGateway.execute(new CommandMessage(cmd, null));

		Assert.assertEquals(2, collectionParam.getState().length);
		Assert.assertFalse(ArrayUtils.contains(collectionParam.getState(), initial[1]));
	}

	// /sample_view/attr_list_1_NestedEntity/_process?fn=_filter&expr=state.nested_attr_String.contains('pickme')
	@SuppressWarnings("unchecked")
	@Test
	public void testComplexCollection() {
		Long refId = createOrGetDomainRoot_RefId();

		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_1_NestedEntity").addAction(Action._get).getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<List<SampleCoreNestedEntity>> collectionParam = (Param<List<SampleCoreNestedEntity>>) resp.getState()
				.getSingleResult();

		List<SampleCoreNestedEntity> initial = new ArrayList<>();
		initial.add(new SampleCoreNestedEntity());
		initial.get(0).setNested_attr_String("you should pickme");
		initial.add(new SampleCoreNestedEntity());
		initial.get(1).setNested_attr_String("should should not pick me");
		initial.add(new SampleCoreNestedEntity());
		initial.get(2).setNested_attr_String("you should also pickme");
		collectionParam.setState(initial);

		Command cmd = CommandBuilder
				.withUri(VIEW_PARAM_ROOT + ":" + refId
						+ "/attr_list_1_NestedEntity/_process?fn=_filter&expr=state.nested_attr_String.contains('pickme')")
				.getCommand();
		commandExecutorGateway.execute(new CommandMessage(cmd, null));

		Assert.assertEquals(2, collectionParam.getState().size());
		Assert.assertFalse(collectionParam.getState().contains(initial.get(1)));
	}

	// /sample_view/attr_list_2_simple/_process?fn=_filter&expr=contains('pickme')&targetParam=/../tmp
	@SuppressWarnings("unchecked")
	@Test
	public void testTargetParam() {
		Long refId = createOrGetDomainRoot_RefId();

		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_2_simple").addAction(Action._get).getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<List<String>> collectionParam = (Param<List<String>>) resp.getState().getSingleResult();

		List<String> initial = new ArrayList<>();
		initial.add("you should pickme");
		initial.add("should should not pick me");
		initial.add("you should also pickme");
		collectionParam.setState(initial);

		Command cmd = CommandBuilder
				.withUri(VIEW_PARAM_ROOT + ":" + refId
						+ "/attr_list_2_simple/_process?fn=_filter&expr=contains('pickme')&targetParam=/../tmp")
				.getCommand();
		commandExecutorGateway.execute(new CommandMessage(cmd, null));

		Param<List<String>> tmp = collectionParam.findParamByPath("/../tmp");
		Assert.assertEquals(2, tmp.getState().size());
		Assert.assertFalse(tmp.getState().contains(initial.get(1)));
	}

	// /sample_view/attr_list_2_simple/_process?fn=_filter&expr=true
	@SuppressWarnings("unchecked")
	@Test
	public void testGivenParamHasNullState() {
		Long refId = createOrGetDomainRoot_RefId();

		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addRefId(refId)
				.addNested("/attr_list_2_simple").addAction(Action._get).getMock();
		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handleGet(req, null);
		Param<List<String>> collectionParam = (Param<List<String>>) resp.getState().getSingleResult();

		Command cmd = CommandBuilder
				.withUri(VIEW_PARAM_ROOT + ":" + refId
						+ "/attr_list_2_simple/_process?fn=_filter&expr=contains('pickme')&targetParam=/../tmp")
				.getCommand();
		commandExecutorGateway.execute(new CommandMessage(cmd, null));

		Assert.assertNull(collectionParam.getState());
	}
}
