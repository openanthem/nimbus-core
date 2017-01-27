/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

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
public class TypeStateAndConfig implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@JsonIgnore @Getter private final ParamType config;
	
	
	public String getName() {
		return config.getName();
	}
	
	public ParamType.CollectionType getCollection() {
		return config.getCollection();
	}
	
	
	public boolean isNested() {
		return config.isNested();
	}
	
	public <P> StateAndConfig.Model<P, ? extends ModelConfig<P>> findIfNested() {
		return null;
	}
	
	
	
	@Getter @Setter @ToString(callSuper=true, of="model")
	public static class Nested<P> extends TypeStateAndConfig implements Serializable {

		private static final long serialVersionUID = 1L;

		final private ModelStateAndConfig<P, ?> model;
		
		
		public Nested(ParamType.Nested<P> config, ModelStateAndConfig<P, ?> model) {
			super(config);
			this.model = model;
		}
		
		
		@SuppressWarnings("unchecked")
		@Override
		public StateAndConfig.Model<P, ? extends ModelConfig<P>> findIfNested() {
			return model;
		}
	}
	
}
