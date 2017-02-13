/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config.internal;

import java.lang.annotation.Annotation;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.model.config.AnnotationConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig.Desc;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public abstract class AbstractParamConfig<P> implements ParamConfig<P> {

	private static final long serialVersionUID = 1L;

	final private String code;

	final private MapsTo.Path mapsTo;
	
	public AbstractParamConfig(String code, MapsTo.Path mapsTo) {
		this.code = code;
		this.mapsTo = mapsTo;
	}
	

	
	abstract public void setDesc(ParamConfig.Desc desc);
	
	abstract public void setValidations(List<AnnotationConfig> validations);
	
	abstract public void setType(ParamType type);
	
	abstract public void setAnnotations(List<Annotation> annotations);
	
}
