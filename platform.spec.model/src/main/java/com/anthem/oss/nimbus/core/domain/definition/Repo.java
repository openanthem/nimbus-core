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
		
		public static boolean exists(Repo repo) {
			return repo!=null && repo.value()!=Repo.Database.rep_none;
		}
	}

	/* 1nd level repository: cache (distributed session or sticky) */
	public enum Cache {
		rep_none,
		rep_device,	
		rep_user,
		rep_entity;
		
		public static boolean exists(Repo repo) {
			return repo!=null && repo.cache()!=Repo.Cache.rep_none;
		}
	}

	
	String alias() default "";
	
	Database value() default Database.rep_mongodb;	
	
	Cache cache() default Cache.rep_device;	
	
	boolean autoSave() default true;
}
