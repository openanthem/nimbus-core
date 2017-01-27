/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.sa;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.anthem.nimbus.platform.core.process.api.ActivitiContext.ServiceActivatorContext;

/**
 * @author Rakesh Patel
 *
 */
@Component
public class DefaultExecutionHandler implements ServiceActivatorHandler<Object>, ApplicationContextAware {

	ApplicationContext context;
	
	@Override
	public Object handle(ServiceActivatorContext srvcActCtx) throws ServiceActivatorExecutionException {
		try {
			Object service = this.context.getBean(srvcActCtx.getDefinition().getServiceName());
			Method method = service.getClass().getDeclaredMethod(srvcActCtx.getDefinition().getServiceMethod(), ServiceActivatorContext.class);
		
			Object output = ReflectionUtils.invokeMethod(method, service, srvcActCtx);
			return output;
		}
		catch(UndeclaredThrowableException ex) {
			throw new ServiceActivatorExecutionException("ServiceActivator - Execution step failed for - service: "
					+srvcActCtx.getDefinition().getServiceName()+" - method: "+srvcActCtx.getDefinition().getServiceMethod() ,ex.getUndeclaredThrowable());
		}
		catch(Exception e) {
			throw new ServiceActivatorExecutionException("ServiceActivator - Execution step failed for - service: "
							+srvcActCtx.getDefinition().getServiceName()+" - method: "+srvcActCtx.getDefinition().getServiceMethod() ,e);
		}
	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
		
	}

}
