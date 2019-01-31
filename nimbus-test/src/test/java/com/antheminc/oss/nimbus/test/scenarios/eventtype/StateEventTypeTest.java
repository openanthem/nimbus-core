package com.antheminc.oss.nimbus.test.scenarios.eventtype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateEventTypeTest extends AbstractFrameworkIntegrationTests {

	private static final String ENTITY_VIEW = "sampleeventtypeview";
	private static final String ENTITY_VIEW_ROOT = PLATFORM_ROOT+"/"+ENTITY_VIEW;
	
	
	@Test
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void t01_initEntity() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ENTITY_VIEW_ROOT)
				.addAction(Action._new)
				.getMock();
		
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Param param = (Param)holder.getState().getSingleResult();
		
		// onStateLoad handlers
		Message attr1Msg = (Message) param.findParamByPath("/attr1").getMessages().iterator().next();
		assertEquals("no override event annotation", attr1Msg.getText());

		Message attr2Msg = (Message) param.findParamByPath("/attr2").getMessages().iterator().next();
		assertEquals("override allowed event annotation", attr2Msg.getText());

		assertNull(param.findParamByPath("/attr3").getMessages());
		
		Message attr4Msg = (Message) param.findParamByPath("/attr4").getMessages().iterator().next();
		assertEquals("conflicing event annotation", attr4Msg.getText());

		assertNull(param.findParamByPath("/attr5").getMessages());
		
		Message attr6Msg = (Message) param.findParamByPath("/attr6").getMessages().iterator().next();
		assertEquals("mandatory override event annotation", attr6Msg.getText());
		
		// onStateChange handlers
		param.findParamByPath("/attr3").setState("attr3");
		
		Message attr3Msg = (Message) param.findParamByPath("/attr3").getMessages().iterator().next();
		assertEquals("no override on state change annotation", attr3Msg.getText());

		param.findParamByPath("/attr5").setState("attr5");
		
		Message attr5Msg = (Message) param.findParamByPath("/attr5").getMessages().iterator().next();
		assertEquals("redundant event annotaiton", attr5Msg.getText());

		

	}
}
