/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ProcessAssociation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String parentProcessId;
	
	private String parentTaskId;
	
	private String childProcessId;

}
