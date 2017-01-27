/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.task;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskRouter {

	public void route(QuadModel<?,?> taskModel, String assignmentCriteria);
}
