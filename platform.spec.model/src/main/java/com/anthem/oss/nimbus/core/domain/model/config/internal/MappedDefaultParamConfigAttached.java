/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.MappedParamConfigLinked;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo", "path"})
public class MappedDefaultParamConfigAttached<P, M> extends DefaultParamConfig<P> implements MappedParamConfigLinked<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore	final private ParamConfig<M> mapsTo;

	@JsonIgnore final private Path path;

	public MappedDefaultParamConfigAttached(String code, ParamConfig<M> mapsTo, Path path) {
		super(code);
		this.mapsTo = mapsTo;
		this.path = path;
	}
	
}
