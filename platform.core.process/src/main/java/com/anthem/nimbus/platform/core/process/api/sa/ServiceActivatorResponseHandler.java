/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.sa;

import java.io.Serializable;

import com.anthem.nimbus.platform.core.process.api.ActivitiContext.ServiceActivatorContext;
import com.anthem.nimbus.platform.spec.model.AbstractModel;

/**
 * @author AC67870
 *
 */
public interface ServiceActivatorResponseHandler<T> {

	public T handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorException;

}
