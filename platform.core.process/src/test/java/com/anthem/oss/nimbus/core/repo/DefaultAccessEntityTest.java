/**
 * 
 */
package com.anthem.oss.nimbus.core.repo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractTestConfigurer;
import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;

import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;

/**
 * @author Soham Chakravarti
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultAccessEntityTest extends AbstractTestConfigurer {

	@Autowired
	MongoOperations mongoOps;
	
	
	@Test
	public void test_createStaticAccesses() {
		DefaultAccessEntity staticAccessess = AccessEntityFactory.createPlatformAndSubTree1();
		
		mongoOps.save(staticAccessess, "defaultAccessEntity");
		
	}
	
}
