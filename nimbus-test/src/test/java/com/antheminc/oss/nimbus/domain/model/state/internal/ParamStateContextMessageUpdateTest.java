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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.model.state.AbstractStateEventHandlerTests;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Context;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Type;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamStateContextMessageUpdateTest extends AbstractStateEventHandlerTests {

	@Override
	protected Command createCommand() {
		Command cmd = CommandBuilder.withUri("/hooli/thebox/p/sample_expr/_new").getCommand();
		return cmd;
	}

	private void addMessage(Param<?> p, Message msg) {
		Set<Message> newMsgs = new HashSet<>();
		if(!CollectionUtils.isEmpty(p.getMessages()))
			newMsgs.addAll(p.getMessages());
		
		newMsgs.add(msg);
		p.setMessages(newMsgs);
	}
	
	@Test
	public void t01_single_set() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("TestUniqueId","test message "+ new Date(), Type.INFO, Context.INLINE,"");
		
		addListener();
		addMessage(msg_trigger, msg);
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(msg_trigger);
		
		
		assertNotNull(_paramEvents);
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
		
		assertTrue(msg_trigger.getMessages().contains(msg));
		assertTrue(msg_trigger.hasContextStateChanged());
	}
	

	@Test
	public void t02_multi_set_same_msg() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("TestUniqueId","test message "+ new Date(), Type.INFO, Context.INLINE,"");
		
		// set once
		addMessage(msg_trigger, msg);
		
		// set same instance again
		addListener();
		addMessage(msg_trigger, msg);
		
		// validate that its not triggered again
		assertNull(_paramEvents);

		// from first message set
		assertTrue(msg_trigger.getMessages().contains(msg));
		
		// SOHAM: given that message was already present, f/w should detect that the context hasn't changed
		//== TODO assertFalse(msg_trigger.hasContextStateChanged());
	}
	
	@Test
	public void t03_multi_set_changed_msg() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("TestUniqueId","test message "+ new Date(), Type.INFO, Context.INLINE,"");
		
		// set once
		addMessage(msg_trigger, msg);
		
		// change text in same Message instance
		addListener();
		Message msg2 = new Message("TestUniqueId2","new text "+ new Date(), Type.INFO, Context.INLINE,"");
		addMessage(msg_trigger, msg2);
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(msg_trigger);
		
		// validate that it is triggered again
		assertNotNull(_paramEvents);
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
		
		// from 2nd message set
		assertTrue(msg_trigger.getMessages().contains(msg));
		assertTrue(msg_trigger.getMessages().contains(msg2));
		assertTrue(msg_trigger.hasContextStateChanged());
	}
}
