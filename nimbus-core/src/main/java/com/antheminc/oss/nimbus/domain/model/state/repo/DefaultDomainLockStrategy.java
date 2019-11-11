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

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.DomainEntityLock;

/**
 * @author Sandeep Mantha
 *
 */
public class DefaultDomainLockStrategy implements DomainEntityLockStrategy {

	private DomainEntityLockService domainEntityLockProvider;

	private SessionProvider sessionProvider;
	
	public DefaultDomainLockStrategy(BeanResolverStrategy beanResolverStrategy) {
		this.domainEntityLockProvider = beanResolverStrategy.find(DomainEntityLockService.class);
		this.sessionProvider = beanResolverStrategy.find(SessionProvider.class);
	}

	@Override
	public void evalAndapply(Param<?> param) {

		DomainEntityLock<?> lock = domainEntityLockProvider.getLock(param);
		if (lock != null) {
			if (!sessionProvider.getSessionId().equals(lock.getSessionId())) {
				throw new FrameworkRuntimeException(
						"Domain locked for the absolute uri " + param.getRootExecution().getRootCommand().getAbsoluteUri());
			}
		} else {
			domainEntityLockProvider.createLock(param);

		}

	}
		
	@Override
	public void releaseLock() {
		domainEntityLockProvider.removeLock();
	}

}
