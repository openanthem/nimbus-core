package com.anthem.oss.nimbus.core.domain.definition;

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
	
	public AssociationType association() default AssociationType.none;
	
	public FetchType fetch() default FetchType.EAGER;
	
	public CascadeType cascade() default CascadeType.ALL;
	
	public enum AssociationType {
		oneToOne,
		oneToMany,
		manyToOne,
		manyToMany,
		none;
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