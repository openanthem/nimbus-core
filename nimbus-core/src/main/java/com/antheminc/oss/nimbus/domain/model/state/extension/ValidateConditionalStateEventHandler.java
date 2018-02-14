package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.lang.ArrayUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p>Validate Conditional State Event handler for conditionally assigning validations based
 * on conditional logic defined via <tt>&#64;ValidateConditional</tt> configuration.</p>
 * 
 * @author Tony Lopez
 *
 */
public class ValidateConditionalStateEventHandler extends 
	AbstractConditionalStateEventHandler.EvalExprWithCrudActions<ValidateConditional> {

	public static final String ATTR_GROUPS = "groups";
	
	public ValidateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}

	/**
	 * <p>This strategy will first find all siblings of <tt>param</tt>, inclusively. As it finds
	 * a sibling param, it will head-recursively traverse to all nested params that exist below that 
	 * sibling param.</p> 
	 * 
	 * <p>For each param identified that has a conditional <tt>Constraint</tt> that should be applied, 
	 * this strategy will attempt to add the identified "group" of that <tt>Constraint</tt> to the 
	 * respective param's <tt>activeValidationGroups</tt>. <b>Note:</b> Since this strategy follows a 
	 * head-recursion pattern, it will attempt to apply the assignment of 
	 * <tt>activeValidationGroups</tt> to the lowest nested param before adding to the parent 
	 * param.</p>
	 * 
	 * <p>A conditional </tt>Constraint</tt> that should be applied is one who's "groups" field 
	 * contains the group identified by <tt>configuredAnnotation.targetGroup</tt>.</p>
	 * 
	 * <p>This strategy corresponds directly to <tt>ValidateConditional.scope = 
	 * ValidationScope.SIBLING_NESTED</tt>.</p>
	 * 
	 * @param param the onChangeParam
	 * @param configuredAnnotation the annotation metadata
	 */
	private void doSiblingNestedStrategy(Param<?> param, ValidateConditional configuredAnnotation) {
		this.handleSiblings(param, configuredAnnotation, (siblingParam, ca) -> {
			this.handleSiblingNested(siblingParam, ca, this::handleGroupAssignment);
		});
	}
	
	/**
	 * <p>This strategy will first find all siblings of <tt>param</tt>, inclusively. For each 
	 * sibling that has a conditional <tt>Constraint</tt> that should be applied, this strategy will 
	 * attempt to add the identified "group" of that <tt>Constraint</tt> to the respective param's 
	 * <tt>activeValidationGroups</tt>.</p>
	 * 
	 * <p>A conditional </tt>Constraint</tt> that should be applied is one who's "groups" field 
	 * contains the group identified by <tt>configuredAnnotation.targetGroup</tt>.</p>
	 * 
	 * <p>This strategy corresponds directly to <tt>ValidateConditional.scope = 
	 * ValidationScope.SIBLING</tt>.</p>
	 * 
	 * @param param the onChangeParam
	 * @param configuredAnnotation the annotation metadata
	 */
	private void doSiblingStrategy(Param<?> param, ValidateConditional configuredAnnotation) {
		this.handleSiblings(param, configuredAnnotation, this::handleGroupAssignment);
	}

	/**
	 * <p>When this <tt>param</tt> contains a Constraint annotation config with a
	 * matching group from <tt>configuredAnnotation</tt>, then that group identifier
	 * will be added to </tt>activeValidationGroups</tt> of <tt>param</tt>.</p>
	 * 
	 * @param param the param to apply group assignment to
	 * @param configuredAnnotation the annotation metadata
	 */
	@SuppressWarnings("unchecked")
	private void handleGroupAssignment(Param<?> param, ValidateConditional configuredAnnotation) {
		
		List<AnnotationConfig> validations = param.getConfig().getValidations();
		if (null != validations) {
			
			// Collect the validation groups that should be "active"
			Set<Class<? extends ValidationGroup>> activeValidationGroups = new HashSet<>();
			for(AnnotationConfig ac : validations) {
				Class<?>[] groups = (Class<?>[]) ac.getAttributes().get(ATTR_GROUPS);
				if (ArrayUtils.contains(groups, configuredAnnotation.targetGroup())) {
					activeValidationGroups.add(configuredAnnotation.targetGroup());
				}
			}
			
			// Set the active validation groups.
			param.setActiveValidationGroups(activeValidationGroups.toArray(new 
					Class[activeValidationGroups.size()]));
		}
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ValidateConditional configuredAnnotation) {
		
		if (this.evalWhen(onChangeParam, configuredAnnotation.when())) {
			
			if (ValidationScope.SIBLING.equals(configuredAnnotation.scope())) {
				this.doSiblingStrategy(onChangeParam, configuredAnnotation);
				
			} else if (ValidationScope.SIBLING_NESTED.equals(configuredAnnotation.scope())) {
				this.doSiblingNestedStrategy(onChangeParam, configuredAnnotation);
			}
		} else {
			
			if (ValidationScope.SIBLING.equals(configuredAnnotation.scope())) {
				this.doSiblingRemovalStrategy(onChangeParam, configuredAnnotation);
				
			} else if (ValidationScope.SIBLING_NESTED.equals(configuredAnnotation.scope())) {
				this.doSiblingNestedRemovalStrategy(onChangeParam, configuredAnnotation);
			}
		}
	}
	
	private void doSiblingNestedRemovalStrategy(Param<?> param, ValidateConditional configuredAnnotation) {
		this.handleSiblingNested(param, configuredAnnotation, this::removeTargetGroup);
	}

	private void doSiblingRemovalStrategy(Param<?> param, ValidateConditional configuredAnnotation) {
		this.handleSiblings(param, configuredAnnotation, this::removeTargetGroup);
	}

	@SuppressWarnings("unchecked")
	private void removeTargetGroup(Param<?> param, ValidateConditional configuredAnnotation) {
		param.setActiveValidationGroups((Class<? extends ValidationGroup>[]) 
				ArrayUtils.removeElement(param.getActiveValidationGroups(), configuredAnnotation.targetGroup()));
	}
	
	/**
	 * <p>Traverses all sibling params and nested params of those siblings of <tt>param</tt> 
	 * and executes <tt>handler</tt> from the context of each param.</p>
	 * 
	 * @param param the param from which to retrieve siblings and nested entities from
	 * @param configuredAnnotation the annotation metadata
	 */
	private void handleSiblingNested(Param<?> param, ValidateConditional configuredAnnotation, 
			BiConsumer<Param<?>, ValidateConditional> handler) {
		if (param.isNested()) {
			param.findIfNested().getParams().forEach(nestedParam -> 
				handleSiblingNested(nestedParam, configuredAnnotation, handler));
		}
		handler.accept(param, configuredAnnotation);
	}

	/**
	 * <p>Traverses all sibling params of <tt>param</tt> and executes <tt>handler</tt> from the context 
	 * of each sibling param.</p>
	 * 
	 * @param param the param from which to retrieve siblings from
	 * @param configuredAnnotation the annotation metadata
	 */
	private void handleSiblings(Param<?> param, ValidateConditional configuredAnnotation, 
			BiConsumer<Param<?>, ValidateConditional> handler) {
		param.getParentModel().getParams().forEach(p -> handler.accept(p, configuredAnnotation));
	}
}
