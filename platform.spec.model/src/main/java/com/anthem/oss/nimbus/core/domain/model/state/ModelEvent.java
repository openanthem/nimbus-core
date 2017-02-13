/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.command.Action;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true)
public class ModelEvent<P> extends AbstractEvent<String, P> {
	
	public ModelEvent(Action a, String path, P payload) {
		super(a.toString(), path, payload);
	}
	
	public ModelEvent(){}
	
	public String getPath() {
		return getId();
	}
}

