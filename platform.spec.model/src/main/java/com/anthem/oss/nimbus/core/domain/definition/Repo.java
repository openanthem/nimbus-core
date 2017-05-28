/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
@Model
@Inherited
public @interface Repo {

	/* 2rd level repository: persistent stores */
	public enum Database {
		rep_none,
		rep_mongodb,
		rep_rdbms;
	}

	/* 1nd level repository: cache (distributed session or sticky) */
	public enum Cache {
		rep_none,
		rep_session,	
		rep_user,
		rep_db;
	}

	
	String alias() default "";
	
	Database value() default Database.rep_mongodb;	
	
	Cache cache() default Cache.rep_session;	
	
	boolean autoSave() default true;
}
