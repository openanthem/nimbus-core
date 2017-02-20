/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.definition.Model;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * This is an abstract implementation of persistence event listener of tyep {@link StateAndConfigEventListener}
 * Concrete implementation MUST extend this class. e.g {@link ParamStateAtomicPersistenceEventListener}
 * 
 * @author AC67870
 *
 */
public abstract class ParamStatePersistenceEventListener implements StateAndConfigEventListener {

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		Domain rootDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		if(rootDomain == null) 
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(rootDomain.includedListeners()).stream()
											.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
											.filter((listenerType) -> listenerType == ListenerType.persistence)
											.findFirst()
											.orElse(null);
		
		if(includeListener == null)
			return false;
		
		//Repo repo = p.getRootDomain().getConfig().getRepo();
		//if(repo == null)
		//		return false;
		return true;
	}
	
	@Override
	public abstract boolean listen(ModelEvent<Param<?>> event);
}
