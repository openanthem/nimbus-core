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
package com.antheminc.oss.nimbus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Swetha Vemuri
 *
 */
public class UniqueIdGenerationUtil {

	private static final DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss.SSS");
	
	public static String generateUniqueId() {
		LocalDateTime datetime = LocalDateTime.now();
		
		StringBuilder str = new StringBuilder();
		str = str.append("{");
		str.append(dateformat.format(datetime));
		str.append("-");
		str.append(Math.random());
		str.append("}");
		return str.toString();		
	}
}
