/**
 *  Copyright 2016-2018 the original author or authors.
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

import java.io.Serializable;
import java.util.List;

import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Rakesh Patel
 *
 */
public interface ExternalModelRepository extends ModelRepository {

	default public <T> T _new(ModelConfig<T> mConfig){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _new(ModelConfig<T> mConfig, T newState){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	default public <ID extends Serializable, T> T _save(String alias, T state) {
		return state;
	}
	
	default public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias){
		throw new UnsupportedOperationException("_new operation is not supported for Database.rep_ws repository");
	}
	
	//Action._update: partial update
	default public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state){
		throw new UnsupportedOperationException("_update operation is not supported for Database.rep_ws repository");
	}
	
	//Action._replace: complete update
	default public void _replace(Param<?> param){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	default public void _replace(List<Param<?>> params){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	default public <T> T _replace(String alias, T state){
		throw new UnsupportedOperationException("_replace operation is not supported for Database.rep_ws repository");
	}
	
	//Action._delete
	default public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias){
		throw new UnsupportedOperationException("_delete operation is not supported for Database.rep_ws repository");
	}
	
}
