package com.anthem.oss.nimbus.core;

import java.util.Arrays;

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
import com.anthem.oss.nimbus.core.domain.command.execution.DefaultProcessGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
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
    DefaultProcessGateway processGateway;
    
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

    protected MultiExecuteOutput getMultiExecuteOutput(Command command, String rawPayload){
        CommandMessage cmdMsg = new CommandMessage();
        cmdMsg.setCommand(command);
        cmdMsg.setRawPayload(rawPayload);
        ProcessResponse resp = processGateway.executeProcess(cmdMsg);
        MultiExecuteOutput execop = (MultiExecuteOutput)resp.getResponse();
        return execop;
    }

}
