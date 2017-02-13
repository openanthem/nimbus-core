/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskInitializer {

	public void initialize(QuadModel<?,?> taskModel, String initializeCriteria);
}
