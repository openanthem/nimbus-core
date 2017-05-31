/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorSearch<R> implements CommandExecutor<R> {
	
	@Override
	public Output<R> execute(Input input) {
		// TODO Auto-generated method stub
		return null;
	}
	
}/* extends AbstractProcessTaskExecutor {

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
	
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		//String alias = cmd.getRootDomainAlias();
		
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		Class<?> criteriaClass = aec.getInput().getModel().getReferredClass();
		//TODO - add condition that Rakesh creates to get the query parameter from command
		Object criteria = null;
		if(cmd.getRequestParams()!=null && cmd.getRequestParams().get("criteria")!=null) {
			criteria = cmd.getRequestParams().get("criteria")[0];
		} else {
			criteria = converter.convert(criteriaClass, cmdMsg);
		}
		
		Class<?> resultClass = aec.getOutput().getModel().getReferredClass();
		
		ModelRepository rep = repFactory.get(cmdMsg.getCommand());
		
		String alias = AnnotationUtils.findAnnotation(resultClass, Repo.class).alias(); // TODO Move this at the repo level, so below method should only pass refId and coreClass
		
		if(StringUtils.isBlank(alias)) {
			alias = AnnotationUtils.findAnnotation(resultClass, Domain.class).value();
		}
		R r;
		if(cmd.getRequestParams()!=null && cmd.getRequestParams().get("projection")!=null) {
			r = (R)rep._search(resultClass, alias, criteria,Projection.COUNT);
		} else {
			r = (R)rep._search(resultClass, alias, criteria);
		}
		return r;
	}


}
*/