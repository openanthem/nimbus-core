/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface Repo {

	public enum Option {
		/* 3rd level repository: persistent stores */
		rep_mongodb,
		rep_neo4j,
		rep_rdbms,
		
		/* 2nd level repository: distributed cache */
		rep_redis,
		
		/* 1st level repository: local */
		rep_thread,
		rep_jvm;
	}
	
	Option value();	//3rd level
	
	Option cache() default Option.rep_thread;	//2nd level
	
	public static final Option DEFAULT = Option.rep_thread; 
}
