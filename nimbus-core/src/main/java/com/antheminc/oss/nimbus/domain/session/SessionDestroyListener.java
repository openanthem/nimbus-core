package com.antheminc.oss.nimbus.domain.session;

import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.SessionDestroyedEvent;
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

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.state.repo.DefaultDomainLockStrategy;

import lombok.Getter;

/**
 * @author Sandeep Mantha
 *
 */
@Getter
public class SessionDestroyListener implements ApplicationListener<SessionDestroyedEvent> {

	private final SessionProvider sessionProvider;
	
	private final DefaultDomainLockStrategy domainLockStrategy;

	public SessionDestroyListener(BeanResolverStrategy beanResolver) {
		this.sessionProvider = beanResolver.get(SessionProvider.class);
		this.domainLockStrategy = beanResolver.get(DefaultDomainLockStrategy.class);
	}
	
	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		getSessionProvider().clear();
		domainLockStrategy.releaseLock();
	}


}
