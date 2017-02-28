/**
 * 
 */
package com.anthem.nimbus.platform.core.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;

import com.anthem.nimbus.platform.core.process.api.AbstractPlatformIntegrationTests;
import com.anthem.oss.nimbus.test.sample.um.model.Patient;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;

/**
 * @author Swetha Vemuri
 *
 */
@EnableAutoConfiguration
public class EmbedMongoConnectionTest extends AbstractPlatformIntegrationTests {
	
	@Autowired
	MongoOperations mongoOps;
	
	/*
	 * This test case is written to verify the connection of embedded mongodb.
	 */
	@Test
	public void tc01_createTestData() {
		UMCase umc = new UMCase();
		umc.setCaseType("medical");
		umc.setRequestType("outpatient");
		Patient p = new Patient();
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setSubscriberId("YZW12345");
		umc.setPatient(p);
		
		mongoOps.insert(umc, "test_umcase");
		assertTrue(mongoOps.collectionExists("test_umcase"));
		assertNotNull(mongoOps.findAll(UMCase.class, "test_umcase"));
		
		List<UMCase> dbList = mongoOps.findAll(UMCase.class, "test_umcase");
		assertNotNull(dbList.get(0));
		assertNotNull(dbList.get(0).getId());
	}
	
	/*
	 * This test case is written to verify the session of the embedded mongodb runs for multiple tests in the suite
	 */
	@Test
	public void tc02_createTestData() {
		UMCase umc = new UMCase();
		umc.setCaseType("surgical");
		umc.setRequestType("inpatient");
		Patient p = new Patient();
		p.setFirstName("John");
		p.setLastName("Doe");
		p.setSubscriberId("YZW12345");
		umc.setPatient(p);
		
		mongoOps.insert(umc, "test_umcase_ip");
		assertTrue(mongoOps.collectionExists("test_umcase_ip"));
		assertNotNull(mongoOps.findAll(UMCase.class, "test_umcase_ip"));
		
		List<UMCase> dbList = mongoOps.findAll(UMCase.class, "test_umcase_ip");
		assertNotNull(dbList.get(0));
		assertNotNull(dbList.get(0).getId());
	}

}
