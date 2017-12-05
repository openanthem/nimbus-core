/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import com.antheminc.oss.nimbus.core.domain.model.state.Notification;

/**
 * @author Soham Chakravarti
 *
 */
interface NotificationConsumerDelegate<M> extends Notification.Consumer<M> {

	public Notification.Consumer<M> getDelegate();
	
	@Override
	default void handleNotification(Notification<M> event) {
		getDelegate().handleNotification(event);
	}
}
