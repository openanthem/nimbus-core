/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.core.domain.definition.Execution.Config;

/**
 * @author Soham Chakravarti
 *
 */
public class Executions {
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@Execution
	public @interface Configs {
		Config[] value();
	}
	
}
