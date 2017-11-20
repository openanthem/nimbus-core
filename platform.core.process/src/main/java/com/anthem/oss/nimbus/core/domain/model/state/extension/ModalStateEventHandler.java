package com.anthem.oss.nimbus.core.domain.model.state.extension;

import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Modal;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * <p>Default StateEventHandler for <tt>ViewConfig.Modal</tt> that sets default
 * contextual values for enabled and visible to the value in the provided
 * <tt>&#64;ParamContext</tt> field <tt>context</tt>.</p>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.anthem.oss.nimbus.core.domain.definition.ViewConfig.Modal
 */
public class ModalStateEventHandler implements OnStateLoadHandler<Modal> {

	@Override
	public void handle(Modal configuredAnnotation, Param<?> param) {
		param.setVisible(configuredAnnotation.context().visible());
		param.setEnabled(configuredAnnotation.context().enabled());
	}
}