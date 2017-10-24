/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Collections;
import java.util.List;

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

	public enum MessageType {
		INFO,
		WARNING,
		DANGER,
		SUCCESS;
	}
		
	private String msgText;
	private MessageType msgType; 
	
	private Boolean visible = true;
	private Boolean enabled = true;
	
	private Boolean active = true;
	
//	@Transient
//	private List<ParamValue> values;
	
	public List<ParamValue> getValues() {
		return Collections.emptyList();
	}
	public void setValues(List<ParamValue> v) {
		
	}
}
