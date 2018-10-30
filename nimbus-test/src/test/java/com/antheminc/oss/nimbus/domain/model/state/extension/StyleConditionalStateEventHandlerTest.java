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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import org.junit.Assert;
import org.junit.Test;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Tony Lopez
 *
 */
public class StyleConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	@Test
	public void testStyleConditionalSingle() {

		Param<String> p_p0 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0");

		// Validate original style is set
		Assert.assertNull(p_p0.getStyle());

		// Trigger the and validate the style is set conditionally
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p1").setState(1);
		Assert.assertEquals("red", p_p0.getStyle().getCssClass());
	}

	@Test
	public void testStyleConditionalMultiple() {

		Param<Integer> p_p2 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p2");
		
		// Validate original label is set
		Assert.assertNull(getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0").getStyle());
		
		// Trigger and validate the style is set conditionally
		p_p2.setState(1);
		Assert.assertEquals("red", getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0").getStyle().getCssClass());
		
		// Trigger and validate the style is set conditionally
		p_p2.setState(2);
		Assert.assertEquals("blue", getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0").getStyle().getCssClass());
		
		// Trigger and validate the style is set conditionally
		p_p2.setState(3);
		Assert.assertEquals("green", getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0").getStyle().getCssClass());
	}

	@Test
	public void testStyleConditionalDontApply() {
		Param<String> p_p0 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0");

		// Validate original style is set
		Assert.assertNull(p_p0.getStyle());

		// Trigger and validate the style is NOT set conditionally
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p1").setState(999);
		Assert.assertNull(p_p0.getState());
	}

	@Test
	public void testStyleConditionalReset() {

		Param<String> p_p0 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p0");

		// Validate original style is set
		Assert.assertNull(p_p0.getStyle());

		// Trigger the and validate the style is set conditionally
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p1").setState(1);
		Assert.assertEquals("red", p_p0.getStyle().getCssClass());
		
		// Validate original style is set
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p1").setState(0);
		Assert.assertNull(p_p0.getStyle());
	}
	
	@Test
	public void testStyleConditional_exclusive() {
		// Validate original styles are set
		Param<Integer> p_exclusive = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p3Exclusive");
		Param<Integer> p_nonExclusive = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfStyleForm/p3NonExclusive");
		Assert.assertNull(p_exclusive.getStyle());
		Assert.assertNull(p_nonExclusive.getStyle());

		// Trigger the exclusive style and check if it is set appropriately
		p_exclusive.setState(20);
		Assert.assertEquals("red", p_exclusive.getStyle().getCssClass());
		
		// Trigger the non-exclusive style and check if it is set appropriately
		p_nonExclusive.setState(20);
		Assert.assertEquals("green", p_nonExclusive.getStyle().getCssClass());
	}

	private <T> Param<T> getParam(String paramPath) {
		return _q.getRoot().findParamByPath(paramPath);
	}

	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
}