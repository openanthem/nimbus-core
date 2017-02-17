/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain.event;

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
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelPersistenceHandler;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepositoryFactory;
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
		return !p.getConfig().isMapped();		
	}
	
	@Override
	public boolean publish(ModelEvent<Param<?>> event) {
		List<ModelEvent<Param<?>>> events = new ArrayList<>();
		events.add(event);
			
		Param<?> p = (Param<?>) event;
		Repo repo = p.getRootModel().getConfig().getRepo();
		if(repo==null) {
			throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+event.getPayload().getRootModel());
		} 
			
		ModelPersistenceHandler handler = repoFactory.getHandler(p.getRootModel().getConfig().getRepo());
		
		if(handler == null) {
			throw new InvalidConfigException("There is no repository handler provided for the configured repository :"+repo.value().name()+ " for root model: "+event.getPayload().getRootModel());
		}
		
		return handler.handle(events);
		
	}

}
