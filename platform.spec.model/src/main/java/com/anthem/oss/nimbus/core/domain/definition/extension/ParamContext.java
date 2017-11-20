package com.anthem.oss.nimbus.core.domain.definition.extension;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.anthem.oss.nimbus.core.domain.definition.event.StateEvent.OnStateLoad;

/**
 * <p>ParamContext is used to set the contextual properties of a field during the OnStateLoad event. 
 * Contextual properties include those such as: visible, enabled, etc. It is likely that as the framework
 * evolves, additional values will be defined within ParamContext.
 * 
 * <p>The intent of <tt>ParamContext</tt> is to be able to decorate fields with default behavior.
 * For example:</p>
 * 
 * <pre>
 * public static class SampleView {
 * 
 *     &#64;Modal
 *     &#64;ParamContext(enabled=true, visible=true)
 *     private MyModal myModal;
 *     
 *     public static class MyModal { ... }
 * }
 * </pre>
 * 
 * <p>In this scenario we have configured the contextual values for enabled and visible to be
 * <b>true</b>. These values will be set during the <tt>OnStateLoad</tt> event.</p>
 * 
 * @author Soham Chakravarti, Tony Lopez (AF42192)
 * @see com.anthem.oss.nimbus.core.domain.model.state.extension.ParamContextStateEventHandler
 *
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD, ANNOTATION_TYPE})
@OnStateLoad
public @interface ParamContext {

	/**
	 * Whether or not the decorated target should be visible.
	 */
	boolean visible();
	
	/**
	 * Whether or not the decorated target should be enabled.
	 */
	boolean enabled();
	
}
