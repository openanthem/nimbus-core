package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.lang.ArrayUtils;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.extension.ValidateConditionalStateEventHandler;

/**
 * <p>Abstract Strategy implementation class provides support for strategies used
 * when handling &#64;ValidateConditional executions.</p>
 * 
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingValidationAssignmentStrategy
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingNestedValidationAssignmentStrategy
 * @author Tony Lopez
 *
 */
public abstract class AbstractValidationAssignmentStrategy implements 
		ValidateConditionalStateEventHandler.ValidationAssignmentStrategy {

	public static final String ATTR_GROUPS = "groups";
	
	/*
	 * (non-Javadoc)
	 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ValidateConditionalStateEventHandler.ValidationAssignmentStrategy#execute(boolean, com.antheminc.oss.nimbus.domain.model.state.EntityState.Param, java.lang.Class)
	 */
	public void execute(boolean add, Param<?> onChangeParam, Class<? extends ValidationGroup> group) {
		if (!add) {
			this.unassignGroupFrom(onChangeParam, group);
		} else {
			this.assignGroupTo(onChangeParam, group);
		}
	}
	
	/**
	 * <p>Abstract method containing logic to add <tt>targetGroup</tt> to the 
	 * the desired <tt>activeValidationGroups</tt> fields.</p>
	 * 
	 * @param onChangeParam the root param on which &#64;ValidateConditional is defined
	 * @param targetGroup the group to assign
	 */
	abstract void assignGroupTo(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup);
	
	/**
	 * <p>Abstract method containing logic to remove <tt>targetGroup</tt> from the desired 
	 * <tt>activeValidationGroups</tt> fields.</p>
	 * 
	 * @param onChangeParam the root param on which &#64;ValidateConditional is defined
	 * @param targetGroup the group to unassign
	 */
	abstract void unassignGroupFrom(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup);
	
	/**
	 * <p>When <tt>param</tt> contains a Constraint annotation config with a matching group of 
	 * <tt>targetGroup</tt>, then that group will be added to the </tt>activeValidationGroups</tt> 
	 * field of <tt>param</tt>.</p>
	 * 
	 * @param param the param to apply group assignment to
	 * @param targetGroup the group to assign
	 */
	@SuppressWarnings("unchecked")
	protected void addGroupToParam(Param<?> param, Class<? extends ValidationGroup> targetGroup) {
		
		List<AnnotationConfig> validations = param.getConfig().getValidations();
		if (null != validations) {
			
			// Collect the validation groups that should be "active"
			for(AnnotationConfig ac : validations) {
				Class<?>[] groups = (Class<?>[]) ac.getAttributes().get(ATTR_GROUPS);
				if (ArrayUtils.contains(groups, targetGroup)) {
					Set<Class<? extends ValidationGroup>> activeValidationGroups = new HashSet<>(Arrays.asList(param.getActiveValidationGroups()));
					activeValidationGroups.add(targetGroup);
					param.setActiveValidationGroups(activeValidationGroups.toArray(new Class[activeValidationGroups.size()]));
					return;
				}
			}
		}
	}
	
	/**
	 * <p>Traverse (recursively) all sibling params and nested params of those siblings of {@code param}
	 * and executes {@code handler} from the context of each param.
	 * @param param the param object from which to retrieve nested entities from
	 * @param handler the handler method to execute on all retrieved params
	 * @param targetGroup the group object
	 */
	protected void handleNested(Param<?> param, Class<? extends ValidationGroup> targetGroup, 
			BiConsumer<Param<?>, Class<? extends ValidationGroup>> handler) {
		handleNested(param, targetGroup, handler, true);
	}
	
	/**
	 * <p>Traverses all sibling params and nested params of those siblings of <tt>param</tt> 
	 * and executes <tt>handler</tt> from the context of each param.
	 * @param param the param object from which to retrieve nested entities from
	 * @param handler the handler method to execute on all retrieved params
	 * @param targetGroup the group object
	 * @param recursive whether or not this should recursively traverse nested params.
	 */
	protected void handleNested(Param<?> param, Class<? extends ValidationGroup> targetGroup, 
			BiConsumer<Param<?>, Class<? extends ValidationGroup>> handler, boolean recursive) {
		
		if (param.isNested() && null != param.findIfNested().getParams()) {
			for(Param<?> nestedParam: param.findIfNested().getParams()) {
				if (!recursive) {
					handler.accept(nestedParam, targetGroup);
				} else {
					handleNested(nestedParam, targetGroup, handler, true);
				}
			}
		}
		handler.accept(param, targetGroup);
	}

	/**
	 * <p>Traverses all sibling params of <tt>param</tt> and executes <tt>handler</tt> from the context 
	 * of each sibling param.</p>
	 * 
	 * @param param the param from which to retrieve siblings from
	 * @param handler the handler method to execute on the sibling params
	 * @param targetGroup the group object
	 */
	protected void handleSiblings(Param<?> param, Class<? extends ValidationGroup> targetGroup, 
			BiConsumer<Param<?>, Class<? extends ValidationGroup>> handler) {
		param.getParentModel().getParams().forEach(p -> handler.accept(p, targetGroup));
	}
	
	/**
	 * <p>If <tt>param</tt> contains a Constraint annotation config with a matching group of 
	 * <tt>group</tt>, then that group will be removed from the </tt>activeValidationGroups</tt> 
	 * field of <tt>param</tt>.</p>
	 * 
	 * @param param the param from which to remove <tt>group</tt> from
	 * @param targetGroup the group to remove
	 */
	@SuppressWarnings("unchecked")
	protected void removeGroupFromParam(Param<?> param, Class<? extends ValidationGroup> targetGroup) {
		param.setActiveValidationGroups((Class<? extends ValidationGroup>[]) 
				ArrayUtils.removeElement(param.getActiveValidationGroups(), targetGroup));
	}
}
