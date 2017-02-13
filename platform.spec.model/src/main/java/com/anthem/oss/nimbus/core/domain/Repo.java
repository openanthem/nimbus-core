/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

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

	/* 3rd level repository: persistent stores */
	public enum Database {
		rep_none,
		rep_mongodb,
		rep_neo4j,
		rep_rdbms;
	}

	/* 2nd level repository: distributed cache */
	public enum Cache {
		rep_none,
		rep_redis,
		rep_thread,
		rep_jvm;
	}
	
	/* 1st level repository: local cache */
	public enum Local {
		rep_none,
	}
	
	Database value() default Database.rep_none;	
	
	Cache[] cache() default Cache.rep_none;	
	
	Local local() default Local.rep_none;
}
