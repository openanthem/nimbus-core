package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.List;
import java.util.stream.Collectors;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.builder.AbstractEntityStateBuilder;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * Conditional State Event handler for updating <tt>Values</tt> annotated fields based
 * on conditional logic defined via configuration.
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public class ValuesConditionalStateEventHandler extends AbstractConditionalStateEventHandler 
		implements OnStateLoadHandler<ValuesConditional>, OnStateChangeHandler<ValuesConditional> {

	public static final JustLogit LOG = new JustLogit();
	
	private final CommandExecutorGateway gateway;
	
	public ValuesConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.gateway = this.beanResolver.get(CommandExecutorGateway.class);
	}

	/**
	 * Sets the <tt>values</tt> field for <tt>targetParam</tt> by extracting the underlying
	 * collection of <tt>ParamValue</tt> defined within <tt>values</tt>.
	 * 
	 * @param targetParam the entity to set the values within
	 * @param values the values metadata to set
	 * @param resetOnChange if true, always resets the state of <tt>targetParam</tt> when 
	 * <tt>values</tt> is updated. If false, resets the state of <tt>targetParam</tt> only when
	 * the existing state does not exist within the new updated set of <tt>values</tt>.
	 */
	private void execute(Param<?> targetParam, Values values, boolean resetOnChange) {
		final List<ParamValue> oldValues = targetParam.getValues();
		final List<ParamValue> newValues = AbstractEntityStateBuilder.buildValues(values, targetParam, this.gateway);
		targetParam.setValues(newValues);
		LOG.trace(() -> "Updated values for param '" + targetParam + "' from '" + oldValues + "' to '" + newValues + "'.");
		
		if (resetOnChange) {
			targetParam.setState(null);
		} else {
			
			// check if targetParam state is in the list of new values. reset to null if not.
			if (null != targetParam.getState() && 
					!newValues.stream().map(ParamValue::getCode).collect(Collectors.toList())
						.contains(targetParam.getState())) {
				
				targetParam.setState(null);
			}
		}
	}
	
	/**
	 * Iterates through the conditions configured for <tt>configuredAnnotation</tt> and attempts
	 * to execute them based on the evaluated expression of the condition's when() value.
	 * 
	 * <p>If the configuredAnnotation's <tt>exclusive</tt> value is <tt>true</tt>, then the first 
	 * truthy condition that is successfully executed will be the only condition to be executed.
	 * 
	 * @param configuredAnnotation the <tt>ValuesConditional</tt> annotation for the source parameter
	 * @param srcParam the source parameter (annotated field)
	 * @param targetParam the target parameter to execute against
	 * @return true if a condition was successfully executed, false if no condition was executed
	 */
	private boolean executeConditions(ValuesConditional configuredAnnotation, Param<?> srcParam, Param<?> targetParam) {
		boolean executed = false;
		
		// iterate through the conditions
		for(final Condition condition: configuredAnnotation.condition()) {
			
			// if the condition is evaluated to true
			if (this.evalWhen(srcParam, condition.when())) {
				
				// set the values
				this.execute(targetParam, condition.then(), configuredAnnotation.resetOnChange());
				
				// if the configured annotation should only execute one true condition,
				// execute the first and exit
				if (configuredAnnotation.exclusive()) {
					return true;
				}
				
				// note that a successful execution has occurred and continue
				executed = true;
			}
		}
		
		return executed;
	}
	
	@Override
	public void handle(ValuesConditional configuredAnnotation, Param<?> param) {
		this.handleInternal(configuredAnnotation, param);	
	}
	
	@Override
	public void handle(ValuesConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		this.handle(configuredAnnotation, event.getParam());
	}

	private void handleInternal(ValuesConditional configuredAnnotation, Param<?> srcParam) {
		final Param<?> targetParam = this.retrieveParamByPath(srcParam, configuredAnnotation.target());
		
		final boolean shouldExecuteDefault = !this.executeConditions(configuredAnnotation, srcParam, targetParam);
		
		if (shouldExecuteDefault) {
			this.execute(targetParam, targetParam.getConfig().getValues(), configuredAnnotation.resetOnChange());
		}
	}
}
