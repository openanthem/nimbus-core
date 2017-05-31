/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractUnitTest;

/**
 * @author Rakesh Patel
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoIdSequenceRepositoryTest extends AbstractUnitTest {

	@Autowired
	IdSequenceRepository seqIdRepo;
	
	@Autowired
	MongoOperations mongoOps;
	
	@Test
	public void t1_testSequenceCollectionUpsert() {
		
		mongoOps.dropCollection("sequence");
		
		long seqId = seqIdRepo.getNextSequenceId("global");
		
		assertNotEquals(0, seqId);
	}
	
	@Test
	public void t2_testSequenceIdIncreament() {
		
		long seqId = seqIdRepo.getNextSequenceId("global");
		
		assertEquals(2, seqId);
	}
}
