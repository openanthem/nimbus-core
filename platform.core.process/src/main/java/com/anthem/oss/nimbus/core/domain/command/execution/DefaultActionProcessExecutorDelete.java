/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionProcessExecutorDelete {} /*extends AbstractProcessTaskExecutor {

	ModelRepositoryFactory repoFactory;
	
	DomainConfigBuilder domainConfigApi;
	
	public DefaultActionProcessExecutorDelete(ModelRepositoryFactory repoFactory, DomainConfigBuilder domainConfigApi) {
		this.repoFactory = repoFactory;
		this.domainConfigApi = domainConfigApi;
	}
	
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
		
		Class<?> coreClass = mConfig.isMapped() ? mConfig.findIfMapped().getMapsTo().getReferredClass() : mConfig.getReferredClass();
		Domain coreDomain = AnnotationUtils.findAnnotation(coreClass, Domain.class);
		
		return (R)rep._delete(refId, coreClass, coreDomain.value());
		
	
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}

}
*/