/**
 * 
 */
package com.antheminc.oss.nimbus.platform.spec.model.process;

import java.io.Serializable;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class ProcessEngineContext implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient Object output;
	private transient Object input;
	private transient Param<?> param;
	public ProcessEngineContext(Param<?> param){
		this.param = param;
	}
	public boolean isOutputAnException() {
		return output != null && output instanceof Exception;
	}
	
}
