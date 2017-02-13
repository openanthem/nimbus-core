/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessExecutorEvents;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigAPI;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._delete$execute")
public class DefaultActionProcessExecutorDelete extends AbstractProcessTaskExecutor {
	
	
	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Autowired
	DomainConfigAPI domainConfigApi;
	
	@SuppressWarnings("unchecked")

	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		//return null;
		
		

		Command cmd = cmdMsg.getCommand();
		String refId = cmd.getRefId(Type.DomainAlias);
		
		ModelRepository rep = repoFactory.get(cmd); 
		
		//QuadModel<?, ?> q = qBuilder.build(cmd, cConfig-> 
											//	rep._get(refId, cConfig.getReferredClass(), AnnotationUtils.findAnnotation(cConfig.getReferredClass(), CoreDomain.class).value()));
		//PlatformSession.setAttribute(cmd, q);
		//initiateProcessExecution(cmdMsg, q);
		
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getMapsTo().value() : mConfig.getReferredClass();
		CoreDomain coreDomain = AnnotationUtils.findAnnotation(coreClass, CoreDomain.class);
		
		return (R)rep._delete(refId, coreClass, coreDomain.value());
		
	
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
