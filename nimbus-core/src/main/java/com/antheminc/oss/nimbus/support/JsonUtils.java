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
package com.antheminc.oss.nimbus.support;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
public class JsonUtils {

	private static ObjectMapper om = new ObjectMapper();
	
	/**
	 * <p>Determine if the provided {@code json} is considered to be a JSON array.  
	 * @param json the JSON string to validate
	 * @return {@code true} if {@code json} is a JSON array
	 */
	public static boolean isJsonArray(String json) {
		try {
			JsonNode jsonTree = om.readerFor(new TypeReference<Map<String, JsonNode>>() {}).readTree(json);
			return jsonTree.isArray();
		} catch (Exception e) {
			return false;
		}
	}
}
