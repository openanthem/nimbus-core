package com.antheminc.oss.nimbus.domain.model.state.extension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState.LeafState;
import com.antheminc.oss.nimbus.domain.model.state.internal.MappedDefaultParamState.MappedLeafState;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Akancha Kashyap
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MessageConditionalEventHandlerTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {

		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_view/_new").getCommand();
		return cmd;
	}
	
	@Test
	public void t00_init() {
		assertNotNull(_q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox"));
		
	}
	
	@Test
	public void t01_testOnStateChangeMessage_1() {
		Param<Integer> inputParam = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox");
	
		Assert.assertNull(inputParam.getMessages());
		assertFalse(inputParam.hasContextStateChanged());
		inputParam.setState(101);
		
		assertNotNull(inputParam.getMessages());
		assertEquals(1, inputParam.getMessages().size());
		assertEquals("This is a Test Warning Message", inputParam.getMessages().iterator().next().getText());
		assertTrue(inputParam.hasContextStateChanged());
	}
	
	@Test
	public void t02_testOnStateLoadMessage_2() {
		Param<String> inputParam = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testMessageTextBox2");
		
		assertNotNull(inputParam.getMessages());
		assertEquals(1, inputParam.getMessages().size());
		assertEquals("This is a Test Warning Message", inputParam.getMessages().iterator().next().getText());
		assertTrue(inputParam.hasContextStateChanged());
	}
	
	@Test
	public void t02_testOnStateLoadMessageWithSpelMessage_3() {
		Param<Integer> inputParam = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox");
		inputParam.setState(102);
		
		Param<String> inputParam2 = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox3");
		Assert.assertNull(inputParam2.getMessages());
		assertFalse(inputParam2.hasContextStateChanged());
		
		inputParam2.setState("No");
		
		assertNotNull(inputParam2.getMessages());
		assertEquals(1, inputParam2.getMessages().size());
		assertEquals("102", inputParam2.getMessages().iterator().next().getText());
		assertTrue(inputParam2.hasContextStateChanged());
		
		
		// Reset the when condition- and verify if the msg is deleted
		inputParam2.setState("ABC");
		Assert.assertNull(inputParam2.getMessages());
		assertTrue(inputParam2.hasContextStateChanged());
		
		// Now do a set state twice and make sure only one msg is added
		inputParam2.setState("No");
		inputParam2.setState("No");
		
		assertNotNull(inputParam2.getMessages());
		assertEquals(1, inputParam2.getMessages().size());
		assertEquals("102", inputParam2.getMessages().iterator().next().getText());
		assertTrue(inputParam2.hasContextStateChanged());
		
	}
	
	// Test to check the UI params are getting updated 
	@Test
	public <T, M> void t02_testOnStateLoadMessageWithSpelMessage_4() throws IOException {
		
		JacksonTester.initFields(this, om);
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri("Anthem/fep/p/sample_view/")
				.addAction(Action._new)
				.getMock();

		Holder<MultiOutput> resp = (Holder<MultiOutput>) controller.handlePost(req, null);
		assertNotNull(resp);
		final MultiOutput singleOut = MultiOutput.class.cast(Holder.class.cast(resp).getState());
		Param<?> param = (Param<?>) singleOut.getSingleResult();

		String coreId = param.findParamByPath("/.m/id").getState().toString();
		
		
		param.findParamByPath("/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox").setState(102);
		
		
		Param<String> inputParam2 = param.findParamByPath("/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox3");
		Assert.assertNull(inputParam2.getMessages());
		assertFalse(inputParam2.hasContextStateChanged());
		// Now fire an update for testWarningTextBox3
		
		
		
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(PLATFORM_ROOT)
				.addNested("/event/notify")
				.getMock();
		
		ModelEvent<String> modelEvent_q1 = new ModelEvent<>();
		modelEvent_q1.setId("/sample_view:"+coreId+"/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox3/_update");
		modelEvent_q1.setType(Action._update.name());
		modelEvent_q1.setPayload(json.write("No").getJson());
		
		Holder<MultiOutput> textBox3Resp = (Holder<MultiOutput>) controller.handleEventNotify(httpReq, modelEvent_q1);
		assertThat(textBox3Resp).isNotNull();
		
		final MultiOutput textBox3Output = MultiOutput.class.cast(Holder.class.cast(textBox3Resp).getState());
		List<Output<?>> outputs = textBox3Output.getOutputs();
		
		// Now assert that we have atleast one Output - where the value is not a boolean and the message has correct Values
		boolean oneGoodOutputFound = false;
		for(Output<?> out :outputs) {
			if(out.getValue()  instanceof Boolean) continue;
			
			MappedLeafState<T,M> x = (MappedLeafState<T,M>) out.getValue();
			assertNotNull(x.getMessages());
			assertEquals(1, x.getMessages().size());
			assertEquals("102", x.getMessages().iterator().next().getText());
			assertTrue(x.hasContextStateChanged());
			oneGoodOutputFound = true;
			
		}
		assertTrue(oneGoodOutputFound);
		
		// Reset the when condition- and verify if the msg is deleted
		ModelEvent<String> modelEvent_2 = new ModelEvent<>();
		modelEvent_2.setId("/sample_view:"+coreId+"/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox3/_update");
		modelEvent_2.setType(Action._update.name());
		modelEvent_2.setPayload(json.write("ABC").getJson());
		
		Holder<MultiOutput> resp2 = (Holder<MultiOutput>) controller.handleEventNotify(httpReq, modelEvent_2);
		assertThat(resp2).isNotNull();

		
		final MultiOutput textBox3Output2 = MultiOutput.class.cast(Holder.class.cast(resp2).getState());
		assertNotNull(textBox3Output2);
	
		List<Output<?>> outputs2 = textBox3Output2.getOutputs();
		
		// Now assert that we have atleast one Output - where the value is not a boolean and the Message is Null
		boolean oneGoodOutFound2 = false;
		for(Output<?> out :outputs2) {
			if(out.getValue()  instanceof Boolean) continue;
			MappedLeafState<T,M> x = (MappedLeafState<T,M>) out.getValue();
			assertNull(x.getMessages());
			
			oneGoodOutFound2 = true;
			
		}
		assertTrue(oneGoodOutFound2);
		
		// Now set some message again
		ModelEvent<String> modelEvent_3 = new ModelEvent<>();
		modelEvent_3.setId("/sample_view:"+coreId+"/page_aqua/vtAqua/vsSampleForm/vfSampleForm/testWarningTextBox3/_update");
		modelEvent_3.setType(Action._update.name());
		modelEvent_3.setPayload(json.write("No").getJson());
		
		Holder<MultiOutput> resp3 = (Holder<MultiOutput>) controller.handleEventNotify(httpReq, modelEvent_3);
		assertThat(resp3).isNotNull();

		
		final MultiOutput textBox3Output3 = MultiOutput.class.cast(Holder.class.cast(resp3).getState());
		assertNotNull(textBox3Output3);
	
		List<Output<?>> outputs3 = textBox3Output3.getOutputs();
		
		// Now assert that we have atleast one Output - where the value is not a boolean and the Message is Null
		boolean oneGoodOutFound3 = false;
		for(Output<?> out :outputs3) {
			if(out.getValue()  instanceof Boolean) continue;
			MappedLeafState<T,M> x = (MappedLeafState<T,M>) out.getValue();
			assertNotNull(x.getMessages());
			assertEquals(1, x.getMessages().size());
			assertEquals("102", x.getMessages().iterator().next().getText());
			assertTrue(x.hasContextStateChanged());
			
			oneGoodOutFound3 = true;
			
		}
		assertTrue(oneGoodOutFound3);
		
	}
	
	@Test
	public void t05_message_targetpath() {
		Param<String> inputParam = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p7_messagetest");
		
		Assert.assertNull(inputParam.getMessages());
		assertFalse(inputParam.hasContextStateChanged());
		inputParam.setState("Set");
		
		Param<String> messageParam = _q.getRoot().findParamByPath("/sample_view/page_aqua/vtAqua/vsSampleForm/vfSampleForm/p8_message");
		assertNotNull(messageParam.getMessages());
		assertEquals(1, messageParam.getMessages().size());
		assertEquals("Message is set on p8_message", messageParam.getMessages().iterator().next().getText());
		assertTrue(messageParam.hasContextStateChanged());
		
		inputParam.setState("Reset");
		assertNull(messageParam.getMessages());
		assertTrue(messageParam.hasContextStateChanged());
		
		
	}
}