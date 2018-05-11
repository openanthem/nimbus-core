/**
 * 
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
