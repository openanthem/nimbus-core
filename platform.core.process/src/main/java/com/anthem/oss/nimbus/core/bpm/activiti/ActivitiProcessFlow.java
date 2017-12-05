/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm.activiti;

import java.util.List;

import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ActivitiProcessFlow extends ProcessFlow {
	private static final long serialVersionUID = 1L;
	private String processDefinitionId;
	private List<String> activeTasks;
}
