/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.repository;

import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateRepository {

	public <P> P _get(DomainState.Param<P> param);
	
	public <P> Action _set(DomainState.Param<P> param, P newState);
}
