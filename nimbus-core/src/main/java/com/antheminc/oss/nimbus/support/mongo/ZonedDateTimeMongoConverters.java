/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author Soham Chakravarti
 *
 */
public class ZonedDateTimeMongoConverters {

	public static final String K_DATE = "_date";
	public static final String K_ZONE = "_zone";
	
	public static class ZDTSerializer implements Converter<ZonedDateTime, DBObject> {
		@Override
		public DBObject convert(ZonedDateTime z) {
			if(z==null)
				return null;
			
			ZonedDateTime zUTC = z.withZoneSameInstant(ZoneOffset.UTC);
			
			DBObject dbObj = new BasicDBObject();
			
			dbObj.put(K_DATE, Date.from(zUTC.toInstant()));
			dbObj.put(K_ZONE, z.getZone().getId());
			return dbObj;
		}
	}
	
	public static class ZDTDeserializer implements Converter<DBObject, ZonedDateTime> {
		@Override
		public ZonedDateTime convert(DBObject dbObj) {
			if(dbObj==null)
				return null;
			
			Date date = (Date)dbObj.get(K_DATE);
			String zone = (String)dbObj.get(K_ZONE);
			
			Assert.notNull(date, "Persisted entity for "+ZonedDateTime.class.getSimpleName()+" must not have null value for "+K_DATE);
			Assert.notNull(zone, "Persisted entity for "+ZonedDateTime.class.getSimpleName()+" must not have null value for "+K_ZONE);
			
			ZonedDateTime zUTC = ZonedDateTime.ofInstant(date.toInstant(), ZoneOffset.UTC);
			return zUTC.withZoneSameInstant(ZoneId.of(zone));
		}
	}

}
