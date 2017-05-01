/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository.Projection;
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
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
