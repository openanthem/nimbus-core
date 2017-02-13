/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateRepository {

	public <P> P _get(DomainState.Param<P> param);
	
	public <P> Action _set(DomainState.Param<P> param, P newState);
}
