/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskRouter {

	public void route(QuadModel<?,?> taskModel, String assignmentCriteria);
}
