/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent;

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

