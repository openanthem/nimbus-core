/**
 * 
 */
package com.anthem.oss.nimbus.core.repo;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.AccessEntityRepository;
import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;

import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Import(BPMEngineConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Profile("test")
public class AccessEntityRepositoryTest {

	@Autowired AccessEntityRepository rep;
	
	@Test
	public void test_createStaticAccesses() {
		DefaultAccessEntity staticAccessess = AccessEntityFactory.createPlatformAndSubTree1();
		
		rep.save(staticAccessess); //TODO save was showing compile error, commented temporary to get the code running
		
	}
	
}
