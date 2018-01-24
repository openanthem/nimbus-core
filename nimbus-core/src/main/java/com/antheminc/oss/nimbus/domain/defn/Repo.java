/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.defn;

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
		rep_rdbms,
		rep_ws;
		
		public static boolean exists(Repo repo) {
			return repo!=null && repo.value() != Repo.Database.rep_none;
		}
		
		public static boolean isPersistable(Repo repo) {
			return exists(repo) && (repo.value() == Database.rep_mongodb || repo.value() == Database.rep_rdbms);
		}
	}

	/* 1nd level repository: cache (distributed session or sticky) */
	public enum Cache {
		rep_none,
		rep_device;
//		rep_user,
//		rep_entity;
		
		public static boolean exists(Repo repo) {
			return repo!=null && repo.cache()!=Repo.Cache.rep_none;
		}
	}

	
	String alias() default "";
	
	Database value();	
	
	Cache cache() default Cache.rep_device;	
	
	boolean autoSave() default true;
	
	NamedNativeQuery[] namedNativeQueries() default {};
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.TYPE)
	@interface NamedNativeQuery {
		
		String name();
		String[] nativeQueries();
	}
	
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target(value=ElementType.TYPE)
//	@interface NativeQuery {
//		
//		String query();
//	}
	
}
