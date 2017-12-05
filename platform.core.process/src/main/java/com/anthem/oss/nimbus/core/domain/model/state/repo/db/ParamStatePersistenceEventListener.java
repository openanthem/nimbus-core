/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo.db;

import java.util.Arrays;

import org.springframework.core.annotation.AnnotationUtils;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * This is an abstract implementation of persistence event listener of type {@link StateAndConfigEventListener}
 * Concrete persistence listener implementation MUST extend this class. e.g {@link ParamStateAtomicPersistenceEventListener}
 * 
 * @author Rakesh Patel
 *
 */
public abstract class ParamStatePersistenceEventListener extends AbstractStateAndConfigEventListener {

	@Override
	public boolean containsListener(ListenerType listenerType) {
		return ListenerType.persistence == listenerType;
	}
	
}
