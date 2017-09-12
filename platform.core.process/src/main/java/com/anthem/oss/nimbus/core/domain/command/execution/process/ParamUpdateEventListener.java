package com.anthem.oss.nimbus.core.domain.command.execution.process;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.AbstractStateAndConfigEventListener;

/**
 * @author Rakesh Patel
 *
 */
// This class is specific to task and create other classes for any other listener for same param update
public class ParamUpdateEventListener extends AbstractStateAndConfigEventListener {

	@Autowired MongoOperations mongoOps;
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(StringUtils.contains(event.getPayload().getPath(), 
				"/cmcase/status") && StringUtils.equalsIgnoreCase("Cancelled", (String)event.getPayload().getState())) {
			
			String entityId = (String)event.getPayload().getRootDomain().findParamByPath("/id").getState();
			
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
