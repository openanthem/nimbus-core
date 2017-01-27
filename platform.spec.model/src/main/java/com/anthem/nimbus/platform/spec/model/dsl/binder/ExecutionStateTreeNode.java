/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */

@Getter @Setter
public class ExecutionStateTreeNode implements Serializable {
	private static final long serialVersionUID = 1L;
	private String paramPath;
	private Object oldState;
	private Object newState;
}
