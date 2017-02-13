package com.anthem.nimbus.platform.core.process.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Sandeep Mantha
 *
 */
@Getter @Setter
public class ProcessBean implements HierarchyMatch {
	
	private String processId;
	
	@Autowired
	ActivitiProcessGateway gateway;

	@Override
	public String getUri() {
		return getProcessId();
	}
	
}
