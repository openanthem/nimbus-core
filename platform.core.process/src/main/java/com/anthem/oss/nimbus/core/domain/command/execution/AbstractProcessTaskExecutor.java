/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.builder.QuadModelBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Setter
public abstract class AbstractProcessTaskExecutor implements ProcessTaskExecutor, ApplicationContextAware {
	
	AbstractProcessTaskExecutor defaultNewExec;
	AbstractProcessTaskExecutor defaultGetExec;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;

	@Autowired @Qualifier("default.modelRepositoryFactory")
	ModelRepositoryFactory modelRepositoryFactory;
	
	@Autowired CommandMessageConverter converter;
	
	@Autowired
	HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader;
	
	@Autowired 
	QuadModelBuilder qBuilder;
	
	private JustLogit logit = new JustLogit(getClass());
	
	ApplicationContext appCtx;
	
	
	@Override
	public void setApplicationContext(ApplicationContext appCtx) throws BeansException {
		this.appCtx = appCtx;
		this.defaultNewExec = appCtx.getBean("default._new$execute", AbstractProcessTaskExecutor.class);
		this.defaultGetExec = appCtx.getBean("default._get$execute", AbstractProcessTaskExecutor.class);
	}
	
	@Override
	final public <R> R doExecute(CommandMessage cmdMsg) {
		logit.debug(()->"[doExecute-in][cmdMsg: "+cmdMsg+"]");
		try {
			preExecute(cmdMsg);
			
			crawlCommand(cmdMsg);
			
			R resp = doExecuteInternal(cmdMsg);
			postExecute(cmdMsg, resp);
			
			logit.debug(()->"[doExecute-out]");
//			if(cmdMsg.getCommand().getBehaviors().size() - 1 == cmdMsg.getCommand().getCurrentBehaviorIndex()) {
//				PlatformSession.setAttribute(cmdMsg.getCommand(), null);
//			}

			return resp;
		} catch (Exception ex) {
			logit.error(()->"[doExecute-error][handing off to onError for ex: "+ex.getMessage());
			
			R resp = onError(cmdMsg, ex);
			return resp;
		} 
	}
	
	abstract protected <R> R doExecuteInternal(CommandMessage cmdMsg);

	
	protected void preExecute(CommandMessage cmdMsg) {
		publishEvent(cmdMsg, ProcessExecutorEvents.pre);
	}
	
	protected <R> void postExecute(CommandMessage cmdMsg, R resp) {
		publishEvent(cmdMsg, ProcessExecutorEvents.post);
	}
	
	protected <R> R onError(CommandMessage cmdMsg, Exception ex) 
	throws FrameworkRuntimeException {
		if(ex instanceof FrameworkRuntimeException) {
			throw (FrameworkRuntimeException)ex;
		}
		
		FrameworkRuntimeException prtEx = new FrameworkRuntimeException("Failed to execute cmdMsg: "+ cmdMsg, ex);
		throw prtEx;
	}

	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		CommandMessage clonedMsg = cmdMsg.clone();
		clonedMsg.getCommand().setEvent(e.code);
		
		eventPublisher.publishEvent(clonedMsg);
	}

	
	protected QuadModel<?, ?> findQuad(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmd);
		return q;
	}
	

	protected Object convert(CommandMessage cmdMsg, Param<Object> param) {
		Class<?> referredClass = param.getConfig().getReferredClass();
		Object model = converter.convert(referredClass, cmdMsg);
		return model;
	}
	
	
	@SuppressWarnings("unchecked")
	protected <R> R crawlCommand(CommandMessage cmdMsg) {
		Command cmd = cmdMsg.getCommand();
		if(cmd.getAction()==Action._new && !cmd.getRootDomainElement().hasRefId()) {
			UserEndpointSession.setAttribute(cmd, null);
		}
		
		Object cached = UserEndpointSession.getAttribute(cmd);
		if(cached!=null) return (R)cached;
		
		
		if(!cmd.isView()) { //core
			return null;
			//==throw new UnsupportedOperationException("Core domain handling not yet supported - cmd: "+cmd);
		}
		
		//view model based processing: 1. Create quad instance - _new: just create, all others: if backing core is present look to retrieve it based on refId
		
		//check if there is a backing core model. If yes: create quad model backed by core by saving an empty entity in db. Else: just create quad
		
		/* create command for Quad model loaded purpose only */
		CommandMessage qLoadCmdMsg = cmdMsg.clone();
		qLoadCmdMsg.getCommand().getRootDomainElement().detachChildElements();
		
		// Changed the default get/new to return target object instead of quad model
		Object obj = cmd.getRootDomainElement().hasRefId() ? defaultGetExec.doExecuteInternal(qLoadCmdMsg): defaultNewExec.doExecuteInternal(qLoadCmdMsg);
		
		// getting the quad model for key = root domain alias minus the ref id (if ref id is present in the url).
		// using currentQModel to then retrieve the flowstate while building new quad model. that way we do not loose the flowstate between quad models
		QuadModel<?,?> currentQModel = UserEndpointSession.getAttribute("/"+cmd.getRootDomainAlias());
		
		QuadModel<?, ?> q; 
		if(currentQModel == null) {
			//q = qBuilder.build(cmd, (cConfig)->obj);
			ExecutionEntity<?, Object> e = new ExecutionEntity<>();
			e.setCore(obj);
			
			q = qBuilder.build(cmd, e);
		}
		else{
			//q = qBuilder.build(cmd, (cConfig)->obj,null,(fConfig)->currentQModel.getFlow().getState());
			q = qBuilder.build(cmd, currentQModel.getRoot().getState());
		}
		
		UserEndpointSession.setAttribute(qLoadCmdMsg.getCommand(), q);
		q.getRoot().fireRules(); //TODO verify if it is ok to run rules in defaultGet ??
		
		//QuadModel<?, ?> q = cmd.getRootDomainElement().hasRefId() ? defaultGetExec.doExecuteInternal(qLoadCmdMsg) : defaultNewExec.doExecuteInternal(qLoadCmdMsg);
		UserEndpointSession.setAttribute(cmd, q);

		return (R)q;
		
		
		//check if new instance is required
//		if(StringUtils.isEmpty(cmd.getDomainElement().getRefId())) {
//			ViewModelConfig<?, ?> vmConfig = quadModelBuilder.findViewConfig(cmd);
//		}
		
		//check what model needs to be instantiated
		
	}
	
	protected <T,R,H extends FunctionHandler<T,R>> R doExecuteFunctionHandler(CommandMessage cmdMsg,Class<H> handlerClass) {
		QuadModel<?, ?> q = findQuad(cmdMsg);
		
		//TODO: Load action parameter based on Command
		Param<T> actionParameter = null;
		ExecutionContext executionContext = new ExecutionContext(cmdMsg,q);
		
		H processHandler = getHandler(cmdMsg, handlerClass);
		return processHandler.execute(executionContext, actionParameter);
	}	
	
	protected <T extends FunctionHandler<?, ?>> T getHandler(CommandMessage commandMessage, Class<T> handlerClass){
		String functionName = commandMessage.getCommand().getFirstParameterValue(Constants.KEY_FUNCTION.code);
		return getHandler(functionName, handlerClass);
	}	
	
	protected <T extends FunctionHandler<?, ?>> T getHandler(String functionName, Class<T> handlerClass){
		return appCtx.getBean(functionName, handlerClass);
	}		
	
}
