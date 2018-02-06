/**
 * 
 */
package com.antheminc.oss.nimbus.support.mongo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.antheminc.oss.nimbus.entity.AbstractEntity.IdString;
import com.mongodb.MongoClient;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
public class ZonedDateTimeConverterTest {

	private static final ZoneId K_SAVE_JVM_ZONE_ID = ZoneId.of("Asia/Kolkata");
	
	private static final ZonedDateTime K_ZDT = ZonedDateTime.of(2018, 2, 6, 6, 52, 0, 0, ZoneId.of("US/Pacific-New"));
	//private static final ZonedDateTime K_UTC = ZonedDateTime.of(2018, 2, 6, 14, 52, 0, 0, ZoneId.of("UTC"));
	
	
    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embeded_db";
    
    private MongoOperations mongo;
    
	@SuppressWarnings("serial")
	@Getter @Setter
    private static class _TestEntityWithZDT extends IdString {
		
    	private ZonedDateTime userEnteredZonedDT;
    }
    
    @Before
    public void before() throws Exception {
    	EmbeddedMongoFactoryBean mongoBean = new EmbeddedMongoFactoryBean();
        mongoBean.setBindIp(MONGO_DB_URL);
        
        MongoClient mongoClient = mongoBean.getObject();
        MongoDbFactory factory = new SimpleMongoDbFactory(mongoClient, MONGO_DB_NAME);
        
        List<Converter<?, ?>> converters = new ArrayList<>();
        
        converters.add(new ZonedDateTimeMongoConverters.ZDTSerializer());
        converters.add(new ZonedDateTimeMongoConverters.ZDTDeserializer());
        CustomConversions customConversions = new CustomConversions(converters);
        
        MappingMongoConverter mongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        mongoConverter.setCustomConversions(customConversions);
        mongoConverter.afterPropertiesSet();
        
        mongo = new MongoTemplate(factory, mongoConverter);
        
    }
    
    @BeforeClass
    public static void beforeClass() throws Exception {
    	System.setProperty("user.timezone", K_SAVE_JVM_ZONE_ID.getId());
    }
	
	@Test
	public void t1_fromZ() {
		// set save zone
		assertEquals(K_SAVE_JVM_ZONE_ID, ZoneId.systemDefault());
		
		// create entity to save
		_TestEntityWithZDT entity_dbSave = new _TestEntityWithZDT();
		entity_dbSave.setUserEnteredZonedDT(K_ZDT);
		
		// save to DB in zone 1
		mongo.save(entity_dbSave);
		System.out.println("Saved in DB with ID: "+entity_dbSave.getId());
		
		//& read back in another JVM with different zone
		//System.setProperty("user.timezone", K_READ_JVM_ZONE_ID.getId());
		//assertEquals(K_READ_JVM_ZONE_ID, ZoneId.systemDefault());
		
		_TestEntityWithZDT entity_dbRead = mongo.findById(entity_dbSave.getId(), _TestEntityWithZDT.class);
		
		assertEquals(K_ZDT, entity_dbRead.getUserEnteredZonedDT());
	}
	
	@Test
	public void t2_null() {
		_TestEntityWithZDT entity_dbSave = new _TestEntityWithZDT();
		entity_dbSave.setUserEnteredZonedDT(null);
		
		// save to DB in zone 1
		mongo.save(entity_dbSave);
		System.out.println("Saved in DB with ID: "+entity_dbSave.getId());
		
		_TestEntityWithZDT entity_dbRead = mongo.findById(entity_dbSave.getId(), _TestEntityWithZDT.class);
		
		assertNull(entity_dbRead.getUserEnteredZonedDT());
	}
	
}
