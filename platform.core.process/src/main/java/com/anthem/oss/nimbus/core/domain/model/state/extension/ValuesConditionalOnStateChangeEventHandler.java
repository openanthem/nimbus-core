/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.stream.Collectors;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;

/**
 * <p>ValuesConditional State Event handler implementation for updating <tt>Values</tt> annotated fields 
 * based on conditional logic defined via configuration during the OnStateChange event.</p>
 * 
 * <p>Handles the scenario for when <tt>ValuesConditional</tt> provides the <tt>resetOnChange</tt> flag.
 * When true, the handler will always reset the state of the <tt>targetParam</tt> after the execution step 
 * when new <tt>values</tt> are updated. If false, the handler will reset the state of the <tt>targetParam</tt>
 * only when the existing state does not exist within the new updated set of <tt>values</tt>.</p>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional
 * @see com.anthem.oss.nimbus.core.domain.model.state.extension.AbstractValuesConditionalStateEventHandler
 */
public class ValuesConditionalOnStateChangeEventHandler extends AbstractValuesConditionalStateEventHandler 
	implements OnStateChangeHandler<ValuesConditional> {

	private boolean resetOnChange;
	
	public ValuesConditionalOnStateChangeEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.extension.AbstractValuesConditionalStateEventHandler#afterExecute(com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param)
	 */
	@Override
	protected void afterExecute(Param<?> targetParam) {
		if (this.resetOnChange) {
			targetParam.setState(null);
		} else {
			
			// if there are no values set (default config values) OR
			// if previously selected targetParam state is not in the list of new values. then reset to null.
			if (null == targetParam.getValues() || null != targetParam.getState() && 
					!targetParam.getValues().stream().map(ParamValue::getCode).collect(Collectors.toList())
						.contains(targetParam.getState())) {
				
				targetParam.setState(null);
			}
			// otherwise the state persists.
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler#handle(java.lang.annotation.Annotation, com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext, com.anthem.oss.nimbus.core.domain.model.state.ParamEvent)
	 */
	@Override
	public void handle(ValuesConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		this.resetOnChange = configuredAnnotation.resetOnChange();
		this.handleInternal(configuredAnnotation, event.getParam());
	}
}
