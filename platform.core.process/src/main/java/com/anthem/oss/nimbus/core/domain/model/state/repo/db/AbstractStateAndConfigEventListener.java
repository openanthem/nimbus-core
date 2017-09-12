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
 * This is an abstract implementation of event listener of type {@link StateAndConfigEventListener}
 * Concrete implementation MUST extend this class. e.g {@link ParamStateAtomicPersistenceEventListener}
 * 
 * @author Rakesh Patel
 *
 */
public abstract class AbstractStateAndConfigEventListener implements StateAndConfigEventListener {

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		
		Domain currentDomain = AnnotationUtils.findAnnotation(p.getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			currentDomain = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Domain.class);
		
		if(currentDomain == null)
			return false;
		
		Model pModel = AnnotationUtils.findAnnotation(p.getRootDomain().getConfig().getReferredClass(), Model.class);
		
		ListenerType includeListener = Arrays.asList(currentDomain.includeListeners())
				.stream()
				.filter((listener) -> !Arrays.asList(pModel.excludeListeners()).contains(listener))
				.filter((listenerType) -> containsListener(listenerType))
				.findFirst()
				.orElse(null);
		
		if(includeListener == null)
			return false;
		
		return true;
	}
	
	@Override
	public abstract boolean listen(ModelEvent<Param<?>> event);
	
	public abstract boolean containsListener(ListenerType listenerType);
}
