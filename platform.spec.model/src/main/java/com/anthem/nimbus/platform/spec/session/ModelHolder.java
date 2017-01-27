/**
 * 
 */
package com.anthem.nimbus.platform.spec.session;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ModelHolder implements Serializable{
	private static final long serialVersionUID = 1L;
	Class<?> referredClass;	
}
