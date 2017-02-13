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
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._replace$execute")
public class DefaultActionProcessExecutorReplace extends AbstractProcessTaskExecutor {

	@Autowired 
	QuadModelBuilder qBuilder;
	
	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Autowired
	DomainConfigBuilder domainConfigApi;
	
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
