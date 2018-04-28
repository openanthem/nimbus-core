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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractCommandExecutor;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 * @author Tony Lopez
 *
 */
public class DefaultActionExecutorUpdate extends AbstractCommandExecutor<Boolean> {
	
	public DefaultActionExecutorUpdate(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		
		if(p.isCollection())
			handleCollection(eCtx, p.findIfCollection());
		else 
			handleParam(eCtx, p);
		
		// TODO use the Action output from the setState to check if the action performed is _update to return true
		//, else false - right now it either return _new or _replace (change detection is not yet implemented)
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
	protected void handleCollection(ExecutionContext eCtx, ListParam<Object> p) {
		// perform add on collection
		Object colElemState = getConverter().toReferredType(p.getType().getModel().getElemConfig(), eCtx.getCommandMessage().getRawPayload());
		
		p.add(colElemState);
	}
	
	protected void handleParam(ExecutionContext eCtx, Param<Object> p) {
		getConverter().updateParam(p, eCtx.getCommandMessage().getRawPayload());
	}

}