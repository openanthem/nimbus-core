/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.activiti.bpmn.model.MapExceptionEntry;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessResponse;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.State;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.util.JustLogit;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiCallActivityBehavior extends CallActivityBehavior {
	
	private static final long serialVersionUID = 1L;
	private String mappedParameterPath;
	private JustLogit logit = new JustLogit(this.getClass());

	public ActivitiCallActivityBehavior(String processDefinitionKey, List<MapExceptionEntry> mapExceptions) {
		   super(processDefinitionKey,mapExceptions);
	 }
		  
	 public ActivitiCallActivityBehavior(Expression processDefinitionExpression, List<MapExceptionEntry> mapExceptions) {
		  super(processDefinitionExpression,mapExceptions);
	 }
	 
	 
	@Override
	public void execute(DelegateExecution execution) {
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY);
		if(mappedParameterPath != null){
			CommandMessage cmdMessage = aCtx.getProcessEngineContext().getCommandMsg();
			QuadModel<?,?> quadModel = UserEndpointSession.getOrThrowEx(cmdMessage.getCommand());
			Param<?> targetParam = quadModel.getView().findParamByPath(mappedParameterPath);
			String stateURL = deriveStateURLFromCommand(cmdMessage);
			if(targetParam.getConfig().findIfMapped().getPath().state() == State.Internal){
				Param sourceParam = quadModel.getCore().findParamByPath(stateURL);
				targetParam.findIfMapped().setMapsTo(sourceParam);
			}else{
				StringBuilder mappedStatePath = new StringBuilder(mappedParameterPath);
				mappedStatePath.append(".m");
				quadModel.getView().findModelByPath(mappedStatePath.toString()).setState(deriveMappedState(stateURL));
			}
			
			
		}
		super.execute(execution);
	}
	 
	/**
	 * This method looks at the command message to see if there is a mapped state url.
	 * @param cmdMessage
	 * @return
	 */
	private String deriveStateURLFromCommand(CommandMessage cmdMessage){
		String payload = cmdMessage.getRawPayload();
		if(payload == null)
			return null;	
		String urlToDeriveMappedState = null;
		String[] requestParameters = payload.split("&");
		for(String requestParameter : requestParameters){
			String[] requestParameterEntry = requestParameter.split("=");
			if(requestParameterEntry[0].equals("stateUrl")){
				urlToDeriveMappedState =  requestParameterEntry[1];
				break;
			}
		}
		if(urlToDeriveMappedState != null){
			try {
				urlToDeriveMappedState = URLDecoder.decode(urlToDeriveMappedState,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				logit.error(() -> "Inavlid URL Format.");
			}
			
		}
		return urlToDeriveMappedState;	
	}
	
	/**
	 * Invoke Process Gateway with the url to retrieve state
	 * @param urlToDeriveMappedState
	 * @return
	 */
	private Object deriveMappedState(String urlToDeriveMappedState){
		if(urlToDeriveMappedState == null)
			return null;
		ProcessGateway processGateway = (ProcessGateway)ProcessBeanResolver.appContext.getBean("default.processGateway");
		Command command = CommandBuilder.withUri(urlToDeriveMappedState).getCommand();
		CommandMessage cmdMsg =  new CommandMessage();
		cmdMsg.setCommand(command);
		ProcessResponse response = processGateway.executeProcess(cmdMsg);
		return response.getResponse();
	}
	
	
}
