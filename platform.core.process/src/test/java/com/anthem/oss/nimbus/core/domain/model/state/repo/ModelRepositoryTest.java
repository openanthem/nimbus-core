package com.anthem.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.hamcrest.core.StringContains;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.anthem.oss.nimbus.core.AbstractTestConfigurer;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.entity.client.ExtClient;

/**
 * @author Rakesh Patel
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelRepositoryTest extends AbstractTestConfigurer {

	@Autowired QuadModelBuilder quadBuilder;
	
	
	@Test
	public void t1_testSearchByExample_Ext() {
		Command cmd = prepareCommand("piedpiper/encryption_3.9/p/ext_client/_search?fn=example",null);
		String jsonPayload = "{\"client\": {\"code\":\"example\"}}";
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		cmdMsg.setRawPayload(jsonPayload);
		
		MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
		
		List<ExtClient> exClient = (List<ExtClient>) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		assertNotNull("ExClient.client cannot ne null", exClient.get(0).getClient());
		assertNotNull("ExClient.client.code cannot ne null", exClient.get(0).getClient().getCode());
		
//		QuadModel<?, ?> q = quadBuilder.build(cmd);
//		q.getCore().findParamByPath("/client").setState(exClient.getClient());
//		assertEquals("example", q.getCore().findParamByPath("/client/code").getState());
		
	}
	
	@Test
	public void t2_testSearchByQuery_Ext() {
		Command cmd = prepareCommand("piedpiper/encryption_3.9/p/ext_client/_search?fn=query&where=ext_client.client.code.eq('7')",null);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
		
		List<ExtClient> exClient = (List<ExtClient>) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		assertNotNull("ExClient.client cannot ne null", exClient.get(0).getClient());
		assertNotNull("ExClient.client.code cannot ne null", exClient.get(0).getClient().getCode());
		
	}
	
	@Test
	public void t3_testGet_Ext() {
		Command cmd = prepareCommand("piedpiper/encryption_3.9/p/ext_client:7/_get",null);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.getMockServer().expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/ext_client:7/_get")))
		.andRespond(withSuccess("{\"client\": {\"code\":\"example23\"}}", MediaType.APPLICATION_JSON));

		MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
		
		Param<?> param = (Param<?>)multiOp.getSingleResult();
		
		assertNotNull("Param (ExtClient) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (ExtClient.client.code) cannot ne null", param.findParamByPath("/client/code"));
		assertNotNull("ParamState (ExtClient.Client.code) cannot be null", param.findParamByPath("/client/code").getState());
		

		assertEquals("example23", param.findParamByPath("/client/code").getState());
		
		//QuadModel<?, ?> q = quadBuilder.build(cmd);
		//q.getCore().findParamByPath("/client").setState(param.findParamByPath("/client").getState());
		//assertEquals("7", q.getCore().findParamByPath("/client/code").getState());
		
	}
	
	@Test
	public void t4_testGet() {
		Command cmd = prepareCommand("piedpiper/encryption_3.9/p/client:7/_get",null);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = getCommandGateway().execute(cmdMsg);
		
		Param<?> param = (Param<?>)multiOp.getSingleResult();
		
		assertNotNull("Param (Client) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (client.code) cannot ne null", param.findParamByPath("/code"));
		assertNotNull("ParamState (Client.code) cannot be null", param.findParamByPath("/code").getState());
		
		
	}
}
