package com.anthem.oss.nimbus.core;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultCommandExecutorGateway;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.anthem.oss.nimbus.core.*"})
@SpringBootTest(classes = { BPMEngineConfig.class })
@ActiveProfiles("test")
@Getter
public abstract class AbstractUnitTest {
	
	@Autowired
    @Qualifier("default.processGateway")
    DefaultCommandExecutorGateway processGateway;
    
    private JacksonTester<Object> json; // use this in the sub classes to validate json payload
    
    @Before
    public void setup() {
        ObjectMapper objectMapper = new ObjectMapper(); 
        JacksonTester.initFields(this, objectMapper);
    }
    
    protected Command prepareCommand(String uri, Behavior... behaviors){
        Command command = CommandBuilder.withUri(uri).getCommand();
        if(behaviors != null) {
        	Arrays.asList(behaviors).forEach((b) ->command.templateBehaviors().add(b));
        }
        return command;
    }

    protected List<Output<?>> getMultiExecuteOutput(Command command, String rawPayload){
        CommandMessage cmdMsg = new CommandMessage();
        cmdMsg.setCommand(command);
        cmdMsg.setRawPayload(rawPayload);
        MultiOutput resp = processGateway.execute(cmdMsg);
        List<Output<?>> execop = resp.getOutputs();
        return execop;
    }

}
