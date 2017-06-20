/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Soham Chakravarti
 *
 */
public class MapsTo {

	public enum Mode {
		UnMapped,
		MappedAttached,
		MappedDetached;
	}
	
	public enum State {
		External,
		Internal,
		Associated;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Model
	@Inherited
	public @interface Mapped {
		
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Mapped
	public @interface Type {
		
		Class<?> value();

		/**
		 * throw exception if unmapped property is found. Defaults to silent, i.e., no exception thrown 
		 */
		boolean silent() default true;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	public @interface Path {
		
		String value() default "";
		
		boolean linked() default true;
		
		State state() default State.Internal;
		
		String colElemPath() default DEFAULT_COL_ELEM_PATH;

	}
	
	public static Mode getMode(Path path) {
		if(path==null) return Mode.UnMapped;
		
		return path.linked() ? Mode.MappedAttached : Mode.MappedDetached;
	}
	
	public static boolean hasCollectionPath(Path path) {
		return StringUtils.trimToNull(path.colElemPath())!=null;
	}
	
	public static final String DETACHED_SIMULATED_FIELD_NAME = "detachedParam";
	public static final String DEFAULT_COL_ELEM_PATH = "";
}
