/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(exceptionIfInvalid=true,prefix="model.persistence.strategy")
public class ParamStateAtomicPersistenceEventPublisher { //implements StateAndConfigEventPublisher {

	@Autowired
	ModelRepositoryFactory repoFactory;

	@Getter @Setter
	private PersistenceMode mode;
	
//	@Autowired
//	@Qualifier("rep_mongodb_handler")
//	ModelPersistenceHandler handler;

	//@Override
//	public boolean shouldAllow(EntityState<?> p) {
//		if(p instanceof Param<?>) {
//			Param<?> param = (Param<?>) p;
//			return !param.getConfig().isView() && PersistenceMode.ATOMIC == mode;
//		}
//		else {
//			Model<?> param = (Model<?>) p;
//			return !param.getConfig().isView() && PersistenceMode.ATOMIC == mode;
//		}
//	}
//	
	//@Override
//	public boolean publish(ModelEvent<EntityState<?>> event) {
//		List<ModelEvent<EntityState<?>>> events = new ArrayList<>();
//		events.add(event);
//		
//		EntityState<?> param = event.getPayload();
//		
//		if(param instanceof Param<?>) {
//			Param<?> p = (Param<?>) param;
//			Repo repo = p.getRootModel().getConfig().getRepo();
//			if(repo==null) {
//				throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+event.getPayload().getRootParent());
//			} 
//				
//			ModelPersistenceHandler handler = repoFactory.getHandler(p.getRootModel().getConfig().getRepo());
//			
//			if(handler == null) {
//				throw new InvalidConfigException("There is no repository handler provided for the configured repository :"+repo.value().name()+ " for root model: "+event.getPayload().getRootParent());
//			}
//			
//			return handler.handle(events);
//		}
//		else{
//			Model<?> p = (Model<?>) param;
//			
//			Repo repo = p.getRootModel().getConfig().getRepo();
//			if(repo==null) {
//				throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+event.getPayload().getRootParent());
//			} 
//				
//			ModelPersistenceHandler handler = repoFactory.getHandler(p.getRootModel().getConfig().getRepo());
//			
//			if(handler == null) {
//				throw new InvalidConfigException("There is no repository handler provided for the configured repository :"+repo.value().name()+ " for root model: "+event.getPayload().getRootParent());
//			}
//			
//			return handler.handle(events);
//		}
//		
//		
//	}
//
//	@Override
//	public boolean publish(ModelEvent<Param<?>> event) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
