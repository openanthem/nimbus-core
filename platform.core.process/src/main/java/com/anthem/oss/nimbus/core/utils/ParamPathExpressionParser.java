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

	public static final String KEY_PREFIX = "<!";
	public static final String KEY_SUFFIX = "!>";
	
	public static Map<Integer, String> parse(String in) {
		Map<Integer, String> entries = new LinkedHashMap<>();
		parse(in, 0, entries);
		
		return entries;
	}
	
	
	private static void parse(String in, int startPos, Map<Integer, String> entries) {
		int iStart = StringUtils.indexOf(in, KEY_PREFIX, startPos);
		if(iStart==StringUtils.INDEX_NOT_FOUND)
			return;
		
		int iEnd = findEndRecursive(in, iStart);
		
		String key = StringUtils.substring(in, iStart, iEnd+KEY_SUFFIX.length());
		entries.put(iStart, key);
		
		parse(in, iEnd, entries);
	}
	
	private static int findEndRecursive(String in, int iStart) {
		int iEnd = StringUtils.indexOf(in, KEY_SUFFIX, iStart);
		if(iEnd==StringUtils.INDEX_NOT_FOUND)
			throw new InvalidConfigException("Found config url entry with starting '"+KEY_PREFIX+"' but no closing '"+KEY_SUFFIX+"' in "+ in);

		// check recursive
		int recursiveCount = countRecursive(in, iStart, iEnd);
		if(recursiveCount==0)
			return iEnd;
		
		int iRecurEnd = iStart;
		for(int c=recursiveCount; c>0; c--) {
			iRecurEnd = StringUtils.indexOf(in, KEY_SUFFIX, iRecurEnd+1);
		}
		return iRecurEnd;
	}
	
	private static int countRecursive(String in, int iStart, int iFirstEnd) {
		String subStrBetweenFirstStartAndFirstEnd = in.substring(iStart+1);
		return StringUtils.countMatches(subStrBetweenFirstStartAndFirstEnd, KEY_SUFFIX);
	}
	
	public static String stripPrefixSuffix(String in) {
		return StringUtils.removeEnd(
				StringUtils.removeStart(in, KEY_PREFIX), KEY_SUFFIX);
	}
	
	public static boolean containsPrefixSuffix(String in) {
		int start = StringUtils.indexOf(in, KEY_PREFIX);
		int end = StringUtils.indexOf(in, KEY_SUFFIX, start);
		
		return (start!=StringUtils.INDEX_NOT_FOUND && end!=StringUtils.INDEX_NOT_FOUND);
	}
}
