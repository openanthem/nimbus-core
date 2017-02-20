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
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Model
public @interface Domain {

	String value();
	
	ListenerType[] includeListeners() default { };
	
	enum ListenerType {
		none,
		websocket,
		persistence
		
	}
}
