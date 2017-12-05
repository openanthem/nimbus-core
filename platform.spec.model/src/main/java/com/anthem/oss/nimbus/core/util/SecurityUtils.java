/**
 * 
 */
package com.antheminc.oss.nimbus.core.util;

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
