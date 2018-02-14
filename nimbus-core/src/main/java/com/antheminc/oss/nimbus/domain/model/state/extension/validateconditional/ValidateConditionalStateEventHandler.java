package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.extension.AbstractConditionalStateEventHandler;

/**
 * <p>Validate Conditional State Event handler for conditionally assigning validations based
 * on conditional logic defined via <tt>&#64;ValidateConditional</tt> configuration.</p>
 * 
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional
 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditionals
 * @author Tony Lopez
 *
 */
public class ValidateConditionalStateEventHandler extends 
	AbstractConditionalStateEventHandler.EvalExprWithCrudActions<ValidateConditional> {
	
	private static Map<ValidationScope, ValidationAssignmentStrategy> strategies = new HashMap<>();
	static {
		strategies.put(ValidationScope.SIBLING, new SiblingValidationAssignmentStrategy());
		strategies.put(ValidationScope.SIBLING_NESTED, new SiblingNestedValidationAssignmentStrategy());
	}
	
	public ValidateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ValidateConditional configuredAnnotation) {
		boolean isTrue = this.evalWhen(onChangeParam, configuredAnnotation.when());
		ValidationAssignmentStrategy strategy = this.getValidationAssignmentStrategy(configuredAnnotation);
		strategy.execute(isTrue, onChangeParam, configuredAnnotation.targetGroup());
	}
	
	private ValidationAssignmentStrategy getValidationAssignmentStrategy(ValidateConditional configuredAnnotation) {
		return Optional.ofNullable(strategies.get(configuredAnnotation.scope()))
				.orElseThrow(() -> new InvalidConfigException("Could not locate a Validation Assignment Strategy for: " + 
						configuredAnnotation.scope()));
	}
}
