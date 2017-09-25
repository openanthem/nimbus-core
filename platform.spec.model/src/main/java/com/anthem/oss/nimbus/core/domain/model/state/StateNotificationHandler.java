/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.lang.annotation.Annotation;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface StateNotificationHandler<A extends Annotation> {

	public void handle(Param<?> onChangeParam, Action action, A configuredAnnotation);
}
