/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.LinkedList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.el.ExpressionManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.bpm.ModelInitializer;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.ProcessGateway;
import com.anthem.oss.nimbus.core.domain.definition.AssociatedEntity;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;
import com.anthem.oss.nimbus.core.utils.ProcessBeanResolver;

public class ModelInstantiationServiceTaskDelegate implements JavaDelegate{
	
	private Expression modelName;
	private Expression modelInitializerType;
	private Expression modelInitializer;
	private ProcessGateway processGateway;
	
	public ModelInstantiationServiceTaskDelegate(ProcessGateway processGateway){
		this.processGateway = processGateway;
	}
	

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void execute(DelegateExecution execution) {
		String modelInitializerType = (String)this.modelInitializerType.getValue(execution);
		String modelName = (String)this.modelName.getValue(execution);
		String modelInitializerExpression = (String)this.modelInitializer.getValue(execution);
		
		ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
		ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
		
		/* Instantiate Model */
		ActivitiContext aCtx = (ActivitiContext) execution.getVariable(ActivitiGateway.PROCESS_ENGINE_GTWY_KEY);
		LinkedList<Behavior> behavior = new LinkedList<Behavior>();
		behavior.add(Behavior.$config);
		Command cmd = aCtx.getProcessEngineContext().getCommandMsg().getCommand().createNewCommandForCurrentUser(modelName,Action._new,behavior);
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		processGateway.startProcess(cmdMsg);
		QuadModel<?, ?> q = UserEndpointSession.getOrThrowEx(cmdMsg.getCommand());
		
		/* Initialize newly created model if applicable */
		Expression expression = expressionManager.createExpression(modelInitializerType);	
		String initiliazerType = (String)expression.getValue(execution);
		if(StringUtils.isNotBlank(initiliazerType)) {
			ModelInitializer modelInitializer = ProcessBeanResolver.getBean(initiliazerType, ModelInitializer.class);
			modelInitializer.initialize(q, modelInitializerExpression);
		}
		
		AssociatedEntity associatedEntity = AnnotationUtils.findAnnotation(q.getCore().getConfig().getReferredClass(), AssociatedEntity.class);
		
		if(associatedEntity != null) {
			Command command = CommandBuilder.withUri("/Anthem/platform/p/"+associatedEntity.value()+"/_get").getCommand();
			command.setAction(Action._get);
			command.templateBehaviors().add(Behavior.$execute);
			QuadModel<?, AbstractEntity.IdString> associatedEntityModel = UserEndpointSession.getOrThrowEx(command);
			if(associatedEntityModel != null) {
				q.getCore().findParamByPath("/entityId").setState(associatedEntityModel.getCore().getState().getId());
			}
		}
		
		
	}
}
