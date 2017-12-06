/**
 * 
 */
package com.antheminc.oss.nimbus.core.repo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;

import com.antheminc.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.core.entity.access.DefaultAccessEntity;

import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultAccessEntityTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	MongoOperations mongoOps;
	
	
	@Test
	public void test_createStaticAccesses() {
		DefaultAccessEntity staticAccessess = AccessEntityFactory.createPlatformAndSubTree1();
		mongoOps.save(staticAccessess, "defaultAccessEntity");
	}
	
}
