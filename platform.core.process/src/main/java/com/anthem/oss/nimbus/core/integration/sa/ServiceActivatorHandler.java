/**
 * 
 */
package com.anthem.oss.nimbus.core.integration.sa;


import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext.ServiceActivatorContext;

/**
 * @author AC67870
 *
 */
public interface ServiceActivatorHandler<T> {

	public T handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorException;

}
