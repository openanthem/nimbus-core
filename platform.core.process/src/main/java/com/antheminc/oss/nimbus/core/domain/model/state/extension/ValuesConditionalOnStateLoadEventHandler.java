package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * 
 * <p>Abstract Conditional State Event handler for updating <tt>Values</tt> annotated fields based
 * on conditional logic defined via configuration during the OnStateLoad event.</p>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional
 * @see com.antheminc.oss.nimbus.core.domain.model.state.extension.AbstractValuesConditionalStateEventHandler
 *
 */
public class ValuesConditionalOnStateLoadEventHandler extends AbstractValuesConditionalStateEventHandler 
	implements OnStateLoadHandler<ValuesConditional> {

	public ValuesConditionalOnStateLoadEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler#handle(java.lang.annotation.Annotation, com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param)
	 */
	@Override
	public void handle(ValuesConditional configuredAnnotation, Param<?> param) {
		this.handleInternal(configuredAnnotation, param);
	}

}
