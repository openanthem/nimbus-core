/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.PersistenceMode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
//TODO work in progress - not yet tested after framework 2.0
@EnableConfigurationProperties
@ConfigurationProperties(exceptionIfInvalid=true,prefix="model.persistence.strategy")
public class ParamStateBatchPersistenceEventListener extends ParamStatePersistenceEventListener {

	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Getter @Setter
	List<ModelEvent<Param<?>>> modelEvents;

	@Getter @Setter
	private PersistenceMode mode;
	
	public ParamStateBatchPersistenceEventListener(ModelRepositoryFactory repoFactory) {
		this.repoFactory = repoFactory;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return super.shouldAllow(p) && PersistenceMode.BATCH == mode;
	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if(CollectionUtils.isEmpty(modelEvents)){
			modelEvents = new ArrayList<>();
		}
		modelEvents.add(event);
		
		return true;
	}
	
	
	public boolean flush() {
		
		if(CollectionUtils.isEmpty(this.getModelEvents())) 
			return true;
		
		//group the model events per persistence handler
		Map<Repo, List<ModelEvent<Param<?>>>> repoModelMap = new HashMap<>();
		
		for(ModelEvent<Param<?>> me: this.getModelEvents()) {
			
			Param<?> param = me.getPayload();
			Repo repo = param.getRootDomain().getConfig().getRepo();
			
			if(repo == null) continue; //TODO should throw an error ?
			
			if(CollectionUtils.isEmpty(repoModelMap.get(repo))) {
				repoModelMap.put(repo, new ArrayList<ModelEvent<Param<?>>>());
			}
			repoModelMap.get(repo).add(me);
		}
		
		repoModelMap.forEach((r, me) -> {
			ModelPersistenceHandler handler = repoFactory.getHandler(r);
			if(handler == null) return; // TODO should throw an error ?
			handler.handle(me);
		});
		
		getModelEvents().clear();
		return true;
		
	}

}
