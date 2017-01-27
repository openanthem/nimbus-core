/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.task;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskInitializer {

	public void initialize(QuadModel<?,?> taskModel, String initializeCriteria);
}
