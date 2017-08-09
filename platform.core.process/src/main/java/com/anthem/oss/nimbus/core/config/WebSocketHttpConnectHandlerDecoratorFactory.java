/**
 * 
 */
package com.anthem.oss.nimbus.core.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.web.socket.events.SessionConnectEvent;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Jayant Chaudhuri
 *
 */
@Slf4j
public class WebSocketHttpConnectHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {

	private JustLogit logit = new JustLogit(this.getClass());
	
	private final ApplicationEventPublisher eventPublisher;
	
	private final Map<String, String> sessions = new ConcurrentHashMap<String, String>();

	/**
	 * Creates a new instance.
	 *
	 * @param eventPublisher the {@link ApplicationEventPublisher} to use. Cannot be null.
	 */
	public WebSocketHttpConnectHandlerDecoratorFactory(
			ApplicationEventPublisher eventPublisher) {
		Assert.notNull(eventPublisher, "eventPublisher cannot be null");
		this.eventPublisher = eventPublisher;
	}
	
	public String getAssociatedWebSocketSessionId(String httpSessionId){
		return sessions.get(httpSessionId);
	}

	public WebSocketHandler decorate(WebSocketHandler handler) {
		return new SessionWebSocketHandler(handler);
	}

	private final class SessionWebSocketHandler extends WebSocketHandlerDecorator {

		SessionWebSocketHandler(WebSocketHandler delegate) {
			super(delegate);
		}

		@Override
		public void afterConnectionEstablished(WebSocketSession wsSession)
				throws Exception {
			super.afterConnectionEstablished(wsSession);
			String httpSessionId = retrieveHttpSessionId(wsSession);
			if(httpSessionId != null){
				sessions.put(httpSessionId, wsSession.getId());
			}
			publishEvent(new SessionConnectEvent(this, wsSession));
		}

		private void publishEvent(ApplicationEvent event) {
			try {
				WebSocketHttpConnectHandlerDecoratorFactory.this.eventPublisher
						.publishEvent(event);
			}
			catch (Throwable ex) {
				log.error("Error publishing " + event + ".", ex);
			}
		}
		
		private String retrieveHttpSessionId(WebSocketSession wsSession){
			return ((org.apache.tomcat.websocket.WsSession)((org.springframework.web.socket.adapter.standard.StandardWebSocketSession)wsSession).getNativeSession()).getHttpSessionId();
		}
	}
}
