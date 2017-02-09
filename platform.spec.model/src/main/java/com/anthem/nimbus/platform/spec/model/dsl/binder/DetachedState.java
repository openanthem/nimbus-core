/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @RequiredArgsConstructor
public class DetachedState<V, D> extends ExecutionState<V, D> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public D getDetached() {
		return getCore();
	}
	public void setDetached(D detached) {
		setCore(detached);
	}
}
