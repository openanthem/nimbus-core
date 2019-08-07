/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.repo;

import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamStateGateway extends ParamStateRepository {

	public <T> T getValue(ValueAccessor pd, Object target);	
	public <T> void setValue(ValueAccessor pd, Object target, T value);
	public <T> T instantiate(Class<T> clazz);
	
	/**
	 * <p>Remove the state of the {@code param}. <p>This implementation will
	 * simply sever the connection between the associated object stored in the
	 * {@code param} state. If the state of {@code param} is an instance of
	 * {@link Object}, then the state is nullified, allowing the garbage
	 * collector to take over. If the state of {@code param} is a primitive
	 * type, then the state is reset to it's default value (as defined by the
	 * JVM for the provided primitive type).
	 * @param param the {@link Param} instance to uninstantiate
	 */
	public <T> void uninstantiate(Param<T> param);
	
	default <M> M _instantiateOrGet(Param<M> param) {
		M existing = _getRaw(param);
		return (existing==null) ? _instantiateAndSet(param) : existing;
	}
	public <M> M _instantiateAndSet(Param<M> param);
	
	public <P> P _getRaw(Param<P> param);
	public <P> void _setRaw(Param<P> param, P newState);
}
