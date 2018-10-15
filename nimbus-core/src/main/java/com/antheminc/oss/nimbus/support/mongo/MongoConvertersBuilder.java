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

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * @author Soham Chakravarti
 *
 */
public final class MongoConvertersBuilder {

	private List<Converter<?, ?>> converters = new ArrayList<>();
	
	public MongoConvertersBuilder add(Converter<?, ?> c) {
		converters.add(c);
		return this;
	}
	
	public MongoConvertersBuilder addDefaults() {
		return
				
        // ZDT Converters
         add(new ZonedDateTimeMongoConverters.ZDTSerializer())
        .add(new ZonedDateTimeMongoConverters.ZDTDeserializer())
        
        // UTC Date Converters
        .add(new UTCDateMongoConverters.UTCDateSerializer())
        .add(new UTCDateMongoConverters.UTCDateDeserializer())
        
        // UTC LocalDateTime Converters
        .add(new UTCDateMongoConverters.UTCLocalDateTimeSerializer())
        .add(new UTCDateMongoConverters.UTCLocalDateTimeDeserializer())
        
        // UTC LocalDate Converters
        .add(new UTCDateMongoConverters.UTCLocalDateSerializer())
        .add(new UTCDateMongoConverters.UTCLocalDateDeserializer());
	}
	
	public MongoCustomConversions build() {
		return new MongoCustomConversions(converters);
	}
}
