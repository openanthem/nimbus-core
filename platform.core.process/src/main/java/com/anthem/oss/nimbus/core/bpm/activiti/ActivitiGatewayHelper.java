/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component @Scope("prototype")
@Getter @Setter
public class ActivitiGatewayHelper implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ActivitiContext activitiContext;
	
	public Command getCommand(){
		return getCommandMessage().getCommand();
	}
	
	public CommandMessage getCommandMessage(){
		return activitiContext.getProcessEngineContext().getCommandMsg();
	}
	
	public Object getInput(){
		return activitiContext.getProcessEngineContext().getInput();
	}
	
	public Object getOutput(){
		return activitiContext.getProcessEngineContext().getOutput();
	}
	
	public void setOutput(Object output){
		activitiContext.getProcessEngineContext().setOutput(output);
	}
	
	public String getProcessAlias(){
		String processAlias = getCommandMessage().getCommand().getElement(Type.ProcessAlias).map(e->e.getAlias()).orElse(null);
		return processAlias;
	}
	
//	public void setBackingCoreModel(String path, Object model){
//		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(getCommand());
//		if(quadModel == null)
//			return;
//		quadModel.getView().findParamByPath(path).getType().findIfNested().getBackingCoreModel().setState(model);
//	}
	
	public void setMessageOnModel(String path, String message){
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(getCommand());
		if(quadModel == null)
			return;
		StringBuilder modelPath = new StringBuilder();
		modelPath.append(path).append("#message");
		quadModel.getView().findStateByPath(modelPath.toString()).setState(message);
	}
	
	public void setStateByPath(String path, Object state){
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(getCommand());
		if(quadModel == null)
			return;
		Model viewSAC = quadModel.getView();
		viewSAC.findParamByPath(path).setState(state);		
		
	}
	
	public void setExceptionMessageOnModel(String path){
		QuadModel<?, ?> quadModel = UserEndpointSession.getOrThrowEx(getCommand());
		if(quadModel == null)
			return;
		StringBuilder modelPath = new StringBuilder();
		modelPath.append(path).append("#message");
		ExecuteOutput<?> output = (ExecuteOutput)activitiContext.getProcessEngineContext().getOutput();
		quadModel.getView().findStateByPath(modelPath.toString()).setState(output.getExecuteException().getMessage());
	}
	
	
}
