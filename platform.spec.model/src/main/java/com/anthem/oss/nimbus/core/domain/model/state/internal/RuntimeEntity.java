/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import com.anthem.oss.nimbus.core.domain.definition.Model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class RuntimeEntity {

	private Boolean visible;
	private Boolean enabled;
	
	private String message;
}
