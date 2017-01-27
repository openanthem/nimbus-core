/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component
public class PlatformSubProcessEndListener implements JavaDelegate {

	/* (non-Javadoc)
	 * @see org.activiti.engine.delegate.JavaDelegate#execute(org.activiti.engine.delegate.DelegateExecution)
	 */
	@Override
	public void execute(DelegateExecution execution) {
		// TODO Auto-generated method stub

	}

}
