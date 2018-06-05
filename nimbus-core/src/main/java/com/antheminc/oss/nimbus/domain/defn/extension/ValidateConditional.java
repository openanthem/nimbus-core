package com.antheminc.oss.nimbus.domain.defn.extension;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Payload;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateChange;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;

/**
 * <p>This annotation is used to provide control and management over conditional validations to be 
 * applied to a subset of params.</p>
 * 
 * <p>Use this annotation when a validation(s) should only be applied for a specific scenario by 
 * assigning a SpEL expression to the <tt>when</tt> attribute. When the <tt>when</tt> condition 
 * evaluates to <tt>true</tt>, <tt>&#64;ValidateConditional</tt> will attempt to identify a subset
 * of params for which to conditionally apply validations to. Validations will only be applied to
 * params that have a group identifier that matches this annotation's <tt>targetGroup</tt>
 * attribute. For example, see the following scenario:</p>
 * 
 * <pre>
 * &#64;ValidateConditional(when = "state == 'Y'", targetGroup = GROUP_0.class)
 * private String status;
 * 
 * &#64;NotNull(groups = { GROUP_0.class, GROUP_1.class })
 * &#64;TextBox
 * private String statusReason;
 * </pre>
 * 
 * <p>In this scenario, <tt>statusReason</tt> will only have validation logic applied when the 
 * state of <tt>status</tt> is equal to <tt>Y</tt>.</p>
 * 
 * <p>Note in the previous example that multiple group marker classes were used for 
 * <tt>statusReason</tt>. Although not utilized in this example, this shows that different 
 * combinations of <tt>&#64;ValidateConditional</tt> and group configurations can be used to control 
 * the subset of params for which to apply validation logic to. In addition, different scopes are 
 * able to be defined for this annotation's <tt>scope</tt> attribute to assist in that control.</p>
 * 
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.model.state.extension.ValidateConditionalStateEventHandler
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
@Repeatable(ValidateConditionals.class)
@OnStateLoad @OnStateChange
public @interface ValidateConditional {

	/**
	 *  <p>SpEL based condition to be evaluated relative to param's state on which this 
	 *  annotation is declared.</p>
	 */
	String when();
	
	/**
	 * <p>Specifies which validation group should be applied when this annotation's
	 * <tt>when</tt> condition evaluates to <tt>true</tt>.</p>
	 * 
	 * <p>There are several pre-defined validation group classes defined for convienence
	 * within <tt>ValidateConditional</tt> that can be used to identify a target validation
	 * group. Simply use one of these pre-defined classes or create an implementation of
	 * <tt>ValidationGroup</tt> if needed.</p>
	 * 
	 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup
	 */
	Class<? extends ValidationGroup> targetGroup();
	
	/**
	 * <p>Defines the scope for the param layer at which validations will be applied when 
	 * this annotation's <tt>when</tt> condition evaluates to <tt>true</tt>.</p>
	 * 
	 * <p>Consider setting the scope when needing to control a specific subset of params
	 * to apply validation to. The default scope is: <b>SIBLING</b>.</p>
	 * 
	 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope
	 */
	ValidationScope scope() default ValidationScope.SIBLING;
	
	/**
	 * <p>Marker interface used in conjunction with <tt>targetGroup</tt>. This interface 
	 * is used as an implementation to identify a subset of params in which 
	 * <tt>&#64;ValidateConditional</tt> should apply validations to when it's <tt>when</tt> 
	 * condition evaluates to <tt>true</tt>.</p>
	 * 
	 * <p>For convenience, a set of identify class implementations have been defined within 
	 * <tt>ValidateConditional</tt> as <tt>Group_<b>X</b></tt>, where 0 &#8804; <b>X</b> &#8804; 29. 
	 * If additional marker classes are needed, simply create a new implementation of 
	 * <tt>ValidationGroup</tt> and use that class in the <tt>targetGroup</tt> property and the 
	 * corresponding param.</p>
	 */
	public interface ValidationGroup extends Payload {};
	
	public interface GROUP_0 extends ValidationGroup {};
	public interface GROUP_1 extends ValidationGroup {};
	public interface GROUP_2 extends ValidationGroup {};
	public interface GROUP_3 extends ValidationGroup {};
	public interface GROUP_4 extends ValidationGroup {};
	public interface GROUP_5 extends ValidationGroup {};
	public interface GROUP_6 extends ValidationGroup {};
	public interface GROUP_7 extends ValidationGroup {};
	public interface GROUP_8 extends ValidationGroup {};
	public interface GROUP_9 extends ValidationGroup {};
	public interface GROUP_10 extends ValidationGroup {};
	public interface GROUP_11 extends ValidationGroup {};
	public interface GROUP_12 extends ValidationGroup {};
	public interface GROUP_13 extends ValidationGroup {};
	public interface GROUP_14 extends ValidationGroup {};
	public interface GROUP_15 extends ValidationGroup {};
	public interface GROUP_16 extends ValidationGroup {};
	public interface GROUP_17 extends ValidationGroup {};
	public interface GROUP_18 extends ValidationGroup {};
	public interface GROUP_19 extends ValidationGroup {};
	public interface GROUP_20 extends ValidationGroup {};
	public interface GROUP_21 extends ValidationGroup {};
	public interface GROUP_22 extends ValidationGroup {};
	public interface GROUP_23 extends ValidationGroup {};
	public interface GROUP_24 extends ValidationGroup {};
	public interface GROUP_25 extends ValidationGroup {};
	public interface GROUP_26 extends ValidationGroup {};
	public interface GROUP_27 extends ValidationGroup {};
	public interface GROUP_28 extends ValidationGroup {};
	public interface GROUP_29 extends ValidationGroup {};
	
	/**
	 * <p>The enumerated set of ValidationScope's available for use in 
	 * <tt>ValidateConditional.scope</tt>.</p>
	 * 
	 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional#scope()
	 * @author Tony Lopez
	 *
	 */
	public enum ValidationScope {
		
		/**
		 * <p>Applies validations to sibling params relative to the current param on which this 
		 * annotation is defined.</p>
		 */
		SIBLING,
		
		/**
		 * <p>Applies validations to sibling params relative to the current param on which this 
		 * annotation is defined. Also recursively traverses each of the previous param's nested 
		 * params (or children) and applies validations.</p>
		 */
		SIBLING_NESTED;
	}
	
	int order() default Event.DEFAULT_ORDER_NUMBER;
}
