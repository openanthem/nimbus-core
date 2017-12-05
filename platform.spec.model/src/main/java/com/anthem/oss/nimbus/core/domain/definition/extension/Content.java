/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Locale;

import com.antheminc.oss.nimbus.core.domain.definition.event.ConfigEvent.OnParamCreate;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Contents.Labels;

/**
 * @author Soham Chakravarti
 *
 */
public final class Content {

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	@Repeatable(Labels.class)
	@OnParamCreate
	public @interface Label {
		
		public static final String DEFAULT_LOCALE = Locale.getDefault().toLanguageTag();
		
		String value();
		
		String locale() default "";
	}
}
