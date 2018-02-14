package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

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
 * @author Tony Lopez
 */
public final class SiblingValidationAssignmentStrategy extends ValidationAssignmentStrategy {

	@Override
	void assignGroupTo(Param<?> param, Class<? extends ValidationGroup> group) {
		this.handleSiblings(param, group, this::addGroupToParam);
	}

	@Override
	void unassignGroupFrom(Param<?> param, Class<? extends ValidationGroup> group) {
		this.handleSiblings(param, group, this::removeGroupFromParam);
	}

}
