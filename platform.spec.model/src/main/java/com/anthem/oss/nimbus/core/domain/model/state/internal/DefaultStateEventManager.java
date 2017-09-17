/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.StateEventManager;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class DefaultStateEventManager implements StateEventManager {

	@Override
	public void dispatchNotifications(Queue<Notification<Object>> notifications) {
		while(!notifications.isEmpty()) {
			Notification<Object> event = notifications.poll();
			Param<Object> source =  event.getSource();
			
			if(CollectionUtils.isNotEmpty(source.getEventSubscribers()))
				new ArrayList<>(source.getEventSubscribers())
					.stream()
						.forEach(subscribedParam->subscribedParam.handleNotification(event));
		}
	}
	
	@Override
	public void dispatchEvents(Queue<ParamEvent> events) {
		// TODO Auto-generated method stub
		
	}
}
