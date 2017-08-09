package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Model {
	
	/**
	 * alias
	 */
	String value() default "";
	
	String rules() default "drools";
	
	// list of listeners to exclude at a nested model level. Use only to exclude a particular listener defined at a root Domain level
	ListenerType[] excludeListeners() default { };
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Param {
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		public @interface Values {
			
			public static interface Source {
				public List<ParamValue> getValues(String paramCode);
			}
			
			Class<? extends Source> value() default Source.class;
			
			String url() default "staticCodeValue";
		}
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		public @interface Text {
			String label();
		}
	}
	
}
