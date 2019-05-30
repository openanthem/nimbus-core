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

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.AbstractStateAndConfigEventListener;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

/**
 * @author Rakesh Patel
 *
 */
// This class is specific to task and create other classes for any other listener for same param update
@EnableLoggingInterceptor
public class ParamUpdateEventListener extends AbstractStateAndConfigEventListener {

	@Autowired MongoOperations mongoOps;
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(StringUtils.contains(event.getPayload().getPath(), 
				"/cmcase/status") && StringUtils.equalsIgnoreCase("Cancelled", (String)event.getPayload().getState())) {
			
			Long entityId = (Long)event.getPayload().getRootDomain().findParamByPath("/id").getState();
			
			mongoOps.updateMulti(
					new Query(Criteria.where("status").is("Open").and("entityId").is(entityId)), 
					new Update().set("status", "Cancelled"), 
					"assignmenttask");
			
			// - /p/assignmenttask/_search?fn=query&where=status.eq('open')
					// - /p/assignemnttask/_process?fn=_set&
		}
		
		return true;
	}


	@Override
	public boolean containsListener(ListenerType listenerType) {
		return listenerType == ListenerType.update;
	}

}
