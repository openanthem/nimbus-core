package com.anthem.oss.nimbus.core.bpm;

import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Sandeep Mantha
 *
 */
@Getter @Setter
public class BPMProcessBean implements HierarchyMatch {
	
	private String processId;
	
	@Override
	public String getUri() {
		return getProcessId();
	}
	
}
