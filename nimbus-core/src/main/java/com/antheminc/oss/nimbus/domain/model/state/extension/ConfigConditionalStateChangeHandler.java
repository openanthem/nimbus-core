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
package com.antheminc.oss.nimbus.domain.model.state.extension;


import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextLoader;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
public class ConfigConditionalStateChangeHandler extends EvalExprWithCrudActions<ConfigConditional> {

	private CommandExecutorGateway commandGateway;
	
	private ExecutionContextLoader contextLoader;
	
	private boolean initialized;
	
	public ConfigConditionalStateChangeHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		
	}
	
	public void init() {
		if(initialized)
			return;
		
		setCommandGateway(getBeanResolver().get(CommandExecutorGateway.class));
		setContextLoader(getBeanResolver().get(ExecutionContextLoader.class));
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ConfigConditional configuredAnnotation) {
		init();
		
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		if(!isTrue)
			return;
		
		Config[] configs = configuredAnnotation.config();
		if (ArrayUtils.isEmpty(configs)) {
			throw new InvalidConfigException("No @Config found to execute conditionally on param: " + onChangeParam);
		}
		
		Command rootCmd = onChangeParam.getRootExecution().getRootCommand();
		ExecutionContext eCtx = getContextLoader().load(rootCmd);
		
		getCommandGateway().executeConfig(eCtx, onChangeParam, Arrays.asList(configs));
	}
	
//	@Override
//	public void onStateChange(ConfigConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
//		//init();
//		
//		boolean isTrue = evalWhen(event.getParam(), configuredAnnotation.when());
//		
//		if(!isTrue)
//			return;
//		
//		Config[] configs = configuredAnnotation.config();
//		if (ArrayUtils.isEmpty(configs)) {
//			throw new InvalidConfigException("No @Config found to execute conditionally on param: " + event.getParam());
//		}
//		
//		Command rootCmd = event.getParam().getRootExecution().getRootCommand();
//		ExecutionContext eCtx = getContextLoader().load(rootCmd);
//		
//		getCommandGateway().executeConfig(eCtx, event.getParam(), Arrays.asList(configs));
//	}
}
