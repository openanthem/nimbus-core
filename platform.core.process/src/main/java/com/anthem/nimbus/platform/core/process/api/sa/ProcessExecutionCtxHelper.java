package com.anthem.nimbus.platform.core.process.api.sa;

import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.ActivitiContext;
import com.anthem.nimbus.platform.core.process.api.cache.session.PlatformSession;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AC67870
 *
 */
@Component("processExecutionCtx")
public class ProcessExecutionCtxHelper {
	
	@Getter @Setter 
	private ActivitiContext aCtx;

	//TODO temporary getting from loadQuadModel due to serialization issues in the activiti workflow
	public Object input() {
		//return this.aCtx.getProcessEngineContext().getInput();
		QuadModel<?, ?> q = PlatformSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
		return q.getView();
	}
	
	public Object output() {
		return this.aCtx.getProcessEngineContext().getOutput();
	}
	
	
}
