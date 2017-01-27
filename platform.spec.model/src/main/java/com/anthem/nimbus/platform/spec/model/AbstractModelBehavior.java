/**
 * 
 */
package com.anthem.nimbus.platform.spec.model;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractModelBehavior<T extends AbstractModel<ID>, ID extends Serializable> {

    @Transient
	@JsonIgnore
	final private T model;
	
    
	protected AbstractModelBehavior(T model) {
		this.model = model;
	}
	
	
	@JsonIgnore
	public final T getModel() {
		return model;
	}
	
}
