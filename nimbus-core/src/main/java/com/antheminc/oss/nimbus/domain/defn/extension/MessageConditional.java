
package com.antheminc.oss.nimbus.domain.defn.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Context;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Type;

/**
 * <p>This annotation is used to provide control and management over conditional
 * messages for each param.
 * 
 * <p>Use this annotation when a message should be applied to a form param for a
 * specific scenario by assigning a SpEL expression to the {@code when}
 * attribute. When the {@code when} condition evaluates to {@code true},
 * &#64;{@code MessageConditional} will attempt to add a {@code message} and a
 * UI marker (CSS class) depending on {@code messageType} and {@code context} of
 * attribute. For example, see the following scenario:
 * 
 * <pre>
 * &#64;MessageConditional(when = "state > 30 ", messageType = MessageType.WARNING, message = "'BMI should be less than 30'", context = Context.INLINE)
 * private Integer bmi;
 * </pre>
 * 
 * <p>In this scenario, {@code bmi} field will have warning message "BMI should
 * be less than 30" applied and the UI(CSS class) - will be of type Warning when
 * the state of {@code bmi} is grater than {@code 30}.
 * 
 * @author Akancha Kashyap
 * @since 1.1
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.MessageConditionalHandler
 * @see com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message
 * 
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@OnStateLoad
@OnStateChange
@Repeatable(MessageConditionals.class)
public @interface MessageConditional {

	/**
	 * <p>The {@link Context} of the message. Dictates UI properties whether the
	 * message is shown inline or as a TOAST component.
	 */
	Context context() default Context.INLINE;

	/**
	 * <p>CSS class to override UI default class.
	 */
	String cssClass() default "";

	/**
	 * <p>A SpEL expression message to be displayed if the {@link #when} SpEL condition evaluates to
	 * {@code false}.
	 * <p>This value is evaluated through the SpEL expression parser. When providing
	 * a static string value, it should be encompassed in string quotes.</p>
	 * <p><b>Example</b>
	 * <pre>
	 * message = "'BMI should be less than 30'"
	 * </pre>
	 */
	String message();

	/**
	 * <p>The message {@link Type}. Dictates UI properties of the message.
	 */
	Type messageType() default Type.WARNING;

	/**
	 * <p>The order of execution this annotation should be executed in, with
	 * respect to other conditional annotations that are also decorating this
	 * param.
	 */
	int order() default Event.DEFAULT_ORDER_NUMBER;

	/**
	 * <p>SpEL based condition to be evaluated relative to param's state on
	 * which this annotation is declared.
	 */
	String when();

	/**
	 * <p>To retain the message when {@link #when()} evaluates to {@code false}.
	 */
	boolean whenElseRetainMessage() default false;
	
	/**
	 * <p>Path of param on which the messages are to be set
	 */
	String[] targetPath() default { };
}