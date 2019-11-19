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

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.DomainEntityLock;

/**
 * @author Sandeep Mantha
 *
 */
public class DefaultDomainLockProvider extends AbstractLockService implements DomainEntityLockService{

	private final ModelRepositoryFactory modelrepo;
	
	private final SessionProvider sessionProvider;
	
	public DefaultDomainLockProvider(BeanResolverStrategy beanResolverStrategy) {
		this.modelrepo = beanResolverStrategy.find(ModelRepositoryFactory.class);
		this.sessionProvider = beanResolverStrategy.find(SessionProvider.class);
	}
	
	public DomainEntityLock<?> createLockInternal(Param<?> p) {
		ModelRepository modelrepository = modelrepo.get(p.getRootDomain().getConfig().getRepo().value());
		DomainEntityLock result = (DomainEntityLock) modelrepository._lock(p);
		return (DomainEntityLock<?>) result;
	}
	
	@Override	
	void removeLockInternal(String key) {
		ModelRepository modelrepository = modelrepo.get(Repo.Database.valueOf(repo));
		modelrepository._unlock(key);
	}

	@Override
	String getSessionId() {
		return this.sessionProvider.getSessionId();
	}
}



