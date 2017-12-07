package com.antheminc.oss.nimbus.core.domain.model.state.repo.db;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent.PersistenceMode;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.ModelPersistenceHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.ModelRepositoryFactory;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 * @author Rakesh Patel
 */
@ConfigurationProperties(exceptionIfInvalid=true,prefix="model.persistence.strategy")
public class ParamStateAtomicPersistenceEventListener extends ParamStatePersistenceEventListener {

	ModelRepositoryFactory repoFactory;

	@Getter @Setter
	private PersistenceMode mode;
	
	ModelPersistenceHandler handler;

	public ParamStateAtomicPersistenceEventListener(ModelRepositoryFactory repoFactory, ModelPersistenceHandler handler) {
		this.repoFactory = repoFactory;
		this.handler = handler;
	}
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return super.shouldAllow(p) && PersistenceMode.ATOMIC == mode;
	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		Param<?> p = (Param<?>) event.getPayload();
		
		Repo repo = getParamRepo(p);
		
		if(repo == null) {
			repo = p.getRootDomain().getConfig().getRepo();
		}
		
		if(repo==null) {
			throw new InvalidConfigException("Core Persistent entity must be configured with "+Repo.class.getSimpleName()+" annotation. Not found for root model: "+p.getRootExecution());
		} 
		
		ModelPersistenceHandler handler = repoFactory.getHandler(repo);
		
		if(handler == null) {
			throw new InvalidConfigException("There is no repository handler provided for the configured repository :"+repo.value().name()+ " for root model: "+p.getRootExecution());
		}
		
		Param<?> paramToPersist = returnNestedOrSelf(p);
		
		List<ModelEvent<Param<?>>> events = new ArrayList<>();
		ModelEvent<Param<?>> modelEvent = new ModelEvent<>(Action.getByName(event.getType()), paramToPersist.getPath(), paramToPersist);
		events.add(modelEvent);
		
		return handler.handle(events);
		
	}
	
	private Repo getParamRepo(Param<?> param) {
		return param.isNested() && param.findIfNested().getConfig().getRepo() != null 
				? param.findIfNested().getConfig().getRepo() 
				: param.getRootDomain().getConfig().getRepo();
	}
	
	private Param<?> returnNestedOrSelf(Param<?> param) {
		if(param.isNested() ) {
			Model<Object> model = (Model<Object>)param.findIfNested();
			Class<Object> modelClass = (Class<Object>)model.getConfig().getReferredClass();
			
			Repo repo = AnnotationUtils.findAnnotation(modelClass, Repo.class);
			Domain domain = AnnotationUtils.findAnnotation(modelClass, Domain.class);
			
			if(repo != null && domain != null) {
				return param;
			}
		}
		return param.getRootDomain().getAssociatedParam();
	}

}
