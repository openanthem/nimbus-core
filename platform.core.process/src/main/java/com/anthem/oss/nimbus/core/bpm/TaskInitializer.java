/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;

/**
 * @author Rakesh Patel
 *
 */
public interface TaskInitializer {

	public void initialize(QuadModel<?,?> taskModel, String initializeCriteria);
}
