/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor @ToString(of="config")
public class DomainStateType implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@JsonIgnore @Getter private final ParamType config;
	
	public String getName() {
		return config.getName();
	}
	
	public boolean isCollection() {
		return false;
	}
	
	public boolean isNested() {
		return config.isNested();
	}
	
	public <P> Nested<P> findIfNested() {
		return null;
	}
	
	public <P> NestedCollection<P> findIfCollection() {
		return null;
	}
	
	
	@Getter @Setter @ToString(callSuper=true, of="model")
	public static class Nested<P> extends DomainStateType implements Serializable {

		private static final long serialVersionUID = 1L;

		final private DomainState.Model<P> model;
		
		public Nested(ParamType.Nested<P> config, DomainState.Model<P> model) {
			super(config);
			this.model = model;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Nested<P> findIfNested() {
			return this;
		}
	}
	
	public static class NestedCollection<P> extends Nested<List<P>> {
		private static final long serialVersionUID = 1L;

		public NestedCollection(ParamType.NestedCollection<P> config, DomainState.ListModel<P> model) {
			super(config, model);
		}
		
		@Override
		public boolean isCollection() {
			return true;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public NestedCollection<P> findIfCollection() {
			return this;
		}
		
		@Override
		public DomainState.ListModel<P> getModel() {
			return (DomainState.ListModel<P>)super.getModel();
		}
	}
	
}
