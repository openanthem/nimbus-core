/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.model.ParamConfig.MappedParamConfigDelinked;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo", "path"})
public class MappedDefaultParamConfigDetached<P, M> extends DefaultParamConfig<P> implements MappedParamConfigDelinked<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore	final private ModelConfig<M> mapsTo;

	@JsonIgnore final private Path path;

	public MappedDefaultParamConfigDetached(String code, ModelConfig<M> mapsTo, Path path) {
		super(code);
		this.mapsTo = mapsTo;
		this.path = path;
	}
	
}
