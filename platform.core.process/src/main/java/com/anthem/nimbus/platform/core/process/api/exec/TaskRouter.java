/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskRouter {

	public void route(QuadModel<?,?> taskModel, String assignmentCriteria);
}
