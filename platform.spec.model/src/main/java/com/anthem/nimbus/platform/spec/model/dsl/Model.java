/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Model {


	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Param {
		
		
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		public @interface Values {
			
			
			public static interface Source {
				public List<ParamValue> getValues(String paramCode);
			}
			
			Class<? extends Source> value();
		}
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		public @interface Text {
			String label();
		}
	}
	
}
