/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString
public abstract class AbstractEvent<T extends Serializable, P> {

	private String type;
	
	private T id;
	
	private P payload;

	
	public enum SuppressMode {

		ECHO
	}
	
	public enum PersistenceMode {

		ATOMIC,
		BATCH
	}
  
	
    public AbstractEvent() {}
	
	public AbstractEvent(String type, T id, P payload) {
		this.type = type;
		this.id = id;
		this.payload = payload;
	}
	
}
