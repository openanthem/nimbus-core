package com.anthem.oss.nimbus.core.integration.sa;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext;
import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiContext.ServiceActivatorContext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author AC67870
 *
 */
public class ServiceExecutionHelper {
	
	@Getter @Setter private ActivitiContext aCtx;
	
	public String getExceptionName() {
		return getExceptionName(getActivityId());
	}
	
	
	public String getExceptionName(String activityId) {
		
		if(StringUtils.isBlank(activityId)) 
			return null;
		
		Throwable throwable = getException(activityId);
		
		if(throwable != null) {
			return throwable.getClass().getName();
		}
		return null;
	}
	
	
	public Throwable getException(String activityId) {
		
		if(aCtx == null || StringUtils.isBlank(activityId)) 
			return null;
		
		ServiceActivatorContext saCtx =  aCtx.lookUpServiceActivatorContext(activityId);
		
		if(saCtx != null && saCtx.getServiceExecutionHolder() != null && saCtx.getServiceExecutionHolder().getException() != null &&
				saCtx.getServiceExecutionHolder().getException().getCause() != null) {
			
			return saCtx.getServiceExecutionHolder().getException().getCause();
		}
		return null;
	}
	
	
	public boolean hasException(String exceptionTypeName) {
		
		if(StringUtils.isBlank(getActivityId())) 
			return false;
		
		return hasException(getActivityId(), exceptionTypeName);
	}


	public boolean hasException(String activityId, String exceptionTypeName) {
		
		if(aCtx == null || StringUtils.isBlank(activityId)) 
			return false;
		
		Throwable throwable = getException(activityId);
		
		if(throwable != null) {
			return StringUtils.equals(throwable.getClass().getName(), exceptionTypeName);
		}
		return false;
	}
	
	public boolean hasExceptionInstance(String exceptionTypeName) throws ClassNotFoundException {
		
		if(StringUtils.isBlank(getActivityId())) 
			return false;
		
		Class<?> clzz = Class.forName(exceptionTypeName);
		
		Throwable throwable = getException(getActivityId());
		if(clzz.isInstance(throwable)) {
			return true;
		}
		return false;
	}


	public String getActivityId() {
		return this.aCtx.getId();
	}


	public void setActivityId(String activityId) {
		this.aCtx.setId(activityId);
	}
	
	
 
}
