/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractUnitTest;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.entity.client.Client;

/**
 * @author Rakesh Patel
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorDeleteTest extends AbstractUnitTest {
	
	@Autowired
	MongoOperations mongoOps;
	
	
	@Test
	public void t0_deleteParam() {
		
		Client c = insertClient();
		
		Command cmd = CommandBuilder.withUri("Anthem/fep/icr/p/client:"+c.getId()+"/_delete").getCommand();
		cmd.setAction(Action._delete);
		
		MultiOutput multiOp = getCommandGateway().execute(cmd, null);
		Boolean b = (Boolean)multiOp.getSingleResult();
		
		assertTrue(b);
	}
	
	private Client insertClient() {
		
		Client c = new Client();
		c.setName("client1");
		c.setCode("c1");
		mongoOps.insert(c, "client");
		return c;
	}

}
