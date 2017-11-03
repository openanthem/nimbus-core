/**
 * 
 */
package com.anthem.oss.nimbus.core.domain;

import java.lang.annotation.Annotation;

/**
 * Marker interface for event handlers
 * 
 * @author Soham Chakravarti
 *
 */
public @interface EventHandler {

	Class<? extends Annotation> value();
}
