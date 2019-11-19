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
package com.antheminc.oss.nimbus.entity.lock;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.Value;

import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.event.LockableHandlers.OnRootParamLockHandler;
import com.antheminc.oss.nimbus.domain.model.state.event.LockableHandlers.OnRootParamUnlockHandler;
import com.antheminc.oss.nimbus.domain.model.state.repo.LockException;
import com.antheminc.oss.nimbus.entity.DomainEntityLock;

/**
 * @author Sandeep Mantha
 *
 */
public abstract class AbstractLockService implements DomainEntityLockService {

	abstract DomainEntityLock<?> createLockInternal(Param<?> p);
	
	abstract void removeLockInternal(String key);
	
	abstract String getSessionId();
	
	@Value("${lock.db:rep_mongodb}")
	String repo;
	
	@Override
	public DomainEntityLock<?> createLock(Param<?> p) {
		DomainEntityLock<?> domainEntityLock = createLockInternal(p);
		
		// hook up lock events
		if(!domainEntityLock.getSessionId().equals(getSessionId())) {
			onLockEvent(p, domainEntityLock);
			throw new LockException("Domain entity: "+ domainEntityLock.getDomain() + " locked in session: "+ domainEntityLock.getSessionId());
		}		
		return domainEntityLock;
	}

	@Override
	public void removeLock(String key) {
		removeLockInternal(key);
		//onUnlockEvent();
		
	}

	protected static void onLockEvent(Param<?> actionParam, DomainEntityLock<?> domainEntityLock) {
		EventHandlerConfig eventHandlerConfig = actionParam.getRootDomain().getConfig().getEventHandlerConfig();
		if (eventHandlerConfig != null && eventHandlerConfig.getOnRootParamLockAnnotations() != null) {
			eventHandlerConfig.getOnRootParamLockAnnotations().stream().forEach(ac -> {
				OnRootParamLockHandler<Annotation> handler = eventHandlerConfig.getOnRootParamLockHandler(ac);
				if (handler.shouldAllow(ac, actionParam)) {
					handler.handleOnRootParamLock(ac, actionParam, domainEntityLock);
				}
			});
		}
	}
	
	protected static void onUnlockEvent(Param<?> actionParam) {
		EventHandlerConfig eventHandlerConfig = actionParam.getRootDomain().getConfig().getEventHandlerConfig();
		if (eventHandlerConfig != null && eventHandlerConfig.getOnRootParamUnlockAnnotations() != null) {
			eventHandlerConfig.getOnRootParamUnlockAnnotations().stream().forEach(ac -> {
				OnRootParamUnlockHandler<Annotation> handler = eventHandlerConfig.getOnRootParamUnlockHandler(ac);
				if (handler.shouldAllow(ac, actionParam)) {
					handler.handleOnRootParamUnlock(ac, actionParam);
				}
			});
		}
	}
}
