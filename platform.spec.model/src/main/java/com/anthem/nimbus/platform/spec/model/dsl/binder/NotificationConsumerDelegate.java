/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

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
