
package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Akancha Kashyap
 *
 */
public class MessageConditionalEventHandlerTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {

		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	@Test
	public void t00_init() {
		assertNotNull(_q.getRoot().findParamByPath("/sample_view/page_orange/vtOrange/vsSampleForm/vfSampleForm/testWarningTextBox"));
		
	}
	@Test
	public void t01_testOnStateChangeMessage_1() {
		Param<String> inputParam = _q.getRoot().findParamByPath("/sample_view/page_orange/vtOrange/vsSampleForm/vfSampleForm/testWarningTextBox");
		
		Assert.assertNull(inputParam.getMessage());
		inputParam.setState("Yes");
		Assert.assertNotNull(inputParam.getMessage());
		Assert.assertEquals(inputParam.getMessage().getText(), "This is a Test Warning Message");
		
	}
	@Test
	public void t02_testOnStateLoadMessage_1() {
		Param<String> inputParam = _q.getRoot().findParamByPath("/sample_view/page_orange/vtOrange/vsSampleForm/vfSampleForm/testMessageTextBox2");
		
		Assert.assertNotNull(inputParam.getMessage());
		Assert.assertEquals(inputParam.getMessage().getText(), "This is a Test Warning Message");
		
	}
}
