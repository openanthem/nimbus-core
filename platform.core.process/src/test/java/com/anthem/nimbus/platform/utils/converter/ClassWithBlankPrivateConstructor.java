/**
 * 
 */
package com.anthem.nimbus.platform.utils.converter;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class ClassWithBlankPrivateConstructor {
	private String needed;

	@SuppressWarnings("unused")
	private ClassWithBlankPrivateConstructor() {}
	
	public ClassWithBlankPrivateConstructor(String needed) {
		this.needed = needed;
	}
}
