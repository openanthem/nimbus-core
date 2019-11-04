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

import org.springframework.data.mongodb.core.MongoOperations;

import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.DomainEntityLock;
import com.antheminc.oss.nimbus.entity.LockEntity;

import lombok.RequiredArgsConstructor;

/**
 * @author Sandeep Mantha
 *
 */
@RequiredArgsConstructor
public class DefaultMongoDomainLockProvider extends AbstractLockService implements DomainEntityLockService{

	private final MongoOperations mongoOps;
	
	private final CommandExecutorGateway commandExecutorGateway;
	
	@Override
	public LockEntity getLock(Param<?> p) {
		return null;
//		mongoOps.findById(id, entityClass, collectionName)
//		Command cmd = CommandBuilder.withUri(marker+"/lock/_search?fn=query&where=lock.domain.eq('"+eCtx.toString() +"')").getCommand();
//		MultiOutput o = commandExecutorGateway.execute(new CommandMessage(cmd, null));
//		p = (List<LockEntity>) o.getOutputs().get(0).getValue();
//		if(p)
//		return p;
	}

	@Override
	public DomainEntityLock<?> createLockInternal(Param<?> p) {
		// TODO Auto-generated method stub
		// mongo insert
		return null;
	}

	@Override
	void removeLockInternal(Param<?> p) {
		// TODO Auto-generated method stub
	}

}



