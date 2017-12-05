/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateRepository {

	public <P> P _get(EntityState.Param<P> param);
	
	public <P> Action _set(EntityState.Param<P> param, P newState);
}
