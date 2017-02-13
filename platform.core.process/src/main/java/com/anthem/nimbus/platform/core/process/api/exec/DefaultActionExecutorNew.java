/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.oss.nimbus.core.api.domain.state.DomainConfigAPI;
import com.anthem.oss.nimbus.core.api.domain.state.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.Command;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
import com.anthem.oss.nimbus.core.domain.model.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Soham Chakravarti
 *
 */
@Component("default._new$execute")
public class DefaultActionExecutorNew extends AbstractProcessTaskExecutor {

	@Autowired @Qualifier("default.quadModelBuilder")
	private QuadModelBuilder quadModelBuilder; 
	
	@Autowired
	DomainConfigAPI domainConfigApi;
	
	/**
	 * 1. If the command is domain root only, then create new instance <br>
	 * <tab>	1.1. Check if payload contains json for initial object to be inserted; convert if available
	 * <tab>	1.2. Else, create new instance and call rep to persist
	 * <tab>	1.3. Update command with domain root refId	
	 * 2. Else, use the payload of command message json to convert & instantiate desired object <br>
	 * <tab>	2.1. Traverse object model path using command domain uri <br>
	 * <tab>	2.2. Set newly instantiated object and return  	
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <R> R doExecuteInternal(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		ModelConfig<?> mConfig = aec.getOutput().getModel();
		
		Class<?> coreClass = mConfig.isMapped() ? mConfig.getMapsTo().value() : mConfig.getReferredClass();
		
		return (R)ModelsTemplate.newInstance(coreClass);
		
		//QuadModel<?, ?> q = quadModelBuilder.build(cmd, (mConfig)->ModelsTemplate.newInstance(mConfig.getReferredClass()));
		//PlatformSession.setAttribute(cmd, q);
		
		//init rules
		//q.fireAllRules();
		
		//init process
		//initiateProcessExecution(cmdMsg, q);
		
		//return (R)q;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
}
