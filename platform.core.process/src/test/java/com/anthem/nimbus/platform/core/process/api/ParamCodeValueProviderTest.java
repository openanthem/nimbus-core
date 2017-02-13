package com.anthem.nimbus.platform.core.process.api;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import com.anthem.nimbus.platform.spec.contract.process.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.Action;
import com.anthem.oss.nimbus.core.domain.Behavior;
import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.Constants;
import com.anthem.oss.nimbus.core.domain.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.ParamValue;

/**
 * @author Rakesh Patel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
public class ParamCodeValueProviderTest {


	@Autowired
	@Qualifier("default.processGateway")
	ProcessGateway processGateway;
	
	
	@Test
	public void testGetStaticCodeValue() {
		CommandMessage cmdMsg = build("staticCodeValue", "/status");
		
		MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
		List<ParamValue> values = output.getSingleResult();
		
		Assert.notEmpty(values);
		values.forEach(System.out::println);

	}
	
	@Test
	public void testGetModelCodeValue() {
		CommandMessage cmdMsg = build("cmprogram-programName","");
		
		MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
		List<ParamValue> values = output.getSingleResult();
		
		Assert.notEmpty(values);
		values.forEach(System.out::println);

	}
	
	@Test
	public void testGetNestedModelCodeValue() {
		CommandMessage cmdMsg = build("cmcase/patient-firstName&lastName", "");
		
		MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
		List<ParamValue> values = output.getSingleResult();
		
		Assert.notEmpty(values);
		values.forEach(System.out::println);

	}
	
	private CommandMessage build(String uri, String staticParamPath) {
		String[] uriWithPayload = uri.split(com.anthem.oss.nimbus.core.domain.CODE_VALUE_CONFIG_DELIMITER.code);
		Command cmd = CommandBuilder.withUri(com.anthem.oss.nimbus.core.domain.PARAM_VALUES_URI_PREFIX.code+uriWithPayload[0]+com.anthem.oss.nimbus.core.domain.PARAM_VALUES_URI_SUFFIX.code).getCommand();
		cmd.setAction(Action._search);
		cmd.templateBehaviors().add(Behavior.$execute);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		if(uriWithPayload.length > 1) {
			cmdMsg.setRawPayload(uriWithPayload[1]); // domain model lookup
		}
		else{
			cmdMsg.setRawPayload(staticParamPath); // static code value lookup
		}
		return cmdMsg;
	}
}
