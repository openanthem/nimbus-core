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

import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.internal.MappedDefaultParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.builder.internal.DefaultEntityStateBuilder;
import com.antheminc.oss.nimbus.domain.model.state.internal.DefaultParamState;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter
@EnableLoggingInterceptor
public class InstantiateParamFunctionalHandler <T, R> extends AbstractFunctionHandler<T, R> {
	
	@Autowired DefaultEntityStateBuilder abstractEntityStateBuilder;
	
	@Autowired CommandExecutorGateway executorGateway;
	
	@Override
	public R execute(ExecutionContext eCtx, Param<T> actionParameter) {
		String modelPath = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.LAZY_LOAD_MODEL.code)+"/_get";
		String param = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.LAZY_LOAD_PARAM.code);
		modelPath = eCtx.getCommandMessage().getCommand().getRelativeUri(modelPath);
		Command command = CommandBuilder.withUri(modelPath).getCommand();
		CommandMessage newCommandMessage = new CommandMessage(command, null);
		MultiOutput output = executorGateway.execute(newCommandMessage);
		Param<?> modelParam = (Param<?>)output.getSingleResult();
		Model<?> model = modelParam.findIfNested();
		if(model != null) {
			Param<?> nestedParam = model.findParamByPath(param);
			if(nestedParam == null) {
				ModelConfig<?> mConfig = model.getConfig();
				ParamConfig<?> mpConfig = model.getConfig().findParamByPath(param);
				boolean isMappedNoConversion = modelParam.isMapped() 
						? !abstractEntityStateBuilder.requiresConversion(modelParam)
								: false;
				ParamConfig<?> resolvedParamConfig;
				if(isMappedNoConversion) {
					resolvedParamConfig = new MappedDefaultParamConfig.NoConversion<>(mConfig, mpConfig);
				} else {
					resolvedParamConfig = mpConfig;
				}
				Model<?> mapsToSAC = findMapsToModel(model);
				DefaultParamState<?> mpState = abstractEntityStateBuilder.buildParam(model.getAspectHandlers(), model, resolvedParamConfig, mapsToSAC);
				model.templateParams().add(mpState);
			}
		}
		
		return null;
	}
	
	private Model<?> findMapsToModel(Model<?> model){
		Model<?> mapsToSAC = null;
		while(model != null) {
			if(model.findIfMapped() != null) {
				mapsToSAC = model.findIfMapped().getMapsTo();
				break;
			}
			model = model.getAssociatedParam().getParentModel();
		}
		return mapsToSAC;
	}
}
