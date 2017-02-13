/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateGateway extends ParamStateRepository {

	default <M> M _instantiateOrGet(Param<M> param) {
		M existing = _getRaw(param);
		return (existing==null) ? _instantiateAndSet(param) : existing;
	}
	public <M> M _instantiateAndSet(Param<M> param);
	
	public <P> P _getRaw(Param<P> param);
	public <P> void _setRaw(Param<P> param, P newState);
}
