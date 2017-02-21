package com.anthem.oss.nimbus.core.integration.websocket;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * @author Rakesh Patel
 * 
 */

@Component
public class ParamEventAMQPListener implements StateAndConfigEventListener {

	@Autowired
	SimpMessagingTemplate messageTemplate;
	
	@Autowired CommandTransactionInterceptor interceptor;	
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		if(rootDomain == null) 
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(rootDomain.includeListeners()).stream()
											.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
											.filter((listenerType) -> listenerType == ListenerType.websocket)
											.findFirst()
											.orElse(null);
		
		if(includeListener == null)
			return false;
		
		//Repo repo = p.getRootDomain().getConfig().getRepo();
		//if(repo == null)
		//		return false;
		return true;

	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> modelEvent) {
		Param<?>  p = modelEvent.getPayload();		
//		//TODO temp impl for GRID collection handling
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
//
		return listenInternal(modelEvent);
	}
	
	protected boolean listenInternal(ModelEvent<Param<?>> result) {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		ExecuteOutput<ModelEvent<?>> executeOutput = new ExecuteOutput<>();
		executeOutput.setResult(result);
		
		MultiExecuteOutput multiExecOutput = interceptor.handleResponse(executeOutput);
		
		messageTemplate.convertAndSend("/queue/updates", multiExecOutput); // TODO get the destination name from the config server
		
		//messageTemplate.convertAndSendToUser(auth.getName(),"/queue/updates", executeOutput);
		
		return true;
	}

}
