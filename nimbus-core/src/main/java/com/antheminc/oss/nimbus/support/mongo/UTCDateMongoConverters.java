/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Soham Chakravarti
 *
 */
public class UTCDateMongoConverters {
	
	/* Date */
	public static class UTCDateSerializer implements Converter<Date, Date> {
		@Override
		public Date convert(Date inCurrTimeZone) {
			return serializerTemplate(inCurrTimeZone, currTimeZone->ZonedDateTime.ofInstant(inCurrTimeZone.toInstant(), currTimeZone));
		}
	}
	
	public static class UTCDateDeserializer implements Converter<Date, Date> {
		@Override
		public Date convert(Date inUTC) {
			return deserializerTemplate(inUTC, inCurrTimeZone->Date.from(inCurrTimeZone.toInstant()));
		}
	}
	
	/* LocalDateTime */
	public static class UTCLocalDateTimeSerializer implements Converter<LocalDateTime, Date> {
		@Override
		public Date convert(LocalDateTime inCurrTimeZone) {
			return serializerTemplate(inCurrTimeZone, currTimeZone->ZonedDateTime.of(inCurrTimeZone, currTimeZone));
		}
	}
	
	public static class UTCLocalDateTimeDeserializer implements Converter<Date, LocalDateTime> {
		@Override
		public LocalDateTime convert(Date inUTC) {
			return deserializerTemplate(inUTC, ZonedDateTime::toLocalDateTime);
		}
	}
	
	/* LocalDate */
	public static class UTCLocalDateSerializer implements Converter<LocalDate, Date> {
		@Override
		public Date convert(LocalDate inCurrTimeZone) {
			return serializerTemplate(inCurrTimeZone, currTimeZone->ZonedDateTime.of(inCurrTimeZone, LocalTime.MIN, currTimeZone));
		}
	}
	
	public static class UTCLocalDateDeserializer implements Converter<Date, LocalDate> {
		@Override
		public LocalDate convert(Date inUTC) {
			return deserializerTemplate(inUTC, ZonedDateTime::toLocalDate);
		}
	}
	
	
	public static <T> Date serializerTemplate(T inCurrTimeZone, Function<ZoneId, ZonedDateTime> cb) {
		if(inCurrTimeZone==null)
			return null;
		
		ZoneId currDefaultZone = ZoneId.systemDefault();
		//ZonedDateTime currZoneDT = ZonedDateTime.ofInstant(inDefaultZone.toInstant(), currDefaultZone);
		ZonedDateTime currZoneDT = cb.apply(currDefaultZone);
		
		// convert to UTC
		ZonedDateTime zUTC = currZoneDT.withZoneSameInstant(ZoneOffset.UTC);
		
		return Date.from(zUTC.toInstant());
	}
	
	public static <T> T deserializerTemplate(Date inUTC, Function<ZonedDateTime, T> cb) {
		if(inUTC==null)
			return null;
		
		ZonedDateTime zUTC = ZonedDateTime.ofInstant(inUTC.toInstant(), ZoneOffset.UTC);
		ZoneId currDefaultZone = ZoneId.systemDefault();
		
		// convert to current default system time zone
		ZonedDateTime inCurrTimeZone = zUTC.withZoneSameInstant(currDefaultZone);
		return cb.apply(inCurrTimeZone);
	}

}
