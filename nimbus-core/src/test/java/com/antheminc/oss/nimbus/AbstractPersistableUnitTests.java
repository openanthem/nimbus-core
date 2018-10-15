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
package com.antheminc.oss.nimbus;

import org.junit.After;
import org.junit.Before;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.antheminc.oss.nimbus.support.mongo.MongoConvertersBuilder;
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
        
        MongoCustomConversions customConversions = new MongoConvertersBuilder().addDefaults().build();
        
        MappingMongoConverter mongoConverter = new MappingMongoConverter(new DefaultDbRefResolver(factory), new MongoMappingContext());
        mongoConverter.setCustomConversions(customConversions);
        mongoConverter.afterPropertiesSet();
        
        mongo = new MongoTemplate(factory, mongoConverter);
    }
    
    @After
    public void after() throws Exception {
    	mongo.getDb().drop();
    }
}
