/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._search$execute")
public class DefaultActionExecutorSearch extends AbstractProcessTaskExecutor {

	@Autowired ModelRepositoryFactory repFactory;
	
	@Autowired DomainConfigBuilder domainConfigApi;
	
	@Autowired CommandMessageConverter converter;
	
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		String alias = cmd.getRootDomainAlias();
		
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		Class<?> criteriaClass = aec.getInput().getModel().getReferredClass();
		Object criteria = converter.convert(criteriaClass, cmdMsg);
		
		Class<?> resultClass = aec.getOutput().getModel().getReferredClass();
		
		ModelRepository rep = repFactory.get(cmdMsg.getCommand());
		
		R r = (R)rep._search(resultClass, alias, criteria);
		return r;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
