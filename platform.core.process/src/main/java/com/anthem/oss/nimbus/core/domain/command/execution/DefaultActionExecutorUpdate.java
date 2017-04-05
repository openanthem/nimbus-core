/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionStateTree;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.definition.EnableParamStateTree;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorUpdate extends AbstractProcessTaskExecutor {

	
	@SuppressWarnings("unchecked")
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute("testName","jayant",RequestAttributes.SCOPE_REQUEST);
		
		Command cmd = cmdMsg.getCommand();
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmd);
		
		Model<?> sac = cmd.isView() ? q.getView() : q.getCore();
		
		String path = cmd.buildUri(cmd.root().findFirstMatch(Type.DomainAlias).next());
		
		Param<Object> param = sac.findParamByPath(path);
		
		//Convert from json payload to Object
		Object state = convert(cmdMsg, param);
		
		//Check if the param is associated with a model that manintains parameter state
		boolean useParamStateTree = useParamStateTree(param);
		if(useParamStateTree){
			loadParamStateTree(param);
		}
		
		//Execute update with suppressMode
		q.getEventPublisher().apply(SuppressMode.ECHO);
		param.setState(state);
		q.getEventPublisher().apply(null); // should be persist only
		
		//fire rules post update
		q.getRoot().fireRules();
		// clear the apply on event publisher
	
		
		// get, new(save), replace, delete
		return (R)Boolean.TRUE;
	}
	
	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
	
	/**
	 * 
	 * @param param
	 * @return
	 */
	private boolean useParamStateTree(Param<Object> param){	
		Class<?> mapsToClass = param.getRootExecution().getConfig().getReferredClass();
		if( mapsToClass.getAnnotation(EnableParamStateTree.class) != null){
			return true;
		}
		return false;
	}	
	
	
	/**
	 * 
	 * @param param
	 */
	private void loadParamStateTree(Param<Object> param){
		Model<?> parent = param.getRootExecution();
		ExecutionStateTree stateTree = parent.getExecutionStateTree();
		if(stateTree == null){
			stateTree = new ExecutionStateTree();
			//parent.setExecutionStateTree(stateTree); //TODO refactor revisit commented due to framework change (Jayant ??)
		}
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(ExecutionStateTree.TRIGGER_PARAM_KEY, param, RequestAttributes.SCOPE_REQUEST);
		stateTree.resetStateForParameter();		
	}
	
	
}
