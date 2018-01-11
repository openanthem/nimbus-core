//package com.antheminc.oss.nimbus.core.domain.command.execution.process;
//
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//
//import com.antheminc.oss.nimbus.core.AbstractUnitTest;
//import com.antheminc.oss.nimbus.core.domain.command.Behavior;
//import com.antheminc.oss.nimbus.core.domain.command.Command;
//import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
//import com.antheminc.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
//
//@EnableAutoConfiguration
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class SetFunctionHandlerTest extends AbstractUnitTest{
//	
//	@Autowired SetFunctionHandler<?,?> setFunctionHandler;
//	
//	@Test
//	public void test_Internal() {
//		Command command = prepareCommand("/Acme/admin/p/testmappedmodel/_new", Behavior.$execute);
//		MultiExecuteOutput output = getMultiExecuteOutput(command, null);
//		ExecutionContext context = output.getSingleResult();
//		context.getRootModel().findParamByPath("/parameter1").setState("Parameter1");
//		setFunctionHandler.execute(context, context.getRootModel().findParamByPath("/parameter1"));
//	}
//	
//}
