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
package com.anthem.oss.nimbus.core.bpm;

import org.activiti.engine.delegate.DelegateExecution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultExpressionHelper extends AbstractExpressionHelper {
	
	private final CommandMessageConverter converter;
	
	public DefaultExpressionHelper(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	public String _json(ExecutionContext eCtx, DelegateExecution execution, Object... args) {
		return converter.convert(args[0]);
	}
	
	public Object[] _array(ExecutionContext eCtx, DelegateExecution execution, Object... args) {
		return args;
	}	
	
	public String _buildUrl(ExecutionContext eCtx, DelegateExecution execution, String... args) {
		StringBuilder url = new StringBuilder(args[0]);
		url.append("?");
		if(args.length > 1){
			for(int i = 1; i < args.length-1; i=i+2){
				url.append(args[i]).append("=").append(args[i+1]).append("&");
			}
		}
		return url.toString();
	}	
	
	
	public String _concat(ExecutionContext eCtx, DelegateExecution execution, String... args) {
		StringBuilder result = new StringBuilder();
		for(String arg: args){
			result.append(arg);
		}
		return result.toString();
	}		
	
	
	public Object _getState(ExecutionContext eCtx, DelegateExecution execution, Param<?>... args) {
		return args[0].getState();
	}	
	
	public void _setState(ExecutionContext eCtx, DelegateExecution execution, String... args) {
		
		if(args.length > 1) {
			for(int i = 0; i < args.length-1; i=i+2){
				//eCtx.getQuadModel().getRoot().findParamByPath(args[0]).setState(args[i+1]);
				eCtx.getQuadModel().getView().findParamByPath(args[0]).setState(args[i+1]);
			}
		}
		
	}
}
