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
import com.antheminc.oss.nimbus.InvalidConfigException;

/**
 * <p>This annotation is used to provide control and management over conditional validations to be 
 * applied to a subset of params.
 * 
 * <p>Use this annotation when a validation(s) should only be applied for a specific scenario by 
 * assigning a SpEL expression to the {@link #when()} attribute. When the {@link #when()} condition 
 * evaluates to {@code true}, &#64;{@code ValidateConditional} will attempt to identify a subset
 * of params for which to conditionally apply validations to. Validations will only be applied to
 * params that have a group identifier that matches this annotation's {@link #targetGroup()}
 * attribute. For example, see the following scenario:
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
 * <p>In this scenario, {@code statusReason} will only have validation logic applied when the 
 * state of {@code status} is equal to {@code Y}.
 * 
 * <p>Note in the previous example that multiple group marker classes were used for 
 * {@code statusReason}. Although not utilized in this example, this shows that different 
 * combinations of &#64;{@code ValidateConditional} and group configurations can be used to control 
 * the subset of params for which to apply validation logic to. In addition, different scopes are 
 * able to be defined for this annotation's {@link #scope()} attribute to assist in that control.
 * 
 * @author Tony Lopez
 * @since 1.0
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
	 *  annotation is declared.
	 */
	String when();
	
	/**
	 * <p>Specifies which validation group should be applied when this annotation's
	 * {@link #when()} condition evaluates to {@code true}.
	 * <p>There are several pre-defined validation group classes defined for convenience
	 * within {@code ValidateConditional} that can be used to identify a target validation
	 * group. Simply use one of these pre-defined classes or create an implementation of
	 * {@link ValidationGroup} if needed.
	 */
	Class<? extends ValidationGroup> targetGroup();
	
	/**
	 * <p>The path(s) to the param for which to apply validation logic to when this annotation's 
	 * {@link #when()} condition is {@code true}. This path is relative to the param on which this
	 * &#64;{@code ValidateConditional} decorates. If not provided, the target path points to
	 * the param in which this {@code ValidateConditional} decorates.
	 * <p>If any {@code scanPath} is provided and unable to be located, a {@link InvalidConfigException} 
	 * will be thrown.
	 * <p><b>Note:</b> This behavior works hand-in-hand with {@link #scope()} logic. When combined,
	 * they can be used to perform flexible targeting of which params should have validation logic 
	 * applied.
	 */
	String[] scanPath() default {};
	
	/**
	 * <p>Defines the scope for the param layer at which validations will be applied when 
	 * this annotation's {@link #when()} condition evaluates to {@code true}.
	 * <p>Consider setting the scope when needing to control a specific subset of params
	 * to apply validation to. The default scope is: {@link ValidationScope#SIBLING}.
	 * @see com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationScope
	 */
	ValidationScope scope() default ValidationScope.SIBLING;
	
	/**
	 * <p>Marker interface used in conjunction with {@code targetGroup}. This interface 
	 * is used as an implementation to identify a subset of params in which 
	 * &#64;{@code ValidateConditional} should apply validations to when it's {@link #when()} 
	 * condition evaluates to {@code true}.
	 * <p>For convenience, a set of identify class implementations have been defined within 
	 * {@code ValidateConditional} as {@code Group_<b>X</b>}, where 0 &#8804; <b>X</b> &#8804; 29. 
	 * If additional marker classes are needed, simply create a new implementation of 
	 * {@code ValidationGroup} and use that class in the {@code targetGroup} property and the 
	 * corresponding param.
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
	 * {@link ValidateConditional#scope()}.
	 * 
	 * @author Tony Lopez
	 *
	 */
	public enum ValidationScope {
		
		/**
		 * <p>Applies validations to children params relative to the {@link ValidateConditional#scanPath()} 
		 * defined in &#64;{@code ValidateConditional}.
		 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.ChildrenValidationAssignmentStrategy
		 */
		CHILDREN,
		
		/**
		 * <p>Applies validations to children params relative to the {@link ValidateConditional#scanPath()} 
		 * defined in &#64;{@code ValidateConditional}. Also recursively traverses each of the previous param's 
		 * nested params (or children) and applies validations.
		 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.ChildrenValidationAssignmentStrategy
		 */
		CHILDREN_NESTED,
		
		/**
		 * <p>Applies validations to sibling params relative to the {@link ValidateConditional#scanPath()} 
		 * defined in &#64;{@code ValidateConditional}.
		 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingValidationAssignmentStrategy
		 */
		SIBLING,
		
		/**
		 * <p>Applies validations to sibling params relative to the {@link ValidateConditional#scanPath()} 
		 * defined in &#64;{@code ValidateConditional}. Also recursively traverses each 
		 * of the previous param's nested params (or children) and applies validations.
		 * @see com.antheminc.oss.nimbus.domain.model.state.extension.validateconditional.SiblingNestedValidationAssignmentStrategy
		 */
		SIBLING_NESTED;
	}
	
	int order() default Event.DEFAULT_ORDER_NUMBER;
}
