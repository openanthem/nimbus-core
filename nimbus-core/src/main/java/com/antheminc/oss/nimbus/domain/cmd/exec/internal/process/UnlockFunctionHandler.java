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

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.FunctionHandler;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.lock.DomainEntityLockStrategy;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.Getter;

/**
 * @author Sandeep Mantha
 *
 */
@Getter
@EnableLoggingInterceptor
public class UnlockFunctionHandler implements FunctionHandler<Object, Param<Object>>{

	private DomainEntityLockStrategy domainEntityLockStrategy;
	
	private final SessionProvider sessionProvider;

	public UnlockFunctionHandler(BeanResolverStrategy beanResolver) {
		this.domainEntityLockStrategy = beanResolver.find(DomainEntityLockStrategy.class);
		this.sessionProvider = beanResolver.find(SessionProvider.class);
	}
	
	@Override
	public Param<Object> execute(ExecutionContext eCtx, Param<Object> actionParameter) {
		domainEntityLockStrategy.releaseLock(sessionProvider.getSessionId());
		return null;
	}

}


