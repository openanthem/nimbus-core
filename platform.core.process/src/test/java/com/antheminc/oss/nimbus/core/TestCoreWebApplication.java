package com.antheminc.oss.nimbus.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.test.sample.um.model.core.UMCase;
import com.antheminc.oss.nimbus.test.sample.um.model.view.UMCaseFlow;

import test.com.antheminc.oss.nimbus.platform.spec.model.command.TestCommandFactory;
import test.com.antheminc.oss.nimbus.platform.utils.JsonUtils;
/**
 * @author Soham Chakravarti
 *
 */
@SpringBootApplication
@ConfigurationProperties
@ActiveProfiles("test")
public class TestCoreWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestCoreWebApplication.class, args);
	}
}


@RestController
class TestRestController {
	
	@Autowired
	QuadModelBuilder builder;
	
	@GetMapping("/")
	public ExecuteOutput<String> testPersistence() {
		ExecuteOutput<String> executeOutput = null;
		
		Command cmd = TestCommandFactory.create_view_icr_UMCaseFlow();
		//QuadModel<UMCaseFlow, UMCase> q = quadModelBuilder.build(cmd, (mConfig)->ModelsTemplate.newInstance(mConfig.getReferredClass()));
		QuadModel<UMCaseFlow, UMCase> q = builder.build(cmd);
		
		q.getView().findParamByPath("/pg3/aloha").setState("Test_Aloha");
		
		Param<String> param = q.getView().findParamByPath("/umCaseDisplayId");
		
		if(param != null)
			executeOutput = new ExecuteOutput<String>(param.getState());
		
		if(executeOutput == null)
			executeOutput = new ExecuteOutput<String>(JsonUtils.get().convert(q.getView()));
		
		q.getView().findParamByPath("/pg3/aloha").setState("Test_Aloha_After");
		
		return executeOutput;
		
	}
	
}
