/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Soham Chakravarti
 *
 */
public class UTCDateMongoConverters {
	
	public static class UTCDateSerializer implements Converter<Date, Date> {
		@Override
		public Date convert(Date inDefaultZone) {
			if(inDefaultZone==null)
				return null;
			
			ZoneId currDefaultZone = ZoneId.systemDefault();
			ZonedDateTime currZoneDT = ZonedDateTime.ofInstant(inDefaultZone.toInstant(), currDefaultZone);
			
			// convert to UTC
			ZonedDateTime zUTC = currZoneDT.withZoneSameInstant(ZoneOffset.UTC);
			
			return Date.from(zUTC.toInstant());
		}
	}
	
	public static class UTCDateDeserializer implements Converter<Date, Date> {
		@Override
		public Date convert(Date inUTC) {
			if(inUTC==null)
				return null;
			
			ZonedDateTime zUTC = ZonedDateTime.ofInstant(inUTC.toInstant(), ZoneOffset.UTC);
			ZoneId currDefaultZone = ZoneId.systemDefault();
			
			// convert to current default system time zone
			ZonedDateTime currZoneDT = zUTC.withZoneSameInstant(currDefaultZone);
			return Date.from(currZoneDT.toInstant());
		}
	}

}
