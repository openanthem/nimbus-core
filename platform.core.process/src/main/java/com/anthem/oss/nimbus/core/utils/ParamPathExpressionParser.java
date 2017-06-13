/**
 * 
 */
package com.anthem.oss.nimbus.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public class ParamPathExpressionParser {

	public static final String KEY_PREFIX = "{";
	public static final String KEY_SUFFIX = "}";
	
	public static Map<Integer, String> parse(String in) {
		Map<Integer, String> entries = new LinkedHashMap<>();
		parse(in, 0, entries);
		
		return entries;
	}
	
	
	private static void parse(String in, int startPos, Map<Integer, String> entries) {
		int iStart = StringUtils.indexOf(in, KEY_PREFIX, startPos);
		if(iStart==StringUtils.INDEX_NOT_FOUND)
			return;
		
		int iEnd = StringUtils.indexOf(in, KEY_SUFFIX, iStart);
		if(iEnd==StringUtils.INDEX_NOT_FOUND)
			throw new InvalidConfigException("Found config url entry with starting '{' but no closing '}' in "+ in);
		
		String key = StringUtils.substring(in, iStart, iEnd+1);
		entries.put(iStart, key);
		
		parse(in, iEnd, entries);
	}
	
	public static String stripPrefixSuffix(String in) {
		return StringUtils.removeEnd(
				StringUtils.removeStart(in, KEY_PREFIX), KEY_SUFFIX);
	}
}
