/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ModelEvent;
import com.anthem.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class QuadScopedEventListener implements StateAndConfigEventListener {

	private final List<StateAndConfigEventListener> paramEventListeners;
	
	private SuppressMode suppressMode;
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	
	/**
	 * 
	 */
	@Override
	public synchronized void apply(SuppressMode suppressMode) {
		this.suppressMode = suppressMode;
		logit.trace(()->"suppressMode applied: "+suppressMode);
	}
	
	/**
	 * 
	 */
	@Override
	public boolean listen(ModelEvent<Param<?>> modelEvent) {
		if(CollectionUtils.isEmpty(getParamEventListeners())) return false;
		
		getParamEventListeners()
			.stream()
			.filter(e-> !e.shouldSuppress(getSuppressMode()))
			.filter(e-> e.shouldAllow(modelEvent.getPayload()))
			.forEach(e-> e.listen(modelEvent));
		
		return true;
	}
	
}
