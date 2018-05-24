package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.Map;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * <p>Validate Conditional State Event handler for conditionally assigning validations based
 * on conditional logic defined via <tt>&#64;ValidateConditional</tt> configuration.</p>
 * 
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditionals
 * @author Tony Lopez
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
public class ValidateConditionalStateEventHandler extends 
	AbstractConditionalStateEventHandler.EvalExprWithCrudActions<ValidateConditional> {
	
	private Map<ValidationScope, ValidationAssignmentStrategy> validationAssignmentStrategies;
	
	@SuppressWarnings("unchecked")
	public ValidateConditionalStateEventHandler(BeanResolverStrategy beanResolver,
			Map<ValidationScope, ValidationAssignmentStrategy> validationAssignmentStrategies) {
		super(beanResolver);
		this.validationAssignmentStrategies = 
				beanResolver.find(Map.class, ValidationScope.class, ValidationAssignmentStrategy.class);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ValidateConditional configuredAnnotation) {
		boolean isTrue = this.evalWhen(onChangeParam, configuredAnnotation.when());
		ValidationAssignmentStrategy strategy = this.getValidationAssignmentStrategy(configuredAnnotation);
		strategy.execute(isTrue, onChangeParam, configuredAnnotation.targetGroup());
	}
	
	private ValidationAssignmentStrategy getValidationAssignmentStrategy(ValidateConditional configuredAnnotation) {
		return Optional.ofNullable(getValidationAssignmentStrategies().get(configuredAnnotation.scope()))
				.orElseThrow(() -> new InvalidConfigException("Could not locate a Validation Assignment Strategy for: " + 
						configuredAnnotation.scope()));
	}
	
	/**
	 * <p>Base Strategy interface class to support for strategies used when handling 
	 * &#64;ValidateConditional executions.</p>
	 * 
	 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.AbstractValidationAssignmentStrategy
	 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingValidationAssignmentStrategy
	 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingNestedValidationAssignmentStrategy
	 * @author Tony Lopez
	 *
	 */
	public interface ValidationAssignmentStrategy {
		
		/**
		 * <p>Executes this validation assignment strategy.</p>
		 * 
		 * @param add if true, attempts to assign <tt>group</tt> to the <tt>activeValidationGroups</tt> field 
		 * of <tt>param</tt>.
		 * @param param the root param on which &#64;ValidateConditional is defined
		 * @param group the group to assign
		 */
		public void execute(boolean add, Param<?> param, Class<? extends ValidationGroup> group);
	}
}
