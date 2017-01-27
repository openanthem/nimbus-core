/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.domain.DomainConfigAPI;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepository;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepositoryFactory;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.config.ActionExecuteConfig;
import com.anthem.nimbus.platform.utils.converter.CommandMessageConverter;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._search$execute")
public class DefaultActionExecutorSearch extends AbstractProcessTaskExecutor {

	@Autowired ModelRepositoryFactory repFactory;
	
	@Autowired DomainConfigAPI domainConfigApi;
	
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
