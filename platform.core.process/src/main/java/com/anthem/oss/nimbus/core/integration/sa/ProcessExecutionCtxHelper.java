package com.anthem.oss.nimbus.core.integration.sa;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

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
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(aCtx.getProcessEngineContext().getCommandMsg().getCommand());
		return q.getView();
	}
	
	public Object output() {
		return this.aCtx.getProcessEngineContext().getOutput();
	}
	
	
}
