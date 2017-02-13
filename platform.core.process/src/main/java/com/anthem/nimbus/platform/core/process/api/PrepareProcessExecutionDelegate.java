package com.anthem.nimbus.platform.core.process.api;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */

@Component
@Scope("prototype")
public class PrepareProcessExecutionDelegate implements JavaDelegate {

	private static final String PROCESS_ENGINE_GTWY_KEY = "processGatewayContext"; //TODO move to a constant file as being referred by multiple delegate classes
	
	@Override
	public void execute(DelegateExecution execution) {
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(PROCESS_ENGINE_GTWY_KEY);

		ProcessEngineContext pCtx = aCtx.getProcessEngineContext();
		QuadModel<?,?> quad = PlatformSession.getOrThrowEx(pCtx.getCommandMsg().getCommand());
		pCtx.setInput(quad.getView());

	}

}
