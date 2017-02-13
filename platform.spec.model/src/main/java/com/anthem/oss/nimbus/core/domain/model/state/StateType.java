/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.io.Serializable;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.model.ParamType;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.ListModel;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString(of="config")
public class StateType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore private final ParamType config;
	
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
	public static class Nested<P> extends StateType implements Serializable {

		private static final long serialVersionUID = 1L;

		final private DomainState.Model<P> model;
		
		public Nested(ParamType.Nested<P> config, Model<P> model) {
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

		public NestedCollection(ParamType.NestedCollection<P> config, ListModel<P> model) {
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
		public ListModel<P> getModel() {
			return (DomainState.ListModel<P>)super.getModel();
		}
	}
	
}
