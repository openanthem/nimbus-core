package com.anthem.oss.nimbus.core.domain.command.execution;


import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;


/**
 * @author Rakesh Patel
 *
 */
public class DefaultActionExecutorSearch<T, R> extends AbstractFunctionCommandExecutor<T, R> {
	
	public DefaultActionExecutorSearch(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Output<R> executeInternal(Input input) {
		R response = (R)executeFunctionHanlder(input, FunctionHandler.class);
		
		String projectPath = input.getContext().getCommandMessage().getCommand().getFirstParameterValue("project");
		// TODO optimize for more than one connection output
		if(StringUtils.isNotBlank(projectPath)){
			Param<R> pRoot = findParamByCommandOrThrowEx(input.getContext());
			if(response instanceof Collection<?>) {
				response = (R) ((Collection<?>) response).iterator().next();
			}
			pRoot.setState(response);
			response = (R)pRoot.findParamByPath(projectPath).getState();
		}
		
		return Output.instantiate(input, input.getContext(), response);
	}

}
