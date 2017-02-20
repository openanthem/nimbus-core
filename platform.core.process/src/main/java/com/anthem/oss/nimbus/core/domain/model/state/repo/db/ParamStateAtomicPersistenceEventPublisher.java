/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.PersistenceMode;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventPublisher;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(exceptionIfInvalid=true,prefix="model.persistence.strategy")
public class ParamStateAtomicPersistenceEventPublisher implements StateAndConfigEventPublisher {

	@Autowired
	ModelRepositoryFactory repoFactory;

	@Getter @Setter
	private PersistenceMode mode;
	
	@Autowired
	@Qualifier("rep_mongodb_handler")
	ModelPersistenceHandler handler;

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		Repo repo = p.getRootDomain().getConfig().getRepo();
		if(repo == null)
			return false;
		return true;
	}
	
	@Override
	public boolean publish(ModelEvent<Param<?>> event) {
		List<ModelEvent<Param<?>>> events = new ArrayList<>();
		events.add(event);
			
		Param<?> p = (Param<?>) event.getPayload();
		Repo repo = p.getRootDomain().getConfig().getRepo();
		if(repo==null) {
			throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+p.getRootExecution());
		} 
			
		ModelPersistenceHandler handler = repoFactory.getHandler(repo);
		
		if(handler == null) {
			throw new InvalidConfigException("There is no repository handler provided for the configured repository :"+repo.value().name()+ " for root model: "+p.getRootExecution());
		}
		
		return handler.handle(events);
		
	}

}
