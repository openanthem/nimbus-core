/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionBehaviorExecutorNewInit extends DefaultActionExecutorNew {

	public DefaultActionBehaviorExecutorNewInit(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected Output<Param<?>> executeInternal(Input input) {
		ExecutionContext eCtx = handleNewDomainRoot(input.getContext());
	
		Param<Object> actionParam = findParamByCommandOrThrowEx(eCtx);
		setStateNew(eCtx, input.getContext().getCommandMessage(), actionParam);
		
		return null;
	}

	@Override
	protected void setStateNew(ExecutionContext eCtx, CommandMessage cmdMsg, Param<Object> p) {
		Object newState = eCtx.getQuadModel().getView().getLeafState();
		p.setState(newState);
	}

	
	
	
}
