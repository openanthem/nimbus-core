/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.entity.audit.AuditEntry;

/**
 * Configuration for leaf parameters to generate audit history on state change of the annotated parameter. <br>
 * 
 * Default implementation would throw {@linkplain InvalidConfigException} if configured on non-leaf parameter.
 * 
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateChange
public @interface Audit {

	Class<? extends AuditEntry> value();
	
}
