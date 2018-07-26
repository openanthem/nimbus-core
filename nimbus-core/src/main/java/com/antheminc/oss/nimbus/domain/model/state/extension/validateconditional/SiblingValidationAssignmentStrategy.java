package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p>This strategy will first find all siblings of {@code onChangeParam}, inclusively. As it finds
 * a sibling param, it will head-recursively traverse to all nested params that exist below that 
 * sibling param. 
 * 
 * <p>For each param identified that has a conditional {@code Constraint} that should be applied, 
 * this strategy will attempt to add the identified "group" of that {@code Constraint} to the 
 * respective param's {@code activeValidationGroups}. <b>Note:</b> Since this strategy follows a 
 * head-recursion pattern, it will attempt to apply the assignment of 
 * {@code activeValidationGroups} to the lowest nested param before adding to the parent 
 * param.
 * 
 * <p>A conditional {@code Constraint} that should be applied is one who's "groups" field 
 * contains the group identified by {@code configuredAnnotation.targetGroup}.
 * 
 * <p>This strategy corresponds directly to {@link ValidationScope#SIBLING}.
 * 
 * @author Tony Lopez
 * @since 1.0
 * @see ValidateConditional
 */
public final class SiblingValidationAssignmentStrategy extends AbstractValidationAssignmentStrategy {

	@Override
	void assignGroupTo(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup) {
		onChangeParam.traverseParent(p -> addGroupToParam(p, targetGroup), true, true);
	}

	@Override
	void unassignGroupFrom(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup) {
		onChangeParam.traverseParent(p -> removeGroupFromParam(p, targetGroup), true, true);
	}
}
