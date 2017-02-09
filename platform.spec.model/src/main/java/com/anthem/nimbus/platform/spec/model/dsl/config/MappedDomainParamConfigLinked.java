/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo.Path;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig.MappedParamConfigLinked;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"mapsTo", "path"})
public class MappedDomainParamConfigLinked<P, M> extends DomainParamConfig<P> implements MappedParamConfigLinked<P, M>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore	final private ParamConfig<M> mapsTo;

	@JsonIgnore final private Path path;

	public MappedDomainParamConfigLinked(String code, ParamConfig<M> mapsTo, Path path) {
		super(code);
		this.mapsTo = mapsTo;
		this.path = path;
	}
	
}
