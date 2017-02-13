/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Soham Chakravarti
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DefaultQualifiers.class)
@Qualifier
public @interface DefaultQualifier {

	String value();
}
