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
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Rakesh Patel
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.FIELD})
@Repeatable(AssociatedEntities.class)
public @interface AssociatedEntity {
	
	public String value() default "";
	
	public Class<?> clazz() default Class.class;
	
	public AssociationType association() default AssociationType.NONE;
	
	public FetchType fetch() default FetchType.EAGER;
	
	public CascadeType cascade() default CascadeType.ALL;
	
	public enum AssociationType {
		ONE_TO_ONE,
		ONE_TO_MANY,
		MANY_TO_ONE,
		MANY_TO_MANY,
		NONE;
	}
	public enum FetchType {
		EAGER,
		LAZY;
	}
	public enum CascadeType{
		REMOVE,
		PERSIST,
		ALL;
	}
}

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.FIELD})
@interface AssociatedEntities{
	AssociatedEntity[] value();
}