/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import java.lang.reflect.Method;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateGateway extends ParamStateRepository {

	public <T> T getValue(Method readMethod, Object target);	
	public <T> void setValue(Method writeMethod, Object target, T value);
	public <T> T instantiate(Class<T> clazz);

	default <M> M _instantiateOrGet(Param<M> param) {
		M existing = _getRaw(param);
		return (existing==null) ? _instantiateAndSet(param) : existing;
	}
	public <M> M _instantiateAndSet(Param<M> param);
	
	public <P> P _getRaw(Param<P> param);
	public <P> void _setRaw(Param<P> param, P newState);
}
