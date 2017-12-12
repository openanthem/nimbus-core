package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import java.util.List;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.definition.Model.Param.Values;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.builder.AbstractEntityStateBuilder;
import com.antheminc.oss.nimbus.core.util.JustLogit;

/**
 * 
 * <p>Abstract Conditional State Event handler for updating <tt>Values</tt> annotated fields based
 * on conditional logic defined via configuration.</p>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.antheminc.oss.nimbus.core.domain.definition.extension.ValuesConditional
 */
public abstract class AbstractValuesConditionalStateEventHandler extends AbstractConditionalStateEventHandler {

	public static final JustLogit LOG = new JustLogit();
	
	protected final CommandExecutorGateway gateway;
	
	public AbstractValuesConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.gateway = this.beanResolver.get(CommandExecutorGateway.class);
	}

	/**
	 * Sets the <tt>values</tt> field for <tt>targetParam</tt> by extracting the underlying
	 * collection of <tt>ParamValue</tt> defined within <tt>values</tt>.
	 * 
	 * @param targetParam the entity to set the values within
	 * @param values the values metadata to set
	 */
	protected void execute(Param<?> targetParam, Values values) {
		final List<ParamValue> oldValues = targetParam.getValues();
		final List<ParamValue> newValues = AbstractEntityStateBuilder.buildValues(values, targetParam, this.gateway);
		targetParam.setValues(newValues);
		LOG.trace(() -> "Updated values for param '" + targetParam + "' from '" + oldValues + "' to '" + newValues + "'.");

		// perform any after-execution logic needed
		this.afterExecute(targetParam);
	}
	
	/**
	 * Operations that should be performed after <tt>targetParam</tt>'s have been successfully 
	 * updated. This method is intended to be overridden by subclasses to extend additional 
	 * functionality when needed.
	 * 
	 * @param targetParam the target entity in which the new values were updated
	 */
	protected void afterExecute(Param<?> targetParam) {	
		
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
	protected boolean executeConditions(ValuesConditional configuredAnnotation, Param<?> srcParam, Param<?> targetParam) {
		boolean executed = false;
		
		// iterate through the conditions
		for(final Condition condition: configuredAnnotation.condition()) {
			
			// if the condition is evaluated to true
			if (this.evalWhen(srcParam, condition.when())) {
				
				// set the values
				this.execute(targetParam, condition.then());
				
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

	/**
	 * Entry point for the handler. Seeks to retrieve the target parameter identified by 
	 * <tt>configuredAnnotation</tt> and based on additional conditional logic defined there and
	 * within <tt>srcParam</tt>, will attempt to execute setting the appropriate <tt>Values</tt>
	 * into the target parameter.
	 * 
	 * @param configuredAnnotation the provided configuration via <tt>@ValuesConditional</tt>.
	 * @param srcParam the decorated field's parameter object.
	 */
	protected void handleInternal(ValuesConditional configuredAnnotation, Param<?> srcParam) {
		final Param<?> targetParam = this.retrieveParamByPath(srcParam, configuredAnnotation.target());
		
		final boolean shouldExecuteDefault = !this.executeConditions(configuredAnnotation, srcParam, targetParam);
		
		if (shouldExecuteDefault) {
			this.execute(targetParam, targetParam.getConfig().getValues());
		}
	}
}
