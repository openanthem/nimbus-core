/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.contract.event.StateAndConfigEventPublisher;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.PersistenceMode;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(exceptionIfInvalid=true,prefix="model.persistence.strategy")
public class ParamStateBatchPersistenceEventPublisher implements StateAndConfigEventPublisher {

	@Autowired
	ModelRepositoryFactory repoFactory;
	
	@Getter @Setter
	List<ModelEvent<StateAndConfig<?,?>>> modelEvents;
	
//	@Autowired
//	@Qualifier("rep_mongodb_handler")
//	ModelPersistenceHandler handler;
	
	@Getter @Setter
	private PersistenceMode mode;
	
	
	@Override
	public boolean shouldAllow(StateAndConfig<?,?> p) {
		if(p instanceof Param<?>) {
			Param<?> param = (Param<?>) p;
			return !param.getConfig().isView() && PersistenceMode.BATCH == mode;
		}
		else {
			Model<?,?> param = (Model<?,?>) p;
			return !param.getConfig().isView() && PersistenceMode.BATCH == mode;
		}
	}
	
	@Override
	public boolean publish(ModelEvent<StateAndConfig<?,?>> event) {
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
		Map<Repo, List<ModelEvent<StateAndConfig<?,?>>>> repoModelMap = new HashMap<>();
		
		for(ModelEvent<StateAndConfig<?,?>> me: this.getModelEvents()) {
			
			StateAndConfig<?, ?> sac = me.getPayload();
			Repo repo;
			
			if(sac instanceof Param<?>) {
				repo= ((Param<?>)sac).getRootParent().getConfig().getRepo();
			}
			else{
				repo= ((Model<?,?>)sac).getRootParent().getConfig().getRepo();
			}
			
			if(repo == null) continue; //TODO should throw an error ?
			
			if(CollectionUtils.isEmpty(repoModelMap.get(repo))) {
				repoModelMap.put(repo, new ArrayList<ModelEvent<StateAndConfig<?,?>>>());
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
