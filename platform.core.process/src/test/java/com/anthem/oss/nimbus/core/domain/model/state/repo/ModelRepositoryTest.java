package com.anthem.oss.nimbus.core.domain.model.state.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.entity.client.ExtClient;

import test.com.anthem.oss.nimbus.core.testutils.CommandUtils;

/**
 * @author Rakesh Patel
 */
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelRepositoryTest extends AbstractFrameworkIntegrationTests {

	@Autowired QuadModelBuilder quadBuilder;
	
	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	private MockRestServiceServer mockServer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setup() {
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
	}

    protected List<Output<?>> getMultiExecuteOutput(Command command, String rawPayload){
        CommandMessage cmdMsg = new CommandMessage();
        cmdMsg.setCommand(command);
        cmdMsg.setRawPayload(rawPayload);
        MultiOutput resp = commandGateway.execute(cmdMsg);
        List<Output<?>> execop = resp.getOutputs();
        return execop;
    }
	
	@Test
	public void t1_testSearchByExample_Ext() {
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client/_search?fn=example";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		String jsonPayload = "{\"client\": {\"code\":\"example\"}}";
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		cmdMsg.setRawPayload(jsonPayload);
		
		this.mockServer.expect(requestTo(new StringContains(requestUri)))
			.andRespond(withSuccess("[" + jsonPayload + "]", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		@SuppressWarnings("unchecked")
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
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client/_search?fn=query&where=ext_client.client.code.eq('7')";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		final String jsonPayload = "{\"client\": {\"code\":\"example\"}}";
		this.mockServer.expect(requestTo(new StringContains(requestUri)))
			.andRespond(withSuccess("[" + jsonPayload + "]", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		@SuppressWarnings("unchecked")
		List<ExtClient> exClient = (List<ExtClient>) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		assertNotNull("ExClient.client cannot ne null", exClient.get(0).getClient());
		assertNotNull("ExClient.client.code cannot ne null", exClient.get(0).getClient().getCode());
		
	}
	
	@Test
	public void t3_testGet_Ext() {
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client:7/_get";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.mockServer.expect(requestTo(new StringContains(requestUri)))
			.andRespond(withSuccess("{\"client\": {\"code\":\"example23\"}}", MediaType.APPLICATION_JSON));

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>)multiOp.getSingleResult();
		
		assertNotNull("Param (ExtClient) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (ExtClient.client.code) cannot ne null", param.findParamByPath("/client/code"));
		assertNotNull("ParamState (ExtClient.Client.code) cannot be null", param.findParamByPath("/client/code").getState());
		

		assertEquals("example23", param.findParamByPath("/client/code").getState());
		
		//QuadModel<?, ?> q = quadBuilder.build(cmd);
		//q.getCore().findParamByPath("/client").setState(param.findParamByPath("/client").getState());
		//assertEquals("7", q.getCore().findParamByPath("/client/code").getState());
		
	}
	
	// TODO - Set client object in db.
	@Ignore
	@Test
	public void t4_testGet() {
		final String requestUri = "piedpiper/encryption_3.9/p/client:7/_get";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.mockServer.expect(requestTo(new StringContains(requestUri)))
			.andRespond(withSuccess("{\"client\": {\"code\":\"example23\"}}", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>) multiOp.getSingleResult();
		
		assertNotNull("Param (Client) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (client.code) cannot ne null", param.findParamByPath("/code"));
		assertNotNull("ParamState (Client.code) cannot be null", param.findParamByPath("/code").getState());
		
		
	}
}
