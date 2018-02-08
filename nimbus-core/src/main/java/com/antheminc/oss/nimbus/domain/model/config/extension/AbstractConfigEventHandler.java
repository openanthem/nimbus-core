/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.extension;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.defn.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;


/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractConfigEventHandler<A extends Annotation> {

	protected <T> T castOrEx(Class<T> type, ParamConfig<?> param) {
		if(!type.isInstance(param))
			throw new InvalidConfigException("Handler supports ParamConfig of type: "+type+" but found of type: "+param.getClass());
		
		return type.cast(param);
	}
}
