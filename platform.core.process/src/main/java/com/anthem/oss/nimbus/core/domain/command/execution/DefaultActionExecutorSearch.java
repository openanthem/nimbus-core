/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.nav.NavigationHandler;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorSearch extends AbstractProcessTaskExecutor {

	ModelRepositoryFactory repFactory;
	
	DomainConfigBuilder domainConfigApi;
	
	CommandMessageConverter converter;
	
	public DefaultActionExecutorSearch(ModelRepositoryFactory repFactory, DomainConfigBuilder domainConfigApi,
			CommandMessageConverter converter) {
		super();
		this.repFactory = repFactory;
		this.domainConfigApi = domainConfigApi;
		this.converter = converter;
	}
	
//	@Override
//	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
//		Command cmd = cmdMsg.getCommand();
//		
//		//String alias = cmd.getRootDomainAlias();
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
	
	@SuppressWarnings("unchecked")
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		return (R) doExecuteFunctionHandler(cmdMsg, NavigationHandler.class);
		
	}
	
	@Override
	protected <T extends FunctionHandler<?, ?>> T getHandler(CommandMessage commandMessage, Class<T> handlerClass) {
		T handler = super.getHandler(commandMessage, handlerClass);
		if(handler == null){
			handler = super.getHandler("default._nav$execute?fn=lookup",handlerClass);
		}
		return handler;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
