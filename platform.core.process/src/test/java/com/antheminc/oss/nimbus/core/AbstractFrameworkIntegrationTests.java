/**
 * 
 */
package com.antheminc.oss.nimbus.core;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.antheminc.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.antheminc.oss.nimbus.core.entity.client.Client;
import com.antheminc.oss.nimbus.core.integration.websocket.ParamEventAMQPListener;
import com.antheminc.oss.nimbus.core.web.WebActionController;

/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestFrameworkIntegrationScenariosApplication.class)
@ActiveProfiles("test")
public abstract class AbstractFrameworkIntegrationTests {

	@Autowired protected WebActionController controller;
	
	@Autowired protected MongoOperations mongo;
	
	@Autowired protected MongoTemplate mt;
	
	@Autowired protected CommandMessageConverter converter;
	
	protected static final String CLIENT_ID = "hooli";
	
	protected static final String PLATFORM_ROOT = "/"+CLIENT_ID+"/thebox/p"; 
	
	@MockBean protected ParamEventAMQPListener mockParamEventListener;
	
	@Before
	public void t0_init() {
		this.tearDown();
		Client newClient = new Client();
		newClient.setId(CLIENT_ID);
		mongo.insert(newClient, "cliententity");
		
		assertNotNull(mongo.findById(CLIENT_ID, Client.class, "cliententity"));
	}
	
	/**
	 * Drop the test db "integrationtest" 
	 */
	@After
	public void tearDown() {
		mt.getDb().dropDatabase();
	}
}