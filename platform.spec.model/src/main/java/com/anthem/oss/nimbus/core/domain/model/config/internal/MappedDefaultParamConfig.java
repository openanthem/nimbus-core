/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo", "path"})
public class MappedDefaultParamConfig<P, M> extends DefaultParamConfig<P> implements MappedParamConfig<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore final private ModelConfig<?> mapsToEnclosingModel;
	
	@JsonIgnore	final private ParamConfig<M> mapsTo;

	@JsonIgnore final private Path path;

	public MappedDefaultParamConfig(String code, ModelConfig<?> mapsToEnclosingModel, ParamConfig<M> mapsTo, Path path) {
		super(code);
		this.mapsToEnclosingModel = mapsToEnclosingModel;
		this.mapsTo = mapsTo;
		this.path = path;
	}
	
}
