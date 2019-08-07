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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.internal.AbstractListPaginatedParam.PageWrapper.PageRequestAndRespone;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter
@EnableLoggingInterceptor
public class SetFunctionHandler <T,S> extends URLBasedAssignmentFunctionHandler<T,Void,S> {

	
	@SuppressWarnings("unchecked")
	@Override
	public Void assign(ExecutionContext executionContext, Param<T> actionParameter, Param<S> targetParameter, S state) {

		if (state instanceof PageRequestAndRespone && targetParameter.isCollection()) {
			ListParam<S> listParam = targetParameter.findIfCollection();
			PageRequestAndRespone<S> page = (PageRequestAndRespone<S>)state;
			
			listParam.setPage(page.getContent(), page.getPageable(), page.getTotalSupplier());
		}else {
			targetParameter.setState(state);
		}
		return null;
	}
	
}
