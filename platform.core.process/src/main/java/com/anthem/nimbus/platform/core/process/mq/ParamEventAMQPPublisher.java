package com.anthem.nimbus.platform.core.process.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.anthem.nimbus.platform.core.process.api.command.CommandTransactionInterceptor;
import com.anthem.nimbus.platform.spec.contract.event.StateAndConfigEventPublisher;
import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;
import com.anthem.nimbus.platform.spec.model.command.ExecuteOutput;
import com.anthem.nimbus.platform.spec.model.command.MultiExecuteOutput;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.dsl.config.AnnotationConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Grid;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewParamConfig;

/**
 * @author Rakesh Patel
 * 
 */

@Component
public class ParamEventAMQPPublisher implements StateAndConfigEventPublisher {

	@Autowired
	SimpMessagingTemplate messageTemplate;
	
	@Autowired CommandTransactionInterceptor interceptor;
	
	@Override
	public boolean shouldSuppress(SuppressMode mode) {
		return SuppressMode.ECHO == mode;
	}	
	
	@Override
	public boolean shouldAllow(StateAndConfig<?,?> p) {
		if(p instanceof Param<?>) {
			Param<?> param = (Param<?>) p;
			return param.getConfig().isView();
		}
		else {
			Model<?,?> param = (Model<?,?>) p;
			return param.getConfig().isView();
		}
	}
	
	@Override
	public boolean publish(ModelEvent<StateAndConfig<?,?>> modelEvent) {
		StateAndConfig<?,?>  p = modelEvent.getPayload();
		
		//TODO temp impl for GRID collection handling
//		if(p.getConfig() instanceof ViewParamConfig) {
//			AnnotationConfig uiStyle = ((ViewParamConfig)p.getConfig()).getUiStyles();
//	
//			if(uiStyle!=null && ClassUtils.getShortName(Grid.class).equals(uiStyle.getName())) {
//				Object state = p.getState();
//				ModelEvent e = new ModelEvent(Action._replace, p instanceof Param<?> ?((Param<?>)p).getPath():((Model<?,?>)p).getPath(), state);
//				publishInternal(e);
//				return true;
//			}
//	
//		}

		return publishInternal(modelEvent);
	}
	
	protected boolean publishInternal(ModelEvent<?> result) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ExecuteOutput<ModelEvent<?>> executeOutput = new ExecuteOutput<>();
		executeOutput.setResult(result);
		
		MultiExecuteOutput multiExecOutput = interceptor.handleResponse(executeOutput);
		
		messageTemplate.convertAndSend("/queue/updates", multiExecOutput); // TODO get the destination name from the config server
		
		//messageTemplate.convertAndSendToUser(auth.getName(),"/queue/updates", executeOutput); // TODO get the destination name from the config server
		
		return true;
	}

}
