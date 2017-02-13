/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.lang3.StringUtils;

import com.anthem.nimbus.platform.core.process.api.sa.ServiceActivatorDefinition;
import com.anthem.nimbus.platform.spec.model.process.ProcessEngineContext;
import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ActivitiContext implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private ProcessGatewayHelper processGatewayHelper;

	@Getter @Setter  @RequiredArgsConstructor
	public class ServiceActivatorContext implements Serializable {

		private static final long serialVersionUID = 1L;
	
		private transient final ServiceActivatorDefinition definition;
		
		private transient DelegateExecution delegateExecution;
		
		@Getter @Setter
		public class Holder implements Serializable {
			private static final long serialVersionUID = 1L;
			
			private transient Object input;
			private transient Object output;
			private transient Throwable exception;
		}
		
		private String id;
		private Holder requestExecutionHolder;
		private Holder serviceExecutionHolder;
		private Holder responseExecutionHolder;
		
		public ProcessEngineContext getProcessEngineContext() {
			return processEngineContext;
		}
		
		public ActivitiContext getActivitiContext() {
			return getThis();
		}
		
		@SuppressWarnings("unchecked")
		public Object getObjectOfType(Class<?> clazz) {
			Object output = this.getRequestExecutionHolder().getOutput();
			if(output instanceof Collection<?>) {
				Collection<Object> outputElems = (Collection<Object>)output;
				
				for(Object obj: outputElems) {
					if(clazz.isInstance(obj)) {
						return obj;
					}
				}
			}
			else if(clazz.isInstance(output)){
				return output;
			}
			return null;
		}
		
	}
	

	private ProcessEngineContext processEngineContext;
	
	private List<ServiceActivatorContext> serviceActivatorContexts = new LinkedList<>();
	
	private transient Class<? extends AbstractEntity<? extends Serializable>> outputClass; 
	
	private ActivitiContext getThis() {
		return this;
	}
	
	public ServiceActivatorContext createAndAddServiceActivatorContext(ServiceActivatorDefinition saDefn) {
		ServiceActivatorContext saCtx = new ServiceActivatorContext(saDefn);
		serviceActivatorContexts.add(saCtx);
		return saCtx;
	} 
	
	
	public ServiceActivatorContext lookUpServiceActivatorContext(String id) {
		for(ServiceActivatorContext ctx : this.getServiceActivatorContexts()) {
			if(StringUtils.equals(ctx.getId(), id)){
				return ctx;
			}
		}
		return null;
	}
	
	
}
