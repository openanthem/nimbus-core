/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
public interface ConfigNature {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.FIELD)
	public @interface Ignore {}
	
}
