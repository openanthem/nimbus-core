/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.sa;


import com.anthem.nimbus.platform.core.process.api.ActivitiContext.ServiceActivatorContext;

/**
 * @author AC67870
 *
 */
public interface ServiceActivatorHandler<T> {

	public T handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorException;

}
