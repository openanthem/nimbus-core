/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class RuntimeEntity {

	private Boolean visible;
	private Boolean enabled;
	
	private String message;
}
