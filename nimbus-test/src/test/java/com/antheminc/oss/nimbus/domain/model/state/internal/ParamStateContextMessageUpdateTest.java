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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	@Test
	public void t01_single_set() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("test message "+ new Date(), Type.INFO, Context.INLINE);
		
		addListener();
		msg_trigger.setMessage(msg);
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(msg_trigger);
		
		
		assertNotNull(_paramEvents);
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
		
		assertEquals(msg, msg_trigger.getMessage());
	}
	

	@Test
	public void t02_multi_set_same_msg() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("test message "+ new Date(), Type.INFO, Context.INLINE);
		
		// set once
		msg_trigger.setMessage(msg);
		
		// set same instance again
		addListener();
		msg_trigger.setMessage(msg);
		
		// validate that its not triggered again
		assertNull(_paramEvents);

		// from first message set
		assertEquals(msg, msg_trigger.getMessage());
		
	}
	
	@Test
	public void t03_multi_set_changed_msg() {
		Param<String> msg_trigger = _q.getRoot().findParamByPath("/sample_expr/triggerMessageUpdate");
		assertNotNull(msg_trigger);
		
		Message msg = new Message("test message "+ new Date(), Type.INFO, Context.INLINE);
		
		// set once
		msg_trigger.setMessage(msg);
		
		// change text in same Message instance
		addListener();
		Message msg2 = new Message("new text "+ new Date(), Type.INFO, Context.INLINE);
		msg_trigger.setMessage(msg2);
		
		List<Param<?>> expectedEventParams = new ArrayList<>();
		expectedEventParams.add(msg_trigger);
		
		// validate that it is triggered again
		assertNotNull(_paramEvents);
		_paramEvents.stream()
			.forEach(pe->expectedEventParams.remove(pe.getParam()));
		
		assertTrue(expectedEventParams.isEmpty());
		
		// from 2nd message set
		assertEquals(msg2, msg_trigger.getMessage());
		
	}
}
