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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.EmitUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.config.builder.internal.DefaultEntityConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.QuerySearchCriteria;
import com.antheminc.oss.nimbus.domain.session.SessionProvider;
import com.antheminc.oss.nimbus.entity.DomainEntityLock;
import com.antheminc.oss.nimbus.entity.LockEntity;

import lombok.RequiredArgsConstructor;

/**
 * @author Sandeep Mantha
 *
 */
public class DefaultDomainLockProvider extends AbstractLockService implements DomainEntityLockService{

	private final ModelRepositoryFactory modelrepo;
	
	private final SessionProvider sessionProvider;
	
	private final DomainConfigBuilder domainConfigBuilder;
	
	public DefaultDomainLockProvider(BeanResolverStrategy beanResolverStrategy) {
		this.modelrepo = beanResolverStrategy.find(ModelRepositoryFactory.class);
		this.sessionProvider = beanResolverStrategy.find(SessionProvider.class);
		this.domainConfigBuilder = beanResolverStrategy.find(DomainConfigBuilder.class);
	}

	@Override
	public DomainEntityLock getLock(Param<?> p) {
		//domainConfigBuilder.getModel(LockEntity.class);
		ModelRepository modelrepository = modelrepo.get(Repo.Database.rep_mongodb);
		QuerySearchCriteria qs = new QuerySearchCriteria();
		String refId = String.valueOf(p.getRootExecution().getRootCommand().getRefId(Type.DomainAlias));
		qs.setWhere("lock.domain.eq('/"+ p.getConfig().getCode()+":"+refId+"')");
		List<LockEntity> ls =  (List<LockEntity>) modelrepository._search(LockEntity.class, "lock", () -> qs);
		return ls.size() == 1? ls.get(0) : null;
	}

	public DomainEntityLock<?> createLockInternal(Param<?> p) {
		ModelRepository modelrepository = modelrepo.get(Repo.Database.rep_mongodb);
		return modelrepository._save("lock", createLockEntity(p));
	}

	private DomainEntityLock<?> createLockEntity(Param<?> p) {
		LockEntity le = new LockEntity();
		le.setDomain("/"+p.getConfig().getCode()+":"+p.getRootExecution().getRootCommand().getRefId(Type.DomainAlias));
		le.setSessionId(sessionProvider.getSessionId());
		le.setLockedBy(sessionProvider.getLoggedInUser().getLoginId());
		return le;
	}

	@Override
	void removeLockInternal(Param<?> p) {}
	
	@Override
	void removeLockInternal() {
		ModelRepository modelrepository = modelrepo.get(Repo.Database.rep_mongodb);
		QuerySearchCriteria qs = new QuerySearchCriteria();
		qs.setWhere("lock.sessionId.eq('"+ sessionProvider.getSessionId()+"')");
		List<LockEntity> ls =  (List<LockEntity>) modelrepository._search(LockEntity.class, "lock", () -> qs);
		if(ls.size() == 1) {
			modelrepository._delete(ls.get(0).getId(), LockEntity.class, "lock");
		}
	}
}



