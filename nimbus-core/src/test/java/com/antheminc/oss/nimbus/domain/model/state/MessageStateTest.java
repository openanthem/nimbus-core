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
package com.antheminc.oss.nimbus.domain.model.state;

import org.junit.Assert;
import org.junit.Test;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;

/**
 * @author Tony Lopez
 *
 */
public class MessageStateTest {

	@Test
	public void testEquals() {
		Message testee = new Message("1", null, null, null, "class");
		Assert.assertTrue(testee.equals(null));
		Assert.assertFalse(testee.equals("not equal"));
		testee = new Message("1", "message", Message.Type.INFO, Message.Context.INLINE, "class");
		Assert.assertFalse(testee.equals(null));
		Assert.assertTrue(testee.equals(new Message("1", "message", Message.Type.INFO, Message.Context.INLINE, "class")));
		Assert.assertFalse(testee.equals(new Message("2", "message", Message.Type.INFO, Message.Context.INLINE, "class")));
	}
	
	@Test
	public void testHashCode() {
		Message a = new Message("1", "message", Message.Type.INFO, Message.Context.INLINE, "class");
		Message b = new Message("2", "message", Message.Type.INFO, Message.Context.INLINE, "class");
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		Message c = new Message("1", "message", Message.Type.INFO, Message.Context.INLINE, "class");
		Assert.assertEquals(a.hashCode(), c.hashCode());
	}
}
