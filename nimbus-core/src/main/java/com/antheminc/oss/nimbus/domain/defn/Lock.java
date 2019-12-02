/**
 *  Copyright 2016-2019 the original author or authors.
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;

/**
 * @author Sandeep Mantha
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.TYPE)
public @interface Lock {
	
	/**
	 * <p>Set the alias if sub-locking feature is needed
	 */
	public String alias() default "";
	
	/**
	 * <p>Configs set would be executed to obtain the lock on an entity
	 */
	public Config[] executeToAcquireLock();

	/**
	 * <p>Configs set would be executed when lock is acquired on an entity
	 */
	public Config[] executeWhenLockAcquired() default {};
	
	/**
	 * <p>Configs set would be executed when lock is not acquired on an entity
	 */
	public Config[] executeWhenLockNotAcquired() default {};
	
	
	public LockedBy lockedBy();
	
	
	enum LockedBy {
		session,
		user,
		custom
	}
}

