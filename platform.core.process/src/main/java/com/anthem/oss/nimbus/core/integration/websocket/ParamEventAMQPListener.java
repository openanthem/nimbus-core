package com.anthem.oss.nimbus.core.integration.websocket;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.EventOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandTransactionInterceptor;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.command.execution.MultiExecuteOutput;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.AbstractStateAndConfigEventListener;

/**
 * @author Rakesh Patel
 * 
 */
public class ParamEventAMQPListener extends AbstractStateAndConfigEventListener {

	SimpMessageSendingOperations messageTemplate;

	CommandTransactionInterceptor interceptor;	
	
	//@Autowired
	//WebSocketHttpConnectHandlerDecoratorFactory webSocketHttpConnectHandlerDecoratorFactory;
	
	
	public ParamEventAMQPListener(SimpMessageSendingOperations messageTemplate, CommandTransactionInterceptor interceptor){
		this.messageTemplate = messageTemplate;
		this.interceptor = interceptor;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> in) {
		final EntityState<?> p;
		if(in.getRootExecution().getAssociatedParam().isLinked()) {
			p = in.getRootExecution().getAssociatedParam().findIfLinked();
		} else {
			p = in;
		}
		
		return super.shouldAllow(p);

	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> modelEvent) {
		return listenInternal(modelEvent);
	}
	
	protected boolean listenInternal(ModelEvent<Param<?>> result) {
		//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		EventOutput<Param<?>> eventOutput = new EventOutput<Param<?>>(result.getPayload(), Action.getByName(result.getType()), Behavior.$execute);
		ExecuteOutput<EventOutput<Param<?>>> executeOutput = new ExecuteOutput<>();
		executeOutput.setResult(eventOutput);
		
		MultiExecuteOutput multiExecOutput = interceptor.handleResponse(executeOutput);
		String webSocketSessionId = getAssociatedWebSocketSessionId();
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(webSocketSessionId);		
		//messageTemplate.convertAndSend("/queue/updates", multiExecOutput); // TODO get the destination name from the config server
		messageTemplate.convertAndSendToUser(webSocketSessionId,"/queue/updates", multiExecOutput,headerAccessor.getMessageHeaders(),null);
		return true;
	}
	
	private String getAssociatedWebSocketSessionId(){
		String httpSessionId = RequestContextHolder.getRequestAttributes().getSessionId();
		//return webSocketHttpConnectHandlerDecoratorFactory.getAssociatedWebSocketSessionId(httpSessionId);
		return httpSessionId;
	}

	@Override
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.websocket == listenerType;
	}

}
