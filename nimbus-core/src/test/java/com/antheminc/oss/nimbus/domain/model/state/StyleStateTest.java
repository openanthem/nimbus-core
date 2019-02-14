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

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.StyleState;

/**
 * @author Tony Lopez
 *
 */
public class StyleStateTest {

	@Test
	public void testEquals() {
		StyleState testee = new StyleState();
		testee.setCssClass("alpha");
		
		Assert.assertFalse(testee.equals(null));
		Assert.assertFalse(testee.equals("not equal"));
		Assert.assertTrue(testee.equals(testee));
		
		StyleState equivalent = new StyleState();
		equivalent.setCssClass("alpha");
		Assert.assertTrue(testee.equals(equivalent));
	}
	
	@Test
	public void testHashCode() {
		StyleState a = new StyleState();
		a.setCssClass("alpha");
		StyleState b = new StyleState();
		b.setCssClass("beta");
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
		StyleState c = new StyleState();
		c.setCssClass("alpha");
		Assert.assertEquals(a.hashCode(), c.hashCode());
	}
}
