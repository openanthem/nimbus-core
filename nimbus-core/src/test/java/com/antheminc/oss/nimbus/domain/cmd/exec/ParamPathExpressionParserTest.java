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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Iterator;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParamPathExpressionParserTest {

	@Test
	public void t0_single() {
		String in = "/umcaseview:<!/id!>/_get";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(1, entries.size());
		assertEquals("<!/id!>", entries.values().iterator().next());
		
		String out = ParamPathExpressionParser.stripPrefixSuffix("<!/id!>");
		assertEquals("/id", out);
	}
	
	@Test(expected = InvalidConfigException.class)
	public void t1_missingClose() {
		ParamPathExpressionParser.parse("/umcaseview:<!/id>/_get");
	}
	
	@Test
	public void t2_recursive_simple() {
		String in = "abc/<!p1!>/another2/<!../p2/<!../p3!>/p4!>/another3/<!p100/someB/<!../p5/someA/!>/xyz/<!p6!>/someC!>";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(3, entries.size());
		
		Iterator<String> iter = entries.values().iterator();
		assertEquals("<!p1!>", iter.next());
		assertEquals("<!../p2/<!../p3!>/p4!>", iter.next());
		assertEquals("<!p100/someB/<!../p5/someA/!>/xyz/<!p6!>/someC!>", iter.next());
	}
	
	@Test
	public void t3_multiple() {
		String in = "/umcaseview:<!/id!>/nested/<!/path2!>/_get";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(2, entries.size());
		
		Iterator<String> iter = entries.values().iterator();
		assertEquals("<!/id!>", iter.next());
		assertEquals("<!/path2!>", iter.next());
	}
	
	@Test
	public void t4_recursive_multiple() {
		String in = "/umcaseview:<!../.m/<!/id!>/another/<!path4!>/path3!>/nested/<!/path2!>/_get";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(2, entries.size());
		
		Iterator<String> iter = entries.values().iterator();
		assertEquals("<!../.m/<!/id!>/another/<!path4!>/path3!>", iter.next());
		assertEquals("<!/path2!>", iter.next());
	}
	
	@Test
	public void t5_multiple_next() {
		String in = "/testParam3/_update?rawPayload=\"<!../testParam!><!../testParam2!>\"";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(2, entries.size());
		
		Iterator<String> iter = entries.values().iterator();
		assertEquals("<!../testParam!>", iter.next());
		assertEquals("<!../testParam2!>", iter.next());
	}
	
	@Test
	public void t6_nested2() {
		String in = "/testParam3/_update?rawPayload=\"<!../<!../testParam2!>abc!>\"";
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(1, entries.size());
		
		Iterator<String> iter = entries.values().iterator();
		assertEquals("<!../<!../testParam2!>abc!>", iter.next());
	}
}
