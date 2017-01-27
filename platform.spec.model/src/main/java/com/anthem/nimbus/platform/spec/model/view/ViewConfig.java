/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.view;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.anthem.nimbus.platform.spec.model.dsl.Model;

/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Model
@Inherited
public @interface ViewConfig {

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@ViewConfig 
	public @interface ViewDomain {
		String value();
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface ViewParamBehavior {
		
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface ViewStyle {
		
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface Hints {

		public enum AlignOptions {
			Left,
			Right,
			Center,
			Inherit;
		}

		
		AlignOptions align() default AlignOptions.Inherit;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface Mode {

		
		public enum Options {
			ReadOnly,
			Hidden,
			Inherit
		}

		
		Options value() default Options.Inherit;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Page {
		public enum Type {
			Home,
			Details,
			Form,
			Static
		}

		String alias() default "page";
		
		Type type() default Type.Home;
		
		String route() default "";

		String breadCrumb() default "none";
		
		String imgSrc() default "";
		
		String styleClass() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Card {
		public enum Size {
			XSmall,
			Small,
			Medium,
			Large
		}

		String alias() default "Card";
		
		String imgSrc() default "";
		
		String title() default "";
		
		Size size() default Size.Large;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Menu {
		String alias() default "Menu";		
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Section {
		String alias() default "Section";
		
		String imgSrc() default "";
		
		String styleClass() default "";
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GlobalSection {
		String alias() default "globalSection";
		
		String imgSrc() default "";
		
		String styleClass() default "";
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Form {
		String alias() default "Form";
		
		String submitUrl() default "";
		
		String b() default "";
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Summary {
		String alias() default "summary";
		
		String imgSrc() default "";
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Button {	

		String alias() default "Button";

		String url() default "";

		String b() default "";

		String method() default "GET";
		
		String imgSrc() default "";
		
		String type() default "submit";
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Link {

		String url() default "";

		String method() default "GET";

		String b() default "$executeAnd$nav";
		
		String imgSrc() default "";
		
		String styleClass() default "";
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface TextBox {

		String alias() default "TextBox";
				
		boolean hidden() default false;
		
		boolean readOnly() default false;
		
		String help() default "";
		
		String labelClass() default "anthem-label";
		
		String type() default "text";
		
		boolean postEventOnChange() default false;
		
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ComboBox {
		String alias() default "ComboBox";
		boolean readOnly() default false;
		
		String labelClass() default "anthem-label";
		
		boolean postEventOnChange() default false;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface BreadCrumb {

		String label();

		int order();

		String alias() default "breadCrumb";
		
		boolean postEventOnChange() default false;
	}	

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiSelect {
		String alias() default "MultiSelect";
		
		String labelClass() default "anthem-label";
		
		boolean postEventOnChange() default false;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface InputDate {
		String alias() default "InputDate";
		
		String labelClass() default "anthem-label";
				
		String type() default "date";
		
		boolean postEventOnChange() default false;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Grid {

		String alias() default "Grid";

		boolean onLoad() default false;

		String url() default "";
		
		boolean postEventOnChange() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Radio {
		String alias() default "Radio";
		String labelClass() default "anthem-label";
		String level() default "0";
		String cssClass() default "";
		boolean postEventOnChange() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface CheckBox {
		String alias() default "CheckBox";
		String level() default "0";
		String cssClass() default "";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiSelectCard {
		String alias() default "MultiSelectCard";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiGrid {
		String alias() default "MultiGrid";
		String level() default "0";
		String header() default "test";
		String cssClass() default "question-header";
		boolean postEventOnChange() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface LinearGauge {
		String alias() default "LinearGauge";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ContentContainer {
		String alias() default "ContentContainer";
		String content() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface SubHeader {
		String alias() default "SubHeader";
	}
	
}
