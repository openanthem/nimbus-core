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

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.function.Consumer;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter(AccessLevel.PROTECTED)
public abstract class EvalExprWithCrudActions<A extends Annotation> extends AbstractConditionalStateEventHandler<A> {
	
	public EvalExprWithCrudActions(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	public void onStateLoad(A configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation, StateEventType.ON_LOAD);
	}
	
	@Override
	public void onStateLoadNew(A configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation);
	}
	
	@Override
	public void onStateChange(A configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		EnumSet<Action> validSet = EnumSet.of(Action._new, Action._update, Action._replace, Action._delete);
		
		if(!validSet.contains(event.getAction()))
			return;
		
		handleInternal(event.getParam(), configuredAnnotation, StateEventType.ON_CHANGE);
	}
	
	protected void handleInternal(Param<?> onChangeParam, A configuredAnnotation, StateEventType stateEventType) {
		setStateEventType(stateEventType);
		handleInternal(onChangeParam, configuredAnnotation);
	}
	
	protected abstract void handleInternal(Param<?> onChangeParam, A configuredAnnotation);
	
	protected void handleInternal(Param<?> onChangeParam, String targetPath, Consumer<Param<?>> executeCb) {
		Param<?> targetParam = retrieveParamByPath(onChangeParam, targetPath);

		executeCb.accept(targetParam);
	}
}
