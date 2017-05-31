/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionProcessExecutorReplace {} /*extends AbstractProcessTaskExecutor {

	DefaultQuadModelBuilder qBuilder;
	
	ModelRepositoryFactory repoFactory;
	
	DomainConfigBuilder domainConfigApi;
	
	public DefaultActionProcessExecutorReplace(DefaultQuadModelBuilder qBuilder, ModelRepositoryFactory repoFactory,
			DomainConfigBuilder domainConfigApi) {
		this.qBuilder = qBuilder;
		this.repoFactory = repoFactory;
		this.domainConfigApi = domainConfigApi;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		ModelRepository rep = repoFactory.get(cmd);
		
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmd);
		
		if(q.getCore() != null) {
			ModelConfig<?> mConfig = q.getCore().getConfig();
		}
		
		//StateAndConfig.Model<?, ?> sac = cmd.isView() ? q.getView() : q.getCore();
		
		String path = cmd.buildUri(cmd.root().findFirstMatch(Type.DomainAlias).next());
		
		Param<Object> param = null; //= mConfig.findParamByPath(path);
		
		//Convert from json payload to Object
		Object state = convert(cmdMsg, param);
		
		if(q.getCore() != null) {
			rep._replace(AnnotationUtils.findAnnotation(q.getCore().getConfig().getReferredClass(), Domain.class).value(), state);
			
			// replace : refId, payload
		}
		
		return (R)Boolean.TRUE;
	}

}
*/