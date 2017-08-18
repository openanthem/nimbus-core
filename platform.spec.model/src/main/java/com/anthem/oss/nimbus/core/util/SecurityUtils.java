/**
 * 
 */
package com.anthem.oss.nimbus.core.util;

import org.owasp.esapi.codecs.HTMLEntityCodec;

/**
 * Security Utils for logging securely. Data being logged should pass white listing before logging.
 * Encoding the log using ESAPI HTMLEntityCodec.
 * 
 * @author Swetha Vemuri
 *
 */

public class SecurityUtils {
	
	private final static JustLogit logit = new JustLogit(SecurityUtils.class);
	protected static final String SECURE = "^[a-zA-Z0-9<>@/: &.=?$#_-]{1,1000}";
	private static final char immune[] = {};
	private static final HTMLEntityCodec encoder = new HTMLEntityCodec();

	private static String scanStringForSecureLogging(String inputString, String regExp) {			
					
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
			logit.error(()->"Input object validation failed while scanning for security using REGEX: ["+regExp+"] "+e);
			return encoder.encode(immune, String.valueOf(input));
		}		
	}
}
