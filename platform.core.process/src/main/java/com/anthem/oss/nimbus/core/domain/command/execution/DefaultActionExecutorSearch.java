package com.anthem.oss.nimbus.core.domain.command.execution;


import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;


/**
 * @author Soham Chakravarti
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
		return Output.instantiate(input, input.getContext(), response);
	}



	
//	@Override
//	protected <R> R executeInternal(CommandMessage cmdMsg) {
//		Command cmd = cmdMsg.getCommand();
//		
//		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
//		Class<?> criteriaClass = aec.getInput().getModel().getReferredClass();
//		Object criteria = null;
//		if(cmd.getRequestParams()!=null && cmd.getRequestParams().get("criteria")!=null) {
//			criteria = cmd.getRequestParams().get("criteria")[0];
//		} else {
//			criteria = converter.convert(criteriaClass, cmdMsg);
//		}
//		
//		Class<?> resultClass = aec.getOutput().getModel().getReferredClass();
//		
//		ModelRepository rep = repFactory.get(cmdMsg.getCommand());
//		
//		String alias = AnnotationUtils.findAnnotation(resultClass, Repo.class).alias(); // TODO Move this at the repo level, so below method should only pass refId and coreClass
//		
//		if(StringUtils.isBlank(alias)) {
//			alias = AnnotationUtils.findAnnotation(resultClass, Domain.class).value();
//		}
//		R r;
//		if(cmd.getRequestParams()!=null && cmd.getRequestParams().get("projection")!=null) {
//			r = (R)rep._search(resultClass, alias, criteria,Projection.COUNT);
//		} else {
//			r = (R)rep._search(resultClass, alias, criteria);
//		}
//		return r;
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
//		return (R) doExecuteFunctionHandler(cmdMsg, NavigationHandler.class);
//		
//	}
//	
//	@Override
//	protected <T extends FunctionHandler<?, ?>> T getHandler(CommandMessage commandMessage, Class<T> handlerClass) {
//		T handler = super.getHandler(commandMessage, handlerClass);
//		if(handler == null){
//			handler = super.getHandler("default._nav$execute?fn=lookup",handlerClass);
//		}
//		return handler;
//	}


}
