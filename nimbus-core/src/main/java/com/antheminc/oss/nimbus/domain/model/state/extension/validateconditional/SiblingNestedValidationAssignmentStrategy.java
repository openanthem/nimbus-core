package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
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
 * @author Tony Lopez
 */
public final class SiblingNestedValidationAssignmentStrategy extends ValidationAssignmentStrategy {

	@Override
	void assignGroupTo(Param<?> param, Class<? extends ValidationGroup> group) {
		this.handleSiblings(param, group, (siblingParam, g) -> {
			this.handleNested(siblingParam, g, this::addGroupToParam);
		});
	}

	@Override
	void unassignGroupFrom(Param<?> param, Class<? extends ValidationGroup> group) {
		this.handleSiblings(param, group, (siblingParam, g) -> {
			this.handleNested(siblingParam, g, this::removeGroupFromParam);
		});
	}

}
