/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import org.springframework.data.annotation.Transient;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("#")
@Getter @Setter
public class StateContextEntity {

	private Boolean visible = true;
	private Boolean enabled = true;
	private int order;
	
	private int count;
	
	private String message;
	
	@Transient
	private List<ParamValue> values;
}
