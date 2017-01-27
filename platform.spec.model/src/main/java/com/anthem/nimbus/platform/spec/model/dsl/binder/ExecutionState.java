/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import com.anthem.nimbus.platform.spec.model.AbstractModel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */

@Getter @Setter
public class ExecutionState<V, C> extends AbstractModel.IdString implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private C core;

	private V view;
	
	private FlowState flow;
	
}
