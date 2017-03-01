package com.anthem.nimbus.platform.core.process.api;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.entity.DBSequence;

/**
 * This class provides the base configuration to run Integration test cases.
 * It includes the configuration for EmbeddedMongo and Embedded Redis and a default profile of test.
 * The setup includes creating a collection - 'sequence' with global id starting from 0.
 * All the integration tests can extend from this base class and can override the profile.
 * Other profiles include "build" - which will connect to a remote host for mongoDb, Redis and RabbitMq.
 * 
 * @author Swetha Vemuri
 *
 */

@RunWith(SpringRunner.class)
@ComponentScan(basePackages={"com.anthem.oss.nimbus.core"})
@SpringBootTest(classes= {BPMEngineConfig.class/*, RedisIntegrationTestConfig.class, MongoIntegrationTestConfig.class*/})
@ActiveProfiles("test")
@EnableAutoConfiguration
public class AbstractPlatformIntegrationTests {
	
	@Autowired MongoOperations mongoOps;
	
	@Before
	public void setup(){
		mongoOps.dropCollection("sequence");
		
		DBSequence sequence = new DBSequence();
		sequence.setId("global");
    	sequence.setSeq(0);
		mongoOps.insert(sequence,"sequence");
	}
}
