/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorGet extends AbstractProcessTaskExecutor {
	
	ModelRepositoryFactory repoFactory;
	
	DomainConfigBuilder domainConfigApi;
	
	public DefaultActionExecutorGet(ModelRepositoryFactory repoFactory, DomainConfigBuilder domainConfigApi) {
		this.repoFactory = repoFactory;
		this.domainConfigApi = domainConfigApi;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		// Check if the quad model exists in session
		QuadModel<?, ?> quadModel = UserEndpointSession.getAttribute(cmd);
		if(quadModel != null){
			Model<?> sac = cmd.isView() ? quadModel.getView() : quadModel.getCore();
			String path = cmd.buildUri(cmd.root().findFirstMatch(Type.DomainAlias).next());
			Param<Object> param = sac.findParamByPath(path);
			R state = (R)param.getState();
			return state;
		}
		String refId = cmd.getRefId(Type.DomainAlias);
		ModelRepository rep = repoFactory.get(cmd); 
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		Class<?> coreClass = mConfig.isMapped() ? mConfig.findIfMapped().getMapsTo().getReferredClass() : mConfig.getReferredClass();
		
		Domain coreDomain = AnnotationUtils.findAnnotation(coreClass, Domain.class);
		return (R)rep._get(refId, coreClass, coreDomain.value());
		
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {}

}
