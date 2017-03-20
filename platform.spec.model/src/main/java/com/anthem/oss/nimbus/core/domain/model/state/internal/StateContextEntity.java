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
public class StateContextEntity {

	private Boolean visible = true;
	private Boolean enabled = true;
	
	private int count;
	
	private String message;
}
