/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepository;
import com.anthem.nimbus.platform.core.process.api.repository.ModelRepositoryFactory;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.oss.nimbus.core.api.domain.state.DomainConfigAPI;
import com.anthem.oss.nimbus.core.api.domain.state.QuadModelBuilder;

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
	DomainConfigAPI domainConfigApi;
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		ModelRepository rep = repoFactory.get(cmd);
		
		QuadModel<?, ?> q = PlatformSession.getOrThrowEx(cmd);
		
		if(q.getCore() != null) {
			ModelConfig<?> mConfig = q.getCore().getConfig();
		}
		
		//StateAndConfig.Model<?, ?> sac = cmd.isView() ? q.getView() : q.getCore();
		
		String path = cmd.buildUri(cmd.root().findFirstMatch(Type.DomainAlias).next());
		
		Param<Object> param = null; //= mConfig.findParamByPath(path);
		
		//Convert from json payload to Object
		Object state = convert(cmdMsg, param);
		
		if(q.getCore() != null) {
			rep._replace(AnnotationUtils.findAnnotation(q.getCore().getConfig().getReferredClass(), CoreDomain.class).value(), state);
			
			// replace : refId, payload
		}
		
		return (R)Boolean.TRUE;
	}

}
