/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertNotEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.antheminc.oss.nimbus.core.AbstractFrameworkIntegrationTests;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoIdSequenceRepositoryTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	IdSequenceRepository seqIdRepo;
	
	@Autowired
	MongoOperations mongoOps;
	
	@Test
	public void t1_testSequenceCollectionUpsert() {
		mongoOps.dropCollection("sequence");
		assertNotEquals(0, seqIdRepo.getNextSequenceId("global"));
		assertNotEquals(1, seqIdRepo.getNextSequenceId("global"));
	}
}
