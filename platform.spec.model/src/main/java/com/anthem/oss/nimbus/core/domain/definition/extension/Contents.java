/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.core.domain.RepeatContainer;
import com.antheminc.oss.nimbus.core.domain.definition.extension.Content.Label;

/**
 * @author Soham Chakravarti
 *
 */
public final class Contents {

	@Documented
	@Retention(RUNTIME)
	@Target(FIELD)
	//@RepeatContainer
	public @interface Labels {

		Label[]  value();
	}
}
