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
package com.antheminc.oss.nimbus.support;

import org.owasp.esapi.codecs.HTMLEntityCodec;

import lombok.extern.slf4j.Slf4j;

/**
 * Security Utils for logging securely. Data being logged should pass white listing before logging.
 * Encoding the log using ESAPI HTMLEntityCodec.
 * 
 * @author Swetha Vemuri
 *
 */
@Slf4j
public class SecurityUtils {
	
	public static final String DEFAULT_SECURE = "^[a-zA-Z0-9<>()\\[\\]@/: &.=?,$#_-]{1,1000}";
	
	protected static String SECURE = DEFAULT_SECURE;
	private static final char immune[] = {};
	private static final HTMLEntityCodec encoder = new HTMLEntityCodec();
	
	public SecurityUtils(String secure) {
		SecurityUtils.SECURE = secure;
	}

	private static String scanStringForSecureLogging(String inputString, String regExp) {
		regExp = (regExp==null) ? DEFAULT_SECURE : regExp;
		
		if(inputString.matches(regExp)){
			return inputString;
		}	
		return encoder.encode(immune, inputString);
	}
	
	protected static String scanObjectForSecureLogging(Object input, String regExp) {
		try {
			return scanStringForSecureLogging(String.valueOf(input), regExp);
		}
		catch(Exception e) {
			log.error("Input object validation failed while scanning for security using REGEX: ["+regExp+"] ",e);
			return encoder.encode(immune, String.valueOf(input));
		}		
	}
}
