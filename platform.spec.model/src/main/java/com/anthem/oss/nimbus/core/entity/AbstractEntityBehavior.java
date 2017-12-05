/**
 * 
 */
package com.antheminc.oss.nimbus.core.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractEntityBehavior<T extends AbstractEntity<ID>, ID extends Serializable> {

    @Transient
	@JsonIgnore
	final private T model;
	
    
	protected AbstractEntityBehavior(T model) {
		this.model = model;
	}
	
	
	@JsonIgnore
	public final T getModel() {
		return model;
	}
	
}
