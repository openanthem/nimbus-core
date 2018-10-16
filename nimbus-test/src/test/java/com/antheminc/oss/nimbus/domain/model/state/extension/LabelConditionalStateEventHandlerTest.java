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

import java.util.Locale;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;

/**
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelConditionalStateEventHandlerTest extends AbstractStateEventHandlerTests {

	@Test
	public void t01_labelConditional_single() {

		Param<String> p_testMessageTextBox2 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testMessageTextBox2");

		// Validate original label is set
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));

		// Trigger the label is set conditionally
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox").setState(102);

		// Validate the label config is set
		Assert.assertEquals("Awesome Textbox 2", getLabelText(p_testMessageTextBox2));
	}

	@Test
	public void t02_labelConditional_multiple() {

		Param<String> p_testMessageTextBox2 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testMessageTextBox2");

		// Validate original label is set
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));

		Param<Integer> p_testWarningTextBox = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox");

		// Trigger the label is set conditionally and validate
		p_testWarningTextBox.setState(102);
		Assert.assertEquals("Awesome Textbox 2", getLabelText(p_testMessageTextBox2));

		// Trigger the label is set conditionally and validate
		p_testWarningTextBox.setState(103);
		Assert.assertEquals("Super Awesome Textbox 2", getLabelText(p_testMessageTextBox2));
	}

	@Test
	public void t03_labelConditional_dontApply() {
		// Validate original label is set
		Param<String> p_testMessageTextBox2 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testMessageTextBox2");
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));

		// Trigger the label is set conditionally
		getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox").setState(999);

		// Validate the label config is set
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));
	}

	@Test
	public void t04_labelConditional_resetToInitialLabelConfig() {

		Param<String> p_testMessageTextBox2 = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testMessageTextBox2");

		// Validate original label is set
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));

		Param<Integer> p_testWarningTextBox = getParam(
				"/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox");

		// Trigger the label is set conditionally and validate
		p_testWarningTextBox.setState(102);
		Assert.assertEquals("Awesome Textbox 2", getLabelText(p_testMessageTextBox2));

		// Trigger the label is set conditionally and validate
		p_testWarningTextBox.setState(103);
		Assert.assertEquals("Super Awesome Textbox 2", getLabelText(p_testMessageTextBox2));

		// Trigger the label is set conditionally and validate (back to default)
		p_testWarningTextBox.setState(0);
		Assert.assertEquals("Textbox 2", getLabelText(p_testMessageTextBox2));
	}

	@Test
	public void t05_labelConditional_resetToInitialLabelConfig_none() {
		// Validate original label is set
		Param<String> p_p4 = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p4");
		Assert.assertNull(p_p4.getLabels());

		// Trigger the label is set conditionally and validate
		p_p4.setState("test-label-conditional-1");
		Assert.assertEquals("Amazing Label", getLabelText(p_p4));

		// Trigger the label is set conditionally and validate
		p_p4.setState(null);
		Assert.assertNull(p_p4.getLabels());
	}
	
	@Test
	public void t06_labelConditional_commandDSL() {
		// Validate original label is set
		Param<String> p_p4 = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p4");
		Assert.assertNull(p_p4.getLabels());

		// Trigger the label is set conditionally and validate
		Param<String> p_p5 = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p5");
		p_p5.setState("World");
		Assert.assertNull(p_p4.getLabels());
		
		// Trigger the label is set conditionally and validate
		p_p4.setState("test-label-conditional-2");
		Assert.assertEquals("Hello World!", getLabelText(p_p4));
	}
	
	@Test
	public void t07_labelConditional_exclusive() {
		// Validate original labels are set
		Param<Integer> p_p6_exclusive = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p6_exclusive");
		Param<Integer> p_p6_nonExclusive = getParam("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p6_nonExclusive");
		Assert.assertNull(p_p6_exclusive.getLabels());
		Assert.assertNull(p_p6_nonExclusive.getLabels());

		// Trigger the exclusive label and check if it is set appropriately
		p_p6_exclusive.setState(20);
		Assert.assertEquals("I'm greater than 5!", getLabelText(p_p6_exclusive));
		
		// Trigger the non-exclusive label and check if it is set appropriately
		p_p6_nonExclusive.setState(20);
		Assert.assertEquals("I'm greater than 10!", getLabelText(p_p6_nonExclusive));
	}

	private <T> String getLabelText(Param<T> param) {
		return param.getDefaultLabel().getText();
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
