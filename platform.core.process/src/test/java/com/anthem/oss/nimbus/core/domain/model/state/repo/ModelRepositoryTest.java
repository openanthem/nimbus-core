/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractTestConfigurer;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.entity.client.ExClient;

/**
 * @author Rakesh Patel
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelRepositoryTest extends AbstractTestConfigurer {

	
	@Autowired ModelRepositoryFactory repoFactory;
	
	@Autowired QuadModelBuilder quadBuilder;
	
	
	@Test
	public void t1_testSearchByExample_Ext() {
		Command cmd = prepareCommand("Anthem/fep/p/ex_client/_search?fn=example",null);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
		
		ExClient exClient = (ExClient) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		
		assertNotNull("ExClient.client cannot ne null", exClient.getClient());
		
		assertNotNull("ExClient.client.code cannot ne null", exClient.getClient().getCode());
		
	}
}
