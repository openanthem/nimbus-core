/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.task;

import com.anthem.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter
public abstract class AssignmentQueue extends AbstractEntity.IdString {
	
	private static final long serialVersionUID = 1L;

	private String code;
	
	private Long userId; // change the clientUser to clientUserId (TODO change to collection)
	
	//private List<Long> tasksIds; // change the assignmentTask to assignmentTaskId
	

}
