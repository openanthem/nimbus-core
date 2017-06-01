/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Input;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.Output;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionExecutorUpdate extends AbstractCommandExecutor<Param<?>>{

	public DefaultActionExecutorUpdate(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	@Override
	protected Output<Param<?>> executeInternal(Input input) {
		ExecutionContext eCtx = handleUpdateDomainRoot(input.getContext());
		
		
		return null;
	}
	
	protected ExecutionContext handleUpdateDomainRoot(ExecutionContext eCtx) {
		if(eCtx.getQuadModel()!=null)
			return eCtx;
		return null;
	}
	
}








/*extends AbstractProcessTaskExecutor {

	
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
		
		//Execute update with suppressMode
		//q.getEventPublisher().apply(SuppressMode.ECHO);
		param.setState(state);
		//q.getEventPublisher().apply(null); // should be persist only
		
		//fire rules post update
		q.getRoot().fireRules();
		// clear the apply on event publisher
		
		// get, new(save), replace, delete
		return (R)Boolean.TRUE;
	}
	

	private boolean useParamStateTree(Param<Object> param){	
		Class<?> mapsToClass = param.getRootExecution().getConfig().getReferredClass();
		if( mapsToClass.getAnnotation(EnableParamStateTree.class) != null){
			return true;
		}
		return false;
	}	
}
*/