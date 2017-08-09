/**
 * 
 */
package com.anthem.oss.nimbus.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Test;

/**
 * @author Soham Chakravarti
 *
 */
public class ParamPathExpressionParserTest {

	@Test
	public void t0_single() {
		String in = "/umcaseview:{/id}/_get";
		
		Map<Integer, String> entries = ParamPathExpressionParser.parse(in);
		assertSame(1, entries.size());
		assertEquals("{/id}", entries.values().iterator().next());
		
		String out = ParamPathExpressionParser.stripPrefixSuffix("{/id}");
		assertEquals("/id", out);
	}
}
