/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.test.sample.um.model.core.UMCase;
import com.anthem.oss.nimbus.test.sample.um.model.view.UMCaseFlow;

import test.com.anthem.nimbus.platform.spec.model.command.TestCommandFactory;
import test.com.anthem.nimbus.platform.utils.JsonUtils;
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
