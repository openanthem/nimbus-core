/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig.MappedModelConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo"})
public class MappedDomainModelConfig<T, M> extends DomainModelConfig<T> implements MappedModelConfig<T, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ModelConfig<M> mapsTo;
	
	public MappedDomainModelConfig(ModelConfig<M> mapsTo, Class<T> referredClass) {
		super(referredClass);
		this.mapsTo = mapsTo;
	}
		
}
