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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.RulesConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.RulesRuntime;
import com.antheminc.oss.nimbus.domain.rules.RulesEngineFactory;
import com.antheminc.oss.nimbus.domain.rules.RulesEngineFactoryProducer;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@Getter
@EnableLoggingInterceptor
public class SetByRuleFunctionalHandler <T, R> extends AbstractFunctionHandler<T, R> {
	
	@Autowired RulesEngineFactoryProducer rulesEngineFactoryProducer;
	
	@Autowired CommandExecutorGateway executorGateway;
	
	@Override
	public R execute(ExecutionContext eCtx, Param<T> actionParameter) {
		ModelConfig<?> mConfig = getDomainConfigBuilder().getRootDomainOrThrowEx(eCtx.getCommandMessage().getCommand().getRootDomainAlias());
		
		Class<?> clazz = mConfig.getReferredClass();
			
		RulesEngineFactory reFactory = getRulesEngineFactoryProducer().getFactory(clazz);
		
		Assert.notNull(reFactory, "Rule engine factory is null for the config "+mConfig);
		
		RulesConfig rConfig = reFactory.createConfig(eCtx.getCommandMessage().getCommand().getFirstParameterValue("rule"));
				
		Assert.notNull(rConfig, "Rule config is null for the rule: "+eCtx.getCommandMessage().getCommand().getFirstParameterValue("rule"));
		
		RulesRuntime rRuntime = reFactory.createRuntime(rConfig);
		rRuntime.start();
		
		List<Param<T>> params = new ArrayList<>();
		params.add(actionParameter);
		
		String[] associatedParamUris = eCtx.getCommandMessage().getCommand().getParameterValue("associatedParam");
		
		if(!ArrayUtils.isEmpty(associatedParamUris)) {
			
			Arrays.asList(associatedParamUris).forEach((associatedParamUri) -> {
				associatedParamUri = eCtx.getCommandMessage().getCommand().getRelativeUri(associatedParamUri);
				Command command = CommandBuilder.withUri(associatedParamUri).getCommand();
				CommandMessage newCommandMessage = new CommandMessage(command, eCtx.getCommandMessage().hasPayload() ?  eCtx.getCommandMessage().getRawPayload() :null);
				MultiOutput response = getExecutorGateway().execute(newCommandMessage);
				
				Param<T> associatedParam = (Param<T>)response.getSingleResult();
				params.add(associatedParam);
			});
		}
		
		rRuntime.fireRules(params.toArray(new Param[params.size()]));
		
		
		rRuntime.shutdown();
		
		return null;
	}
}
