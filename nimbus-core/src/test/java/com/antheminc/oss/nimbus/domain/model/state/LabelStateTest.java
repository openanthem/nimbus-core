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

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.LabelState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.StyleState;

/**
 * @author Tony Lopez
 *
 */
public class LabelStateTest {

	@Test
	public void testEquals() {
		LabelState testee = new LabelState();
		testee.setLocale("US-en");
		
		Assert.assertTrue(testee.equals(null));
		Assert.assertFalse(testee.equals("not equal"));
		
		testee.setText("hello");
		Assert.assertFalse(testee.equals(null));
		
		LabelState comparer = new LabelState();
		Assert.assertFalse(testee.equals(comparer));
		
		comparer.setLocale("GB-en");
		Assert.assertFalse(testee.equals(comparer));
		
		comparer.setLocale("US-en");
		comparer.setText("world");
		Assert.assertFalse(testee.equals(comparer));
		
		comparer.setText("hello");
		Assert.assertTrue(testee.equals(comparer));
	}
	
	@Test
	public void testHashCode() {
		LabelState a = new LabelState();
		a.setText("alpha");
		LabelState b = new LabelState();
		b.setText("beta");
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		LabelState c = new LabelState();
		c.setText("alpha");
		Assert.assertEquals(a.hashCode(), c.hashCode());
	}
}
