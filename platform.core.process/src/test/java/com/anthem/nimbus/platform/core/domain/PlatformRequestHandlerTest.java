package com.anthem.nimbus.platform.core.domain;

import java.util.Arrays;
import java.util.LinkedList;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiGateway;
import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.HierarchyMatchBasedBeanFinder;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

import test.com.anthem.nimbus.platform.spec.model.comamnd.TestCommandFactory;

@SpringApplicationConfiguration(classes = {BPMEngineConfig.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlatformRequestHandlerTest {
	
	@Autowired
	ActivitiGateway processGateway;
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	TaskService taskService;
	
//	@Autowired
//	NavigationService navigationService;
	
	@Autowired
	RepositoryService repositoryService;
	
	@Autowired
	private HierarchyMatchBasedBeanFinder processKeyIdentifier;
	
	//@Test
	public void testFindPatient() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		processGateway.executeProcess(cmsg, bean.getUri());
	}
	
	//@Test
	public void testNavigation() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		processGateway.executeProcess(cmsg, bean.getUri());
		//repositoryService.
	}
	
	@Test
	public void testConfig() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._process);
		
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		Object a = processGateway.executeProcess(cmsg, bean.getUri());
		//a = processGateway.startProcess(cmsg);
		//a = processGateway.startProcess(cmsg);
//		a = processGateway.startProcess(cmsg);	
//		a = processGateway.startProcess(cmsg);	
//		a = processGateway.startProcess(cmsg);
		
		System.out.println(a);
	}
	
	
	//@Test
	public void testUserRole() {
		CommandMessage cmsg = create_view_icr_UserRoleFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._process);
		
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		Object a = processGateway.executeProcess(cmsg, bean.getUri());
		
		cmsg.setRawPayload("next");
		cmsg.getCommand().setAction(Action._nav);
		bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		a = processGateway.executeProcess(cmsg, bean.getUri());
		
		cmsg.getCommand().setAction(Action._process);
		bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		a = processGateway.executeProcess(cmsg, bean.getUri());
		
		cmsg.getCommand().setAction(Action._nav);
		bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		a = processGateway.executeProcess(cmsg, bean.getUri());	

		cmsg.getCommand().setAction(Action._process);
		bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		a = processGateway.executeProcess(cmsg, bean.getUri());	
		
		cmsg.setRawPayload("next");
		cmsg.getCommand().setAction(Action._nav);
		bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		a = processGateway.executeProcess(cmsg, bean.getUri());
		
		System.out.println(a);
	}
	
	//@Test
	public void testNav() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._nav);
		cmsg.setRawPayload("next");
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		processGateway.executeProcess(cmsg, bean.getUri());
	}

	//@Test
	public void testExecute() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._new);
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		processGateway.executeProcess(cmsg, bean.getUri());
		
	}
	

	//@Test
	public void testExecuteUpdate() {
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._update);
		HierarchyMatch bean = processKeyIdentifier.findMatchingBean(HierarchyMatch.class, cmsg.getCommand());
		processGateway.executeProcess(cmsg, bean.getUri());
	}

	//@Test
	public void testProcessLoad(){
		CommandMessage cmsg = create_view_icr_UMCaseFlow();
		cmsg.getCommand().setBehaviors(new LinkedList<>(Arrays.asList(Behavior.$execute)));
		cmsg.getCommand().setAction(Action._new);
		processKeyIdentifier.getProcessKeyForExecute(cmsg.getCommand());
		System.out.println("**Completed****");
	}
	
	//@Test
	public void testUserTask(){
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess123");
		taskService.complete("29");
		//runtimeService.activateProcessInstanceById(pi.getId());
		//pi = runtimeService.startProcessInstanceById(pi.getId());//(pi.getId());
		//taskService.getT
		//taskService.
		//Task tsk;
		Assert.assertNotNull(pi);
	}
	
	public static CommandMessage create_view_icr_UMCaseFlow() {
		CommandMessage msg = new CommandMessage();
		Command c = TestCommandFactory.create_view_icr_UMCaseFlow();
		msg.setCommand(c);
		//msg.setProcessName("platform_request_handler");
		return msg;
	}
	
	public static CommandMessage create_view_icr_UserRoleFlow() {
		CommandMessage msg = new CommandMessage();
		Command c = TestCommandFactory.create_view_UserRoleFlow();
		msg.setCommand(c);
		//msg.setProcessName("platform_request_handler");
		return msg;
	}
}
