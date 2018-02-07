/**
 * 
 */
package com.antheminc.oss.nimbus;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.antheminc.oss.nimbus.support.mongo.UTCDateMongoConverters;
import com.antheminc.oss.nimbus.support.mongo.ZonedDateTimeMongoConverters;
import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractPersistableUnitTests {
	
    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embeded_db";

    protected MongoTemplate mongo;
    
    @Before
    public void before() throws Exception {
    	EmbeddedMongoFactoryBean mongoBean = new EmbeddedMongoFactoryBean();
        mongoBean.setBindIp(MONGO_DB_URL);
        
        MongoClient mongoClient = mongoBean.getObject();
        MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, MONGO_DB_NAME);
        
        List<Converter<?, ?>> converters = new ArrayList<>();
        
        // ZDT Converters
        converters.add(new ZonedDateTimeMongoConverters.ZDTSerializer());
        converters.add(new ZonedDateTimeMongoConverters.ZDTDeserializer());
        
        // UTC Date Converters
        converters.add(new UTCDateMongoConverters.UTCDateSerializer());
        converters.add(new UTCDateMongoConverters.UTCDateDeserializer());
        
        CustomConversions customConversions = new CustomConversions(converters);
        
        MappingMongoConverter mongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        mongoConverter.setCustomConversions(customConversions);
        mongoConverter.afterPropertiesSet();
        
        mongo = new MongoTemplate(factory, mongoConverter);
    }
    
    @After
    public void after() throws Exception {
    	mongo.getDb().dropDatabase();
    }
}
