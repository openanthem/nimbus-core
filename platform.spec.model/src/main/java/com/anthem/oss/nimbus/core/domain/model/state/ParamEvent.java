/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data @AllArgsConstructor
public class ParamEvent {

	private Action action;
	
	private Param<?> param;
}
