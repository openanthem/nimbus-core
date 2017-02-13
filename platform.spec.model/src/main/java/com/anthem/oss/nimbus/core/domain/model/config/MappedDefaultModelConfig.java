/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig.MappedModelConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo"})
public class MappedDefaultModelConfig<T, M> extends DefaultModelConfig<T> implements MappedModelConfig<T, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ModelConfig<M> mapsTo;
	
	public MappedDefaultModelConfig(ModelConfig<M> mapsTo, Class<T> referredClass) {
		super(referredClass);
		this.mapsTo = mapsTo;
	}
		
}
