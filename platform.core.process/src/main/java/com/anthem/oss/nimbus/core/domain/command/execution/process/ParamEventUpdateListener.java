package com.anthem.oss.nimbus.core.domain.command.execution.process;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * @author Rakesh Patel
 *
 */
public class ParamEventUpdateListener implements StateAndConfigEventListener {

	@Autowired MongoOperations mongoOps;
	
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		if(rootDomain == null) 
			return false;
		
		ListenerType includeListener = Arrays.asList(rootDomain.includeListeners()).stream()
											.filter((listenerType) -> listenerType == ListenerType.update)
											.findFirst()
											.orElse(null);
		
		if(includeListener == null)
			return false;
		
		return true;
	}
	
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(StringUtils.contains(event.getPayload().getPath(), 
				"/cmcase/status") && StringUtils.equalsIgnoreCase("Cancelled", (String)event.getPayload().getState())) {
			
			String entityId = (String)event.getPayload().getRootDomain().findParamByPath("/id").getState();
			
			mongoOps.updateMulti(new Query(Criteria.where("status").is("Open").and("entityId").is(entityId)), new Update().set("status", "Cancelled"), "assignmenttask");
		}
		return true;
	}

}
