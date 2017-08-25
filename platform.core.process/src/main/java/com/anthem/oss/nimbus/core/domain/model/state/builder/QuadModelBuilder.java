/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;

/**
 * @author Soham Chakravarti
 *
 */
public interface QuadModelBuilder {

	default <V, C> QuadModel<V, C> build(Command cmd) {
		ExecutionEntity<V, C> eState = new ExecutionEntity<>();
		return build(cmd, eState);
	}
	
	public <V, C> QuadModel<V, C> build(Command cmd, V viewState, Param<C> coreParam);
	
	public <V, C> QuadModel<V, C> build(Command cmd, ExecutionEntity<V, C> eState);
	
}
