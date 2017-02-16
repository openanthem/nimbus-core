/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.event;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamEvent<T> {

	public Action getType();
	
	public Param<T> getParam();
}
