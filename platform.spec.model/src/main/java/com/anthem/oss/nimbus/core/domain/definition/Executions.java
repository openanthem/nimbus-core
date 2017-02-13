/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.Execution.Input;
import com.anthem.oss.nimbus.core.domain.definition.Execution.InputParam;
import com.anthem.oss.nimbus.core.domain.definition.Execution.Output;
import com.anthem.oss.nimbus.core.domain.definition.Execution.OutputParam;

/**
 * @author Soham Chakravarti
 *
 */
public class Executions {
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Inputs {
		Input[] value();
	}
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface InputParams {
		InputParam[] value();
	}
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	public @interface Outputs {
		Output[] value();
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface OutputParams {
		OutputParam[] value();
	}
	
}
