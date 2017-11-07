package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;
 
/**
 * This annotation is used to provide the access restriction: <br>
 * e.g. <p>
 * 1. Below config specifies that if a user has role1, then only read permission is allowed for testParam.
 * <pre>
 * @AccessConditional(containsRoles={"role1"}, p = Permission.READ)
 * String testParam;
 * </pre>
 * <p>
 * 2. Below config specifies that if a user has role1 or role2, then testParam will be hidden.
 * <pre>
 * @AccessConditional(containsRoles={"role1","role2"}, p = Permission.HIDDEN)
 * String testParam;
 * </pre>
 * <p>
 * 3. Below config specifies (using when()) that if a user has role1 but not role2, only then testParam will be readOnly.
 * <pre>
 * @AccessConditional(when='!?[#this == 'role1'].empty && ?[#this == 'role'].empty', p = Permission.READ)
 * String testParam;
 * </pre>
 * </p>
 * @author Rakesh Patel
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@OnStateLoad
public @interface AccessConditional {
	
	/**
	 * Use this to specify an expression for roles
	 */
	String whenRoles() default "";
	
	/**
	 * Use this to specify an expression for authorities
	 */
	String whenAuthorities() default "";
	
	/**
	 * Use for simple role contains check, for complex expression, use when()
	 */
	String[] containsRoles() default {};
	
	/**
	 * Use for simple area/accessEntity contains check, for complex expression, use when()
	 */
	String[] containsAuthority() default {};
	
	Permission p();
	
	public enum Permission {
		WRITE,
		READ,
		HIDDEN;
	}

}
