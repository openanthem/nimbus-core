/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventPublisher;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class QuadModel<V, C> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final CommandElement key;
	
	private ExecutionEntity<V, C>.ExModel root;
	
	@JsonIgnore transient private final Model<C> core;
	
	@JsonIgnore transient private final Model<V> view;
	
	@JsonIgnore transient private final Model<ProcessFlow> flow;
	
	@JsonIgnore transient private QuadScopedEventPublisher eventPublisher;
	
	public QuadModel(CommandElement key, ExecutionEntity<V, C>.ExModel root) {
		this.key = key;
		this.root = root;
		this.core = getRoot().findModelByPath("/c");
		this.view = getRoot().findModelByPath("/v");
		this.flow = getRoot().findModelByPath("/f");
	}
	
	public Model<?> getView() {
		if(view==null)
			return core;
		
		return view;
	}
	
	public Model<?> resolveStateAndConfig(Command cmd) {
		return cmd.isView() ? view : core;
	}
	
	@Override
	protected void finalize() throws Throwable {
		getRoot().getExecutionRuntime().stop();
		
		super.finalize();
	}
		
}
