/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultActionExecutorReplace extends AbstractCommandExecutor<Boolean> {
	
	public DefaultActionExecutorReplace(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	protected Output<Boolean> executeInternal(Input input) {
		ExecutionContext eCtx = input.getContext();
		
		ModelConfig<?> rootDomainConfig = getRootDomainConfig(eCtx);
		
		Param<Object> p = findParamByCommandOrThrowEx(eCtx);
		
		Object state = getConverter().convert(p.getConfig().getReferredClass(), eCtx.getCommandMessage().getRawPayload());
		
		/* 
		 * TODO setState should be used instead of below repo call once the change detection is implemented during setState. 
		 * then this class would be same as _update executor
		 */
		//p.setState(state); 
		getRepositoryFactory().get(rootDomainConfig.getRepo())._replace(rootDomainConfig.getAlias(), state);
		
		/*
		 *  TODO use the Action output from the setState to check if the action performed is _update to return true
		 *  else false - (change detection is not yet implemented during set state)
		 */
		return Output.instantiate(input, eCtx, Boolean.TRUE);
	}
	
} 
