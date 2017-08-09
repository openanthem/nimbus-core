package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
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
		Class<Object> refClass = p.getType().getModel().getElemConfig().getReferredClass();
		Object colElemState = getConverter().convert(refClass, eCtx.getCommandMessage().getRawPayload());
		
		p.add(colElemState);
	}
	
	protected void handleParam(ExecutionContext eCtx, Param<Object> p) {
		Object state = getConverter().convert(p.getConfig().getReferredClass(), eCtx.getCommandMessage().getRawPayload());
		
		p.setState(state);
	}
	
}