
package com.antheminc.oss.nimbus.domain.defn.extension;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Context;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Type;


/**
<p>This annotation is used to provide control and management over conditional messages for each param</p>
  
 * <p>Use this annotation when a message should be applied to a form param for a specific scenario by 
 * assigning a SpEL expression to the <i><tt>when</tt></i> attribute. When the <i><tt>when</tt></i> condition 
 * evaluates to <tt>true</tt>, <i><tt>&#64;MessageConditional</tt></i> will attempt to add a <i><tt>message</tt></i> and a UI marker (css class)
 * depending on  <i><tt>messageType</tt></i> and <i><tt>context</tt></i> of 
 * attribute. For example, see the following scenario:</p>
 * 
 * <pre>
 * &#64;MessageConditional(when = "state > 30 ", messageType = MessageType.WARNING,message="BMI should be less than 30", context= Context.INLINE )
 * private Integer bmi;
 * 
 * 
 * <p>In this scenario, <tt>bmi</tt>  field will have warning message "BMI should be less than 30" applied and the UI(css class) - will be of type Warning when the 
 * state of <tt>bmi</tt> is grater than <tt>30</tt>.</p>
 * 
 * 
 * @author Akancha Kashyap
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.MessageConditionalHandler
 *
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@OnStateLoad @OnStateChange
public @interface MessageConditional {
	
	/**
	 *  SpEL based condition to be evaluated relative to param's state on which this annotation is declared.
	 */
	String when();
	/**
	 *  The  Message displayed if the Spel condition evaluates to false.
	 */
	String message();
	/**
	 *  The Type of Message - dictates UI properties of the Message
	 */
	Type messageType() default Type.WARNING;
	/**
	 *  The Context of Message - dictates UI properties whether the message is shown inline or Growl
	 */
	Context context() default Context.INLINE;
	/**
	 *  To retain the Message when "when" evaluates to false - 
	 */
	boolean whenElseRetainMessage() default false;
	
	int order() default Event.DEFAULT_ORDER_NUMBER;
}