/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.repo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.BehaviorExecute;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.CmdExecuteOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.CmdExecuteOutput.HolderValue;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteOutput.GenericExecute;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.client.ExtClient;
import com.antheminc.oss.nimbus.support.CommandUtils;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.scenarios.repo.remote.core.SampleRemoteRepo;
import com.antheminc.oss.nimbus.test.scenarios.repo.remote.core.SampleRemoteRepo.SampleRepoNested;
import com.antheminc.oss.nimbus.test.scenarios.repo.remote.core.SampleRemoteRepo2;
import com.antheminc.oss.nimbus.test.scenarios.repo.remote.core.SampleRemoteRepo2.SampleRepoNested2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Rakesh Patel
 * @author Swetha Vemuri
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ModelRepositoryTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	@Qualifier("default.processGateway")
	CommandExecutorGateway commandGateway;
	
	private MockRestServiceServer mockServerRemoteWs;
	private MockRestServiceServer mockServerDefaultWs;
	
	@Autowired
	@Qualifier("default.rep_remote_ws.restTemplate")
	private RestTemplate remoteWSrestTemplate;
	
	@Autowired
	@Qualifier("default.rep_ws.restTemplate")
	private RestTemplate wsRestTemplate;
	
	@Before
	public void setup() {
		this.mockServerRemoteWs = MockRestServiceServer.createServer(remoteWSrestTemplate);
		this.mockServerDefaultWs = MockRestServiceServer.createServer(wsRestTemplate);
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
		final String jsonPayload = "{\"client\": {\"code\":\"example\"}}";
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		cmdMsg.setRawPayload(jsonPayload);
		

		this.mockServerDefaultWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","example"))
		.andRespond(withSuccess("[{\"client\": {\"code\": \"example\"}},{\"client\": {\"code\": \"example\" }}]", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		@SuppressWarnings("unchecked")
		List<ExtClient> exClient = (List<ExtClient>) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		
		for(int i=0; i< exClient.size(); i++) {
			assertNotNull("ExClient.client cannot ne null", exClient.get(i).getClient());
			assertNotNull("ExClient.client.code cannot ne null", exClient.get(i).getClient().getCode());
			assertEquals("example",exClient.get(i).getClient().getCode());
		}
	
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
		

		this.mockServerDefaultWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","query"))
		.andExpect(queryParam("where","ext_client.client.code.eq('7')"))
		.andRespond(withSuccess("[{\"client\": {\"code\": \"test ext return - using querydsl\" }}]", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		@SuppressWarnings("unchecked")
		List<ExtClient> exClient = (List<ExtClient>) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		assertNotNull("ExClient.client cannot ne null", exClient.get(0).getClient());
		assertNotNull("ExClient.client.code cannot ne null", exClient.get(0).getClient().getCode());
		assertEquals("test ext return - using querydsl",exClient.get(0).getClient().getCode());
		
	}
	
	@Ignore //TODO in DefaultWS
	public void t2_testSearchByQuery_fetch1_Ext() {
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client/_search?fn=query&where=ext_client.client.code.eq('7')&fetch=1";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		

		this.mockServerDefaultWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","query"))
		.andExpect(queryParam("where","ext_client.client.code.eq('7')"))
		.andExpect(queryParam("fetch","1"))
		.andRespond(withSuccess("{\"client\": {\"code\": \"test ext return - using querydsl\" }}", MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		ExtClient exClient = (ExtClient) multiOp.getSingleResult();
		
		assertNotNull("ExClient cannot be null", exClient);
		assertNotNull("ExClient code cannot be null", exClient.getClient().getCode());
	}
	
	@Test
	public void t3_testGet_Ext() {
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client/_get";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.mockServerDefaultWs.expect(requestTo("http://localhost:9095"))
			.andRespond(withSuccess("{\"client\": {\"code\":\"example23\"}}", MediaType.APPLICATION_JSON));

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>)multiOp.getSingleResult();
		
		assertNotNull("Param (ExtClient) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (ExtClient.client.code) cannot ne null", param.findParamByPath("/client/code"));
		assertNotNull("ParamState (ExtClient.Client.code) cannot be null", param.findParamByPath("/client/code").getState());
		

		assertEquals("example23", param.findParamByPath("/client/code").getState());
	}
	
	@Test
	public void testExternalGetWithRefId() {
		final String requestUri = "piedpiper/encryption_3.9/p/ext_client:42/_get";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.mockServerDefaultWs.expect(requestTo("http://localhost:9095"))
			.andRespond(withSuccess("{\"client\": {\"code\":\"example23\"}}", MediaType.APPLICATION_JSON));

		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>)multiOp.getSingleResult();
		
		assertNotNull("Param (ExtClient) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (ExtClient.client.code) cannot ne null", param.findParamByPath("/client/code"));
		assertNotNull("ParamState (ExtClient.Client.code) cannot be null", param.findParamByPath("/client/code").getState());
		

		assertEquals("example23", param.findParamByPath("/client/code").getState());
	}

	@Test
	public void t5_get_remote() throws JsonProcessingException {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo:12/_get";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		String jsonresp  = mockSingleGenericExecuteResponse_GetNewSearch();
		this.mockServerRemoteWs.expect(requestTo(new StringContains(requestUri)))
			.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>) multiOp.getSingleResult();
		
		assertNotNull("Param (SampleRemoteRepo) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (attr1) cannot ne null", param.findParamByPath("/attr1"));
		assertNotNull("ParamState (attr1) cannot be null", param.findParamByPath("/attr1").getState());
		assertNotNull("Param (attr2) cannot ne null", param.findParamByPath("/attr2"));
		assertNotNull("ParamState (attr2) cannot be null", param.findParamByPath("/attr2").getState());
		
		this.mockServerRemoteWs.reset();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void t6_testSearchByQuery_remote() throws JsonProcessingException {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo/_search?fn=query&where=remote_repo.attr1.eq('example1')";
		Command cmd = CommandUtils.prepareCommand(requestUri);
//		Map<String, String[]> requestParams = new HashMap<>();
//		requestParams.put("fn", new String[]{"query"});
//		requestParams.put("where", new String[]{"remote_repo.attr1.eq('example1')"});
//		cmd.setRequestParams(requestParams);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		String jsonresp  = mockGenericExecuteResponse_Search();
		this.mockServerRemoteWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","query"))
		.andExpect(queryParam("where","remote_repo.attr1.eq('example1')"))
		.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		List<SampleRemoteRepo> sample_remote = (List<SampleRemoteRepo>) multiOp.getSingleResult();
		
		assertNotNull("SampleRemote cannot be null", sample_remote);
		assertEquals(2, sample_remote.size());
		assertNotNull("SampleRemote.attr1 cannot ne null", sample_remote.get(0).getAttr1());
		assertEquals("example1", sample_remote.get(0).getAttr1());
		assertNotNull(sample_remote.get(0).getAttr2());
		assertEquals(2, sample_remote.get(0).getAttr2().size());
		assertEquals("nested1", sample_remote.get(0).getAttr2().get(0).getNested_attr());
	}
	
	@Test
	public void t7_testSearchByQuery_fetch1_remote() {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo/_search?fn=query&where=remote_repo.attr1.eq('example1')&fetch=1";
		Command cmd = CommandUtils.prepareCommand(requestUri);
//		Map<String, String[]> requestParams = new HashMap<>();
//		requestParams.put("fn", new String[]{"query"});
//		requestParams.put("where", new String[]{"remote_repo.attr1.eq('example1')"});
//		requestParams.put("fetch", new String[]{"1"});
//		cmd.setRequestParams(requestParams);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		String resp = mockSingleGenericExecuteResponse_GetNewSearch();

		this.mockServerRemoteWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","query"))
		.andExpect(queryParam("where","remote_repo.attr1.eq('example1')"))
		.andExpect(queryParam("fetch","1"))
		.andRespond(withSuccess(resp, MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		SampleRemoteRepo sample_remote = (SampleRemoteRepo) multiOp.getSingleResult();
		
		assertNotNull("SampleRemote cannot be null", sample_remote);
		assertNotNull("SampleRemote.attr1 cannot ne null", sample_remote.getAttr1());
		assertNotNull(sample_remote.getAttr2());
		assertEquals(2, sample_remote.getAttr2().size());
		assertEquals("nested1", sample_remote.getAttr2().get(0).getNested_attr());
	}
	
	@Test
	public void t8_testSearchByExample_remote() {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo/_search?fn=example";
		Command cmd = CommandUtils.prepareCommand(requestUri);
//		Map<String, String[]> requestParams = new HashMap<>();
//		requestParams.put("fn", new String[]{"example"});
//		cmd.setRequestParams(requestParams);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		String response = mockGenericExecuteResponse_Search();
		this.mockServerRemoteWs.expect(requestTo(new StringContains(requestUri)))
		.andExpect(method(HttpMethod.POST))
		.andExpect(queryParam("fn","example"))
		.andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		@SuppressWarnings("unchecked")
		List<SampleRemoteRepo> sample_remote = (List<SampleRemoteRepo>) multiOp.getSingleResult();
		
		assertNotNull("SampleRemote cannot be null", sample_remote);
		assertEquals(2, sample_remote.size());
		assertNotNull("SampleRemote.attr1 cannot ne null", sample_remote.get(0).getAttr1());
		assertEquals("example1", sample_remote.get(0).getAttr1());
		assertNotNull(sample_remote.get(0).getAttr2());
		assertEquals(2, sample_remote.get(0).getAttr2().size());
		assertEquals("nested1", sample_remote.get(0).getAttr2().get(0).getNested_attr());
	}
	
	@Test
	public void t9_new_remote() throws JsonProcessingException {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo/_new?b=$executeAnd$state";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		final String remoteRequestUri = "piedpiper/encryption_3.9/p/remote_repo/_new";
		String jsonresp  = mockSingleGenericExecuteResponse_GetNewSearch();
		this.mockServerRemoteWs.expect(requestTo(new StringContains(remoteRequestUri)))
			.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(),requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:1/_update")))
		.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
	
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>) multiOp.getSingleResult();
		
		assertNotNull("Param (SampleRemoteRepo) cannot be null", param.findParamByPath("/"));
		assertNotNull("Param (attr1) cannot ne null", param.findParamByPath("/attr1"));
		assertNotNull("ParamState (attr1) cannot be null", param.findParamByPath("/attr1").getState());
		assertNotNull("Param (attr2) cannot ne null", param.findParamByPath("/attr2"));
		assertNotNull("ParamState (attr2) cannot be null", param.findParamByPath("/attr2").getState());
		
		this.mockServerRemoteWs.reset();
	}
	
	@Test
	public void t10_delete_root_remote() throws JsonProcessingException {
		t5_get_remote();
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo:12/_delete";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		
		String jsonresp  = mockSingleGenericExecuteResponse_GetNewSearch();
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_get")))
			.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(), requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_update")))
		.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_delete")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Boolean param = (Boolean) multiOp.getSingleResult();
		assertNotNull(param);
		
		this.mockServerRemoteWs.reset();

	}
	
	
	@Test
	public void t12_delete_nested_remote() throws JsonProcessingException {
		final String requestUri = "piedpiper/encryption_3.9/p/remote_repo:12/attr2/0/_delete";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		
		String jsonresp  = mockSingleGenericExecuteResponse_GetNewSearch();
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_get")))
			.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(), requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_update")))
		.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:12/_update")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
		
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Boolean param = (Boolean) multiOp.getSingleResult();
		assertNotNull(param);
		
		this.mockServerRemoteWs.reset();

	}
	
	
	@Test
	public void t13_remote_withExecConfigs_mapped() throws JsonProcessingException {
		String requestUri = "piedpiper/encryption_3.9/p/vr_remote_repo/_new";
		Command cmd = CommandUtils.prepareCommand(requestUri);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		final String remoteRequestUri = "piedpiper/encryption_3.9/p/remote_repo/_new";
		String jsonresp  = mockSingleGenericExecuteResponse_GetNewSearch();
		this.mockServerRemoteWs.expect(requestTo(new StringContains(remoteRequestUri)))
			.andRespond(withSuccess(jsonresp, MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(),requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:1/_update")))
		.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
	
		MultiOutput multiOp = this.commandGateway.execute(cmdMsg);
		
		Param<?> param = (Param<?>) multiOp.getSingleResult();
		
		assertNotNull("Param (SampleRemoteRepo) cannot be null", param.findParamByPath("/.m"));
		assertNotNull("Param (attr1) cannot ne null", param.findParamByPath("/.m/attr1"));
		assertNotNull("ParamState (attr1) cannot be null", param.findParamByPath("/.m/attr1").getState());
		assertNotNull("Param (attr2) cannot ne null", param.findParamByPath("/.m/attr2"));
		assertNotNull("ParamState (attr2) cannot be null", param.findParamByPath("/.m/attr2").getState());
		
		this.mockServerRemoteWs.reset();
		
		requestUri = "piedpiper/encryption_3.9/p/vr_remote_repo:1/vr_attr2/_get";
		cmd = CommandUtils.prepareCommand(requestUri);
		cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(), requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo:1/_update")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/vr_remote_repo2/_new")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_GetNewSearch2(), MediaType.APPLICATION_JSON));

		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo2/_new")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_GetNewSearch2(), MediaType.APPLICATION_JSON));
	
		this.mockServerRemoteWs.expect(ExpectedCount.manyTimes(), requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo2:1/_update")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));

		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/vr_remote_repo2:1/_get")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_GetNewSearch2(), MediaType.APPLICATION_JSON));
		
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo2:1/_get")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_GetNewSearch2(), MediaType.APPLICATION_JSON));
	
		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/vr_remote_repo2:1/_delete")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));

		this.mockServerRemoteWs.expect(requestTo(new StringContains("piedpiper/encryption_3.9/p/remote_repo2:1/_delete")))
			.andRespond(withSuccess(mockSingleGenericExecuteResponse_update(), MediaType.APPLICATION_JSON));

		multiOp = this.commandGateway.execute(cmdMsg);
		
		Boolean b = (Boolean) multiOp.getSingleResult();
		assertNotNull(b);
	
	}
	
	private String mockSingleGenericExecuteResponse_update() {
		CmdExecuteOutput<Boolean> cmdExecOutput = new CmdExecuteOutput<>();		
		Map<Integer,ExecuteOutput.BehaviorExecute<CmdExecuteOutput<Boolean>>> outputMap = new HashMap<>();		
		ExecuteOutput.BehaviorExecute<CmdExecuteOutput<Boolean>> execOutput = new BehaviorExecute<CmdExecuteOutput<Boolean>>(Behavior.$execute,new CmdExecuteOutput<Boolean>());
		GenericExecute<Boolean> responseValue = new GenericExecute<Boolean>();
		
		List<HolderValue<Boolean>> outputs = new ArrayList<>();
		HolderValue<Boolean> holderValue = new HolderValue<>();
		holderValue.setValue(true);
		outputs.add(holderValue);
		
		cmdExecOutput.setOutputs(outputs);	
		execOutput.setResult(cmdExecOutput);
		outputMap.put(0, execOutput);		
		responseValue.setResult(outputMap);
		
		ObjectMapper om = new ObjectMapper();
		String jsonresp;
		try {
			jsonresp = om.writeValueAsString(responseValue);
			return jsonresp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String mockSingleGenericExecuteResponse_GetNewSearch() {
		SampleRemoteRepo remoterepo = new SampleRemoteRepo();
		remoterepo.setId(Long.valueOf("1"));
		remoterepo.setAttr1("example1");
		SampleRepoNested nested = new SampleRepoNested();
		nested.setNested_attr("nested1");
		
		List<SampleRepoNested> nested_list = new ArrayList<>();		
		nested_list.add(nested);
		nested_list.add(nested);
		remoterepo.setAttr2(nested_list);
		CmdExecuteOutput<SampleRemoteRepo> cmdExecOutput = new CmdExecuteOutput<>();		
		Map<Integer,ExecuteOutput.BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo>>> outputMap = new HashMap<>();		
		ExecuteOutput.BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo>> execOutput = new BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo>>(Behavior.$execute,new CmdExecuteOutput<SampleRemoteRepo>());
		GenericExecute<SampleRemoteRepo> responseValue = new GenericExecute<SampleRemoteRepo>();
		
		List<HolderValue<SampleRemoteRepo>> outputs = new ArrayList<>();
		HolderValue<SampleRemoteRepo> holderValue = new HolderValue<>();
		holderValue.setValue(remoterepo);
		outputs.add(holderValue);
		
		cmdExecOutput.setOutputs(outputs);	
		execOutput.setResult(cmdExecOutput);
		outputMap.put(0, execOutput);		
		responseValue.setResult(outputMap);
		
		ObjectMapper om = new ObjectMapper();
		String jsonresp;
		try {
			jsonresp = om.writeValueAsString(responseValue);
			return jsonresp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String mockSingleGenericExecuteResponse_GetNewSearch2() {
		SampleRemoteRepo2 remoterepo = new SampleRemoteRepo2();
		remoterepo.setId(Long.valueOf("1"));
		remoterepo.setAttr1("example1");
		SampleRepoNested2 nested = new SampleRepoNested2();
		nested.setNested_attr("nested1");
		
		List<SampleRepoNested2> nested_list = new ArrayList<>();		
		nested_list.add(nested);
		nested_list.add(nested);
		remoterepo.setAttr2(nested_list);
		CmdExecuteOutput<SampleRemoteRepo2> cmdExecOutput = new CmdExecuteOutput<>();		
		Map<Integer,ExecuteOutput.BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo2>>> outputMap = new HashMap<>();		
		ExecuteOutput.BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo2>> execOutput = new BehaviorExecute<CmdExecuteOutput<SampleRemoteRepo2>>(Behavior.$execute,new CmdExecuteOutput<SampleRemoteRepo2>());
		GenericExecute<SampleRemoteRepo2> responseValue = new GenericExecute<SampleRemoteRepo2>();
		
		List<HolderValue<SampleRemoteRepo2>> outputs = new ArrayList<>();
		HolderValue<SampleRemoteRepo2> holderValue = new HolderValue<>();
		holderValue.setValue(remoterepo);
		outputs.add(holderValue);
		
		cmdExecOutput.setOutputs(outputs);	
		execOutput.setResult(cmdExecOutput);
		outputMap.put(0, execOutput);		
		responseValue.setResult(outputMap);
		
		ObjectMapper om = new ObjectMapper();
		String jsonresp;
		try {
			jsonresp = om.writeValueAsString(responseValue);
			return jsonresp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String mockGenericExecuteResponse_Search() {
		List<SampleRemoteRepo> sample_repo_list = new ArrayList<>();
		SampleRemoteRepo remoterepo1 = new SampleRemoteRepo();
		remoterepo1.setAttr1("example1");
		SampleRepoNested nested = new SampleRepoNested();
		nested.setNested_attr("nested1");
		
		List<SampleRepoNested> nested_list = new ArrayList<>();		
		nested_list.add(nested);
		nested_list.add(nested);
		remoterepo1.setAttr2(nested_list);
		
		SampleRemoteRepo remoterepo2 = new SampleRemoteRepo();
		remoterepo2.setAttr1("example2");
		SampleRepoNested nested2 = new SampleRepoNested();
		nested2.setNested_attr("nested2");
		
		List<SampleRepoNested> nested_list2 = new ArrayList<>();		
		nested_list2.add(nested2);
		nested_list2.add(nested2);
		remoterepo2.setAttr2(nested_list2);
		sample_repo_list.add(remoterepo1);
		sample_repo_list.add(remoterepo2);
		
		CmdExecuteOutput<List<SampleRemoteRepo>> cmdExecOutput = new CmdExecuteOutput<>();		
		Map<Integer, BehaviorExecute<CmdExecuteOutput<List<SampleRemoteRepo>>>> outputMap = new HashMap<>();		
		ExecuteOutput.BehaviorExecute<CmdExecuteOutput<List<SampleRemoteRepo>>> execOutput = new BehaviorExecute<CmdExecuteOutput<List<SampleRemoteRepo>>>(Behavior.$execute,new CmdExecuteOutput<List<SampleRemoteRepo>>());
		GenericExecute<List<SampleRemoteRepo>> responseValue = new GenericExecute<List<SampleRemoteRepo>>();
		
		List<HolderValue<List<SampleRemoteRepo>>> outputs = new ArrayList<>();
		HolderValue<List<SampleRemoteRepo>> holderValue = new HolderValue<>();
		holderValue.setValue(sample_repo_list);
		outputs.add(holderValue);
		
		cmdExecOutput.setOutputs(outputs);	
		execOutput.setResult(cmdExecOutput);
		outputMap.put(0, execOutput);		
		responseValue.setResult(outputMap);
		
		ObjectMapper om = new ObjectMapper();
		String jsonresp;
		try {
			jsonresp = om.writeValueAsString(responseValue);
			return jsonresp;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
