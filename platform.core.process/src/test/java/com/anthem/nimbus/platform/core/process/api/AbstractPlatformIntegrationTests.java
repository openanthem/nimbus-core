package com.anthem.nimbus.platform.core.process.api;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.entity.DBSequence;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * This class provides the base configuration to run Integration test cases.
 * It includes the configuration for EmbeddedMongo.
 * The setup includes creating a collection - 'sequence' with global id starting from 0.
 * All the integration tests can extend from this base class and can override the profile.
 * Other profiles include "build" - which will connect to a remote host for Redis and RabbitMq.
 * When running test cases, an environment variable  - spring.profile.active needs to be provided.
 * 
 * @author Swetha Vemuri
 *
 */

@RunWith(SpringRunner.class)
@ComponentScan(basePackages={"com.anthem.oss.nimbus.core"})
@SpringBootTest(classes= {BPMEngineConfig.class/*,MongoIntegrationTestConfig.class*/})
@EnableAutoConfiguration
@Profile({"test","build"})
public class AbstractPlatformIntegrationTests {
	
	@Autowired MongoOperations mongoOps;
	
	@Before
	public void setup() throws Exception{

		mongoOps.dropCollection("sequence");
		
		DBSequence sequence = new DBSequence();
		sequence.setId("global");
    	sequence.setSeq(0);
		mongoOps.insert(sequence,"sequence");
		
		
	}
	
	@After
	public void tearDown() {
		UserEndpointSession.clearSession();
	}  
	
	@Test
	public void tc_01_sanityCheck() {
		assertNotNull(mongoOps.getCollectionNames());
		assertTrue(mongoOps.getCollectionNames().contains("sequence"));
	}
}
