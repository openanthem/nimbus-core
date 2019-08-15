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

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.RefIdHolder;

/**
 * @author Rakesh Patel
 *
 */
public interface ExternalModelRepository extends ModelRepository {

	default public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> RefIdHolder<T> _new(Command cmd, ModelConfig<T> mConfig, T newState) {
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _save(String alias, T state) {
		return state;
	}
	
	@Override
	default void _save(Param<?> param) {
		throw new UnsupportedOperationException("_update operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _update(Param<?> param, T state){
		throw new UnsupportedOperationException("_update operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _delete(Param<?> param){
		throw new UnsupportedOperationException("_delete operation is not supported for Database.rep_ws repository");
	}
	
}
