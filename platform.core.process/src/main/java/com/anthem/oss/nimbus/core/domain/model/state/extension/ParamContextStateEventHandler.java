package com.anthem.oss.nimbus.core.domain.model.state.extension;

import com.anthem.oss.nimbus.core.domain.definition.extension.ParamContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * Default StateEventHandler for fields decorated with <tt>ParamContext</tt> that sets
 * param context values as defined within the <tt>ParamContext</tt> annotation attributes.
 *  
 * @author Tony Lopez (AF42192)
 * @see com.anthem.oss.nimbus.core.domain.definition.extension.ParamContext
 */
public class ParamContextStateEventHandler implements OnStateLoadHandler<ParamContext> {

	@Override
	public void handle(ParamContext configuredAnnotation, Param<?> param) {
		param.setVisible(configuredAnnotation.visible());
		param.setEnabled(configuredAnnotation.enabled());
	}
}
