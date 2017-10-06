/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateChange;
import com.anthem.oss.nimbus.core.entity.audit.AuditEntry;

/**
 * @author Soham Chakravarti
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateChange
public @interface Audit {

	public static final Class<?> DEFAULT_ENTRY_ENTITY = AuditEntry.class;
	
	public static final Class<?> DEFAULT_AUDITED_ENTITY = Domain.class;
	
	
	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when() default "true";
	
	/**
	 * 
	 * If the default value is not overridden, then the f/w would use {@linkplain AuditEntry} for creating audit entries. <br>
	 * The naming convention would follow {domain-alias of entity being audited}_{domain-alias of audit entry}. <br>
	 * As an example, an entity with domain-alias "car" would have default audit history captured in collection with name "car_audit_history".
	 */
	Class<?> entry() default AuditEntry.class;
	
	/**
	 *  Domain alias of entity being audited. <br>
	 *  If value is not overridden, f/w would auto look-up domain-root entity and use its domain-alias.
	 */
	Class<?> auditedEntity() default Domain.class;
	
	/**
	 * Path relative to param on which this annotation is declared to get audited entity id. <br>
	 * If value is not overridden, then the f/w would auto look-up domain-root entity which is can be persisted (view first, then core) and use its id. 
	 */
	String auditedEntityIdPath() default "";
}
