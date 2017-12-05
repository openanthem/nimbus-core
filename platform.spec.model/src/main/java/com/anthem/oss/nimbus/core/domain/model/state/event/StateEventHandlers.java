/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.event;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.core.domain.EventHandler;
import com.antheminc.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.core.domain.definition.event.StateEvent.OnTxnExecute;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;

/**
 * @author Soham Chakravarti
 *
 */
public final class StateEventHandlers {

	@EventHandler(OnStateLoad.class)
	public interface OnStateLoadHandler<A extends Annotation> {
		
		public void handle(A configuredAnnotation, Param<?> param);
	}
	
	
	@EventHandler(OnStateChange.class)
	public interface OnStateChangeHandler<A extends Annotation> {
		
		public void handle(A configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event);
	}
	
	
	@EventHandler(OnTxnExecute.class)
	public interface OnTxnExecuteHandler<A extends Annotation> {
		
		public void handleOnStart(A configuredAnnotation, ExecutionTxnContext txnCtx);
		
		public void handleOnStop(A configuredAnnotation, ExecutionTxnContext txnCtx, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);
	}
}
