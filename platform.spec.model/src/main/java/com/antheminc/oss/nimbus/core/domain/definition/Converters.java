package com.antheminc.oss.nimbus.core.domain.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rakesh Patel
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Converters {
	

	Class<? extends ParamConverter<?,?>>[] converters() default { };

	public static interface ParamConverter<T, K> {
		
		public K serialize(T input);
		
		public T deserialize(K input);
		
		
	}
}
