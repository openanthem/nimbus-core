/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config.event;

import java.lang.annotation.Annotation;

import com.antheminc.oss.nimbus.domain.EventHandler;
import com.antheminc.oss.nimbus.domain.defn.event.ConfigEvent.OnParamCreate;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;

/**
 * @author Soham Chakravarti
 *
 */
public final class ConfigEventHandlers {

	@EventHandler(OnParamCreate.class)
	public interface OnParamCreateHandler<A extends Annotation> {
		
		public void handle(A configuredAnnotation, ParamConfig<?> param);
	}
}
