/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.repository;

import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateRepository {

	public <P> P _get(StateAndConfig.Param<P> param);
	
	public <P> void _set(StateAndConfig.Param<P> param, P newState);
}
