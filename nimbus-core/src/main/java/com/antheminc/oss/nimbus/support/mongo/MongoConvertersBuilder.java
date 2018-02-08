/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.CustomConversions;

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
	
	public CustomConversions build() {
		return new CustomConversions(converters);
	}
}
