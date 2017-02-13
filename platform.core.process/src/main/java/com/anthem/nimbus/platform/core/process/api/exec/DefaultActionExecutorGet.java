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
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.oss.nimbus.core.api.domain.state.DomainConfigAPI;
import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.model.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._get$execute")
public class DefaultActionExecutorGet extends AbstractProcessTaskExecutor {
	
	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Autowired
	DomainConfigAPI domainConfigApi;
	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		// Check if the quad model exists in session
		QuadModel<?, ?> quadModel = PlatformSession.getAttribute(cmd);
		if(quadModel != null){
			StateAndConfig.Model<?, ?> sac = cmd.isView() ? quadModel.getView() : quadModel.getCore();
			String path = cmd.buildUri(cmd.root().findFirstMatch(Type.DomainAlias).next());
			Param<Object> param = sac.findParamByPath(path);
			R state = (R)param.getState();
			return state;
		}
		String refId = cmd.getRefId(Type.DomainAlias);
		ModelRepository rep = repoFactory.get(cmd); 
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getMapsTo().value() : mConfig.getReferredClass();
		CoreDomain coreDomain = AnnotationUtils.findAnnotation(coreClass, CoreDomain.class);
		return (R)rep._get(refId, coreClass, coreDomain.value());
		
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {}

}
