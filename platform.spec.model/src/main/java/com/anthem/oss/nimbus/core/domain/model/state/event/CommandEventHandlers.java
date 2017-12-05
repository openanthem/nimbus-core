/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.event;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.antheminc.oss.nimbus.core.domain.EventHandler;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.definition.event.CommandEvent.OnRootExecute;
import com.antheminc.oss.nimbus.core.domain.definition.event.CommandEvent.OnSelfExecute;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.ExecutionModel;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;

/**
 * @author Soham Chakravarti
 *
 */
public final class CommandEventHandlers {

	@EventHandler(OnRootExecute.class)
	public interface OnRootExecuteHandler<A extends Annotation> {
		
		public void handleOnStart(A configuredAnnotation, Command cmd);
		
		public void handleOnStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	}
	
	
	@EventHandler(OnSelfExecute.class)
	public interface OnSelfExecuteHandler<A extends Annotation> {
		
		public void handleOnStart(A configuredAnnotation, Command cmd);
		
		public void handleOnStop(A configuredAnnotation, Command cmd, Map<ExecutionModel<?>, List<ParamEvent>> aggregatedEvents);	
	} 
}
