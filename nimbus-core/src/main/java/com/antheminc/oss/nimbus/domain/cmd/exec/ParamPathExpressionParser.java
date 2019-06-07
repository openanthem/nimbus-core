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

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;

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
		return countRecursive(in, iStart, iEnd);
	}
	
	private static int countRecursive(String in, int iStart, int iFirstEnd) {
		// check if there are any starts
		String subStrBetweenFirstStartAndFirstEnd = in.substring(iStart+1, iFirstEnd);
		int countStartsInBetween = StringUtils.countMatches(subStrBetweenFirstStartAndFirstEnd, KEY_PREFIX);
		
		if(countStartsInBetween==0) {
			return iFirstEnd;
		}
		
		// find next end after current end
		int iNextStart = StringUtils.indexOf(in, KEY_PREFIX, iStart+1);
		
		int nextEnd = StringUtils.indexOf(in, KEY_SUFFIX, iFirstEnd+1);
		
		int recursiveNextEnd = countRecursive(in, iNextStart, nextEnd);
		if(nextEnd == recursiveNextEnd)
			return nextEnd;
		
		return countRecursive(in, nextEnd, recursiveNextEnd);
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
