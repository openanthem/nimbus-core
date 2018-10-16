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
/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
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
	
	@WritingConverter
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
	
	@ReadingConverter
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
