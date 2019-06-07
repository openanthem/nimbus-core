/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional;

import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * <p>This strategy will first find all children params of {@code onChangeParam}. As it finds
 * a child param, it will head-recursively traverse to all nested params that exist below that 
 * param.
 * 
 * <p>For each param identified that has a conditional {@code Constraint} that should be applied, 
 * this strategy will attempt to add the identified "group" of that {@code Constraint} to the 
 * respective param's {@code activeValidationGroups}. <b>Note:</b> Since this strategy follows a 
 * head-recursion pattern, it will attempt to apply the assignment of 
 * {@code activeValidationGroups} to the lowest nested param before adding to the parent 
 * param.
 * 
 * <p>A conditional {@code Constraint} that should be applied is one who's "groups" field 
 * contains the group identified by {@code configuredAnnotation.targetGroup()}.
 * 
 * <p>This strategy corresponds directly to {@link ValidationScope#CHILDREN}.
 * 
 * @author Tony Lopez
 * @since 1.0
 * @see ValidateConditional
 */
public class ChildrenValidationAssignmentStrategy extends AbstractValidationAssignmentStrategy {

	@Override
	void assignGroupTo(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup) {
		onChangeParam.traverse(p -> this.addGroupToParam(p, targetGroup), true);
	}
	
	@Override
	void unassignGroupFrom(Param<?> onChangeParam, Class<? extends ValidationGroup> targetGroup) {
		onChangeParam.traverse(p -> this.removeGroupFromParam(p, targetGroup), true);
	}

}
