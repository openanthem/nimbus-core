/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.repository.ModelRepository;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepositoryFactory;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.config.ActionExecuteConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.oss.nimbus.core.api.domain.state.DomainConfigAPI;

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
