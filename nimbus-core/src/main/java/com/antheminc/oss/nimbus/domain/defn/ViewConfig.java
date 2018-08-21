/**
 *  Copyright 2016-2018 the original author or authors.
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
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.Event;
import com.antheminc.oss.nimbus.domain.defn.event.StateEvent.OnStateLoad;
import com.antheminc.oss.nimbus.domain.defn.extension.ParamContext;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Contains all config annotations used for marking fields as "view" components, or objects
 * that the UI should render.
 * 
 * @since 1.0
 * @author Soham Chakravarti
 * @author Dinakar Meda
 * @author Swetha Vemuri
 * @author Sandeep Mantha
 * @author Rakesh Patel
 * @author Vivek Kamenini
 * @author Tony Lopez
 */
public class ViewConfig {
	
	/**
	 * <p>This Accordion is intended for use within a {@link Form} component. For non-form support, consider
	 * {@link AccordionMain}.
	 * 
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Accordion will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>Accordion will render nested fields in the same manner declared directly under a {@link Form} 
	 * component.
	 * 
	 * @since 1.0
	 * @see com.antheminc.oss.nimbus.domain.defn.Form
	 */
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ ElementType.FIELD })
//	@ViewStyle
//	public @interface Accordion {
//		String alias() default "Accordion";
//		String cssClass() default "panel-default";
//	}
	
	/**
	 * <!--TODO Javadoc-->
	 * 
	 * @since 1.1
	 */
	@Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.FIELD })
    @ViewStyle
    public @interface TabInfo {
        String alias() default "TabInfo";
        String info() default "";
        String cssClass() default "";
    }

	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ ElementType.FIELD })
//	@ViewStyle
//	public @interface AccordionGroup {
//		String alias() default "AccordionGroup";
//		String cssClass() default "panel-default";
//	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>AccordionMain will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>AccordionMain should contain nested classes. These classes represent an <i>accordion tab</i> within the 
	 * rendered accordion. The <i>accordion tab</i> class should contain a field decorated with {@link CardDetail}.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@ViewStyle
	public @interface Accordion {
		String activeIndex() default "0";
		String alias() default "Accordion";
		String cssClass() default "panel-default";
		boolean multiple() default false;
		boolean showExpandAll() default false;
	}
	
	/**
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@ViewStyle
	public @interface AccordionTab {
		String alias() default "AccordionTab";
		String cssClass() default "panel-default";
		boolean editable() default false;
		boolean selected() default false;
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>ActionTray can only be contained within a View Layout definition.
	 * 
	 * <p>ActionTray will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Button}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ActionTray {
		public enum Type {
			ActionTray;
		}
		String alias() default "ActionTray";
		String cssClass() default "";		
		String text() default "";
		Type value() default Type.ActionTray;
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Assessment {	
		String alias() default "Assessment";
		String cssClass() default "text-sm-right";
	}

	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface BreadCrumb {
		String alias() default "breadCrumb";
		String label();
		int order();
		String cssClass() default "";
		boolean postEventOnChange() default false;
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Button will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link ActionTray}</li>
	 * <li>{@link ButtonGroup}</li>
	 * <li>{@link Form}</li>
	 * <li>{@link Grid}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>Button should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Button {	
		public enum Style {
			DESTRUCTIVE,
			PLAIN,
			PRIMARY,
			SECONDARY;
		}
		
		public enum Type {
			button,
			reset,
			submit
		}
		
		String alias() default "Button";
		String b() default "$execute";
		boolean browserBack() default false;
		String cssClass() default "";
		boolean formReset() default true;
		String imgSrc() default "";
		Image.Type imgType() default Image.Type.FA;
		String method() default "GET";
		String payload() default "";
		Style style() default Style.PLAIN;
		String title() default "";
		Type type() default Type.button;
		String url() default "";
	}
	
	/**
	 * <p>Container for buttons
	 * 
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>ButtonGroup will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>ButtonGroup will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Button}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ButtonGroup {	
		String alias() default "ButtonGroup";
		String cssClass() default "text-sm-center";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Calendar will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>Calendar should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Calendar {
		String alias() default "Calendar";
		String controlId() default "";	
		String help() default "";
		String hourFormat() default "12";
		String labelClass() default "anthem-label";
		boolean monthNavigator() default false;
		boolean postEventOnChange() default false;
		boolean readOnly() default false;
		boolean readonlyInput() default false;
		boolean showTime() default false;
		boolean timeOnly() default false;	
		String type() default "calendar";
		boolean yearNavigator() default false;
		String yearRange() default "1910:2050";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>CardDetail will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Accordion}</li>
	 * <li>{@link AccordionTab}</li><!-- TODO Candidate for removal --!>
	 * <li>{@link CardDetailsGrid}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>CardDetail will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link CardDetailsHeader}</li>
	 * <li>{@link CardDetailsBody}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface CardDetail {
		
		/**
		 * <p><b>Expected Field Structure</b>
		 * 
		 * <p>CardDetail.Body will be rendered when annotating a field nested under one of the following components:
		 * <ul>
		 * <li>{@link CardDetail}</li>
		 * </ul>
		 * 
		 * <p>CardDetail.Body will render nested fields that are decorated with:
		 * <ul>
		 * <li>{@link FieldValue}</li>
		 * <li>{@link Link}</li>
		 * <li>{@link StaticText}</li>
		 * <li>{@link Paragraph}</li>
		 * </ul>
		 * 
		 * @since 1.0
		 */
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		@ViewStyle
		public @interface Body {
			String alias() default "CardDetailsBody";
			String cssClass() default "";
		}
		
		/**
		 * <p><b>Expected Field Structure</b>
		 * 
		 * <p>CardDetail.Header will be rendered when annotating a field nested under one of the following components:
		 * <ul>
		 * <li>{@link CardDetail}</li>
		 * </ul>
		 * 
		 * <p>CardDetail.Header will render nested fields that are decorated with:
		 * <ul>
		 * <li>{@link FieldValue}</li>
		 * <li>{@link Paragraph}</li>
		 * <li>{@link ButtonGroup}</li>
		 * </ul>
		 * 
		 * @since 1.0
		 */
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		@ViewStyle
		public @interface Header {
			String alias() default "CardDetailsHeader";
			String cssClass() default "";
		}
		
		/**
		 * <!--TODO Candidate for removal-->
		 * 
		 * @since 1.0
		 */
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.FIELD})
		@ViewStyle
		public @interface Tag {
			String alias() default "CardDetailsTag";
			String cssClass() default "";
		}
		
		String alias() default "CardDetail";
		String cssClass() default "";
		boolean draggable() default false;
		boolean editable() default false;
		String imgSrc() default "";
		String modelPath() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>CardDetailsGrid will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>A field decorated with &#64;CardDetailsGrid should be an object containing one or more fields.
	 * Each of these fields would represent a <i>card</i> and should be an object containing one or more
	 * fields decorated with {@link CardDetail}.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface CardDetailsGrid { // new Introduce Sections within Card Grid for grouping.
		String alias() default "CardDetailsGrid";
		boolean draggable() default false;
		String editUrl() default "";
		boolean onLoad() default false;
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>CheckBox will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>CheckBox should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface CheckBox {
		String alias() default "CheckBox";
		String controlId() default "";
		String cssClass() default "";
		String help() default "";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>CheckBoxGroup will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>CheckBox should decorate an array or collection.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface CheckBoxGroup {
		String alias() default "CheckBoxGroup";
		String controlId() default "";
		String cssClass() default "";
		String help() default "";
		String labelClass() default "anthem-label";
		String level() default "0";
		boolean postEventOnChange() default false;
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>ComboBox will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * <li>Layout Domain</li>
	 * <li>{@link GlobalNavMenu}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>ComboBox should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ComboBox {
		String alias() default "ComboBox";
		String controlId() default "";
		String help() default "";
		String labelClass() default "anthem-label";
		String postButtonUrl() default "";
		boolean postEventOnChange() default false;
		boolean readOnly() default false;
		String cssClass() default "";
	}
	
	/**
	 * <!--TODO Write javadoc-->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@DomainMeta
	public @interface ConceptId {
		String value() default "";
	}

	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface ContentContainer {
		String alias() default "ContentContainer";
		String content() default "";
	}	
	
	/**
	 * <!--TODO Write javadoc-->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface DomainMeta {
		
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>FieldValueGroup is a grouping of FieldValues. Used in scenarios where Fields have to displayed grouped in columns.
	 * <p>FieldValueGroup will be rendered under one of the following components:
	 * <ul>
	 * <li>{@link CardDetailBody}</li>
	 * </ul>
	 * 
	 * 
	 * @since 1.1
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface FieldValueGroup {
		String alias() default "FieldValueGroup";
		String cols() default "1";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>FieldValue will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link CardDetailHeader}</li>
	 * <li>{@link CardDetailBody}</li>
	 * <li>{@link FieldValueGroup}</li>
	 * </ul>
	 * 
	 * <p>FieldValue should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface FieldValue {
		public enum Type {
			HEADER, 
			LEFTBAR,
			MENUPANEL,
			DEFAULT,
			Divider,
			Field
		}
		String alias() default "FieldValue";
		String cols() default "4";
		String cssClass() default "";
		String datePattern() default "";
		String imgSrc() default "";
		boolean inplaceEdit() default false;
		String inplaceEditType() default "";
		String placeholder() default "";
		boolean showName() default true;
		/**
		 * Apply styles to display value. This controls the display of value with images and fonts
		 */
		boolean applyValueStyles() default false;
		Type type() default Type.Field;
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>FileUpload will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>FileUpload should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@ViewStyle
	public @interface FileUpload {
		public enum ControlType {
			FORMCONTROL
		}

		String alias() default "FileUpload";
		ControlType controlType() default ControlType.FORMCONTROL;
		String metaData() default "";
		boolean multiple() default true;
		String type() default ".pdf,.png";
		String url() default "";
		String cssClass() default "";
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface FilterButton {
		String alias() default "FilterButton";
		String b() default "$execute";
		String cssClass() default "btn btn-primary";
		String imgSrc() default "";
		String method() default "GET";
		String url() default "";
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface FooterProperty {
		public enum Property {
			DISCLAIMER,
			LINK,
			SSLCERT
		}
		Property value() default Property.DISCLAIMER;
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Form will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>Section</li>
	 * </ul>
	 * 
	 * <p>Form will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Accordion}</li>
	 * <li>{@link Button}</li>
	 * <li>{@link ButtonGroup}</li>
	 * <li>{@link Calendar}</li>
	 * <li>{@link CheckBox}</li>
	 * <li>{@link CheckBoxGroup}</li>
	 * <li>{@link ComboBox}</li>
	 * <li>{@link FileUpload}</li>
	 * <li>{@link Grid}</li>
	 * <li>{@link Header}</li>
	 * <li>{@link MultiSelect}</li>
	 * <li>{@link MultiSelectCard}</li>
	 * <li>*Nested Classes <i>(No annotation decoration necessary)</i></li>
	 * <li>{@link Paragraph}</li>
	 * <li>{@link PickList}</li>
	 * <li>{@link Radio}</li>
	 * <li>{@link Signature}</li>
	 * <li>{@link TextArea}</li>
	 * <li>{@link TextBox}</li>
	 * </ul>
	 * 
	 * <p><i>*Note: Nested class fields will be rendered in the same manner as fields declared directly 
	 * under the Form decorated field. In other words, field declaration for forms can be recursive.</i>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Form { 
		String alias() default "Form";
		String b() default ""; // remove
		String cssClass() default "";
		String navLink() default ""; // remove
		boolean submitButton() default true; // remove
		String submitUrl() default ""; // remove
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>GlobalFooter will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>Layout Domain</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GlobalFooter {

		String alias() default "Footer";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>GlobalHeader will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>Layout Domain</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GlobalHeader {

		String alias() default "Global-Header";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>GlobalNavMenu will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>Layout Domain</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GlobalNavMenu {

		String alias() default "Global-Nav-Menu";
		String cssClass() default "";
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GlobalSection {
		String alias() default "globalSection";
		String cssClass() default "";
		String imgSrc() default "";
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Grid will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p><b>Configuring Row Item Display</b>
	 * 
	 * <p>The class being used in the decorated collection's parameterized type is treated as the definition 
	 * for row item data in the final rendered grid. This is referred to as the <i>collection element type</i>.
	 * The following components may be used to decorate fields in the collection element type:
	 * <ul>
	 * <li>{@link LinkMenu}</li>
	 * <li>{@link GridColumn}</li>
	 * <li>{@link GridRowBody}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Grid {
		String alias() default "Grid";
		boolean clearAllFilters() default false;
		String cssClass() default "";
		String dataKey() default "id";
		boolean expandableRows() default false;
		boolean export() default false;
		boolean isTransient() default false;
		boolean lazyLoad() default false;
		boolean onLoad() default false; 
		String pageSize() default "25";
		boolean pagination() default true;
		boolean postButton() default false;
		String postButtonAlias() default "";
		String postButtonLabel() default "";
		String postButtonTargetPath() default "";
		String postButtonUrl() default "";
		boolean postEventOnChange() default false;
		boolean rowSelection() default false;
		boolean showHeader() default true;
		String url() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>GridColumn will be rendered when annotating a field within a <i>collection element type</i>. 
	 * See {@link Grid} for more details. GridColumn should decorate a simple type.
	 * 
	 * <p>GridColumn should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 * @see com.antheminc.oss.nimbus.domain.defn.Grid
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GridColumn {
		public enum FilterMode {
			contains("containsIgnoreCase"),
			endsWith("endsWithIgnoreCase"),
			equals("equalsIgnoreCase"),
			in("in");
			
			public static String getStrictMatchModeFor(FilterMode mode) {
				if(mode == null)
					return null;
				
				if(mode == FilterMode.equals)
					return "eq";
				
				return mode.getCode();
			}
			
			public static boolean isValidBooleanFilter(FilterMode mode) {
				if(mode == FilterMode.equals || mode == FilterMode.in) {
					return true;
				}
				return false;
			}
			
			//TODO add more filters as valid as they are available (e.g. gt, gte, lt, lte etc...)
			public static boolean isValidNumericFilter(FilterMode mode) {
				if(mode == FilterMode.equals || mode == FilterMode.in) {
					return true;
				}
				return false;
			}
			
			@Getter @Setter
			private String code;
			
			FilterMode(String code) {
				this.code = code;
			}
			
		}
		
		public enum SortAs {
			DEFAULT,
			NUMBER,
			TEXT;
		}
		
		String alias() default "GridColumn";
		String datePattern() default ""; 
		boolean expandable() default true;
		/**
		 * Setting this to true will enable a explicit filter component(Input TextBox) on the UI
		 */
		boolean filter() default false;
		/**
		 * <p>Mode to filter data within the column. It is defaulted to 'equals'
		 * <p>Current filter modes supported by the Grid component are listed as enum values
		 */
		FilterMode filterMode() default FilterMode.equals;
		/**
		 * <p>Default filterValue for a particular column.
		 * <p>Can be used without setting filter attribute to restrict user from providing any other filter value.
		 */
		String filterValue() default "";
		/**
		 * Setting this to true will hide the column in the grid
		 */
		boolean hidden() default false;
		String placeholder() default "";
		boolean rowExpander() default false;
		boolean showAsLink() default false;
		/**
		 * Enables sorting on the column
		 */
		boolean sortable() default true;
		SortAs sortAs() default SortAs.DEFAULT; // number, text
		/**
		 * Apply styles to display value. This controls the display of value with images and fonts
		 */
		boolean applyValueStyles() default false;
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GridContainer {
		String alias() default "GridContainer";
	}

	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface GridFilter {
		
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>The field decorated with &#64;GridRowBody is rendered using the same logic used as defined in 
	 * {@link Section}. This means that &#64;GridRowBody should decorate an object that contains
	 * additional fields decorated with other &#64;ViewConfig annotations. The annotations used within
	 * this object will be rendered corresponding to the rules defined for the {@link Section} component.
	 * 
	 * <p>GridRowBody will be rendered when annotating a field within a <i>collection element type</i>. 
	 * See {@link Grid} for more details.
	 * 
	 * @since 1.0
	 * @see com.antheminc.oss.nimbus.domain.defn.Grid
	 * @see com.antheminc.oss.nimbus.domain.defn.Section
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface GridRowBody {
		String alias() default "GridRowBody";
		boolean asynchronous() default false;
		String cssClass() default "";
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Header will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>Header should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Header {
		public enum Size {
			H1,
			H2,
			H3,
			H4,
			H5
		}
		String alias() default "Header";
		Size size() default Size.H3;
		String cssClass() default "";
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface Hints {
		public enum AlignOptions {
			Center,
			Inherit,
			Left,
			Right;
		}
		AlignOptions value() default AlignOptions.Inherit;
	}

	/**
	 * 
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Image { 
	    public enum Type { 
	        FA, // Font Awesome 
	        SVG 
	    } 
	    String alias() default "Image"; 
	    String cssClass() default ""; 
	    String imgSrc() default ""; 
	    Image.Type type() default Image.Type.FA; 
	    String title() default ""; 
	}

	/**
	 * <!--TODO Write javadoc-->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface Initialize {
		String alias() default "initialize";
	}

	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MenuLink {
		String alias() default "MenuLink";	
		String url() default "";
		String imgSrc() default "";
		String cssClass() default "";
		String page() default "";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface InPlaceEdit {
		String alias() default "InPlaceEdit";
		String type() default "text";
		String cssClass() default "";
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface LinearGauge {
		String alias() default "LinearGauge";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
		String cssClass() default "";
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Link will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link CardDetail}</li>
	 * <li>{@link GlobalFooter}</li>
	 * <li>{@link GlobalNavMenu}</li>
	 * <li>{@link GlobalHeader}</li>
	 * <li>{@link Grid}</li>
	 * <li>Layout Domain</li>
	 * <li>{@link Menu}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>Link should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Link {
		public enum Type {
			DEFAULT,
			EXTERNAL,
			INLINE,
			MENU;
		}
		String alias() default "Link";
		String altText() default "";	
		String b() default "$executeAnd$nav";
		String cssClass() default "";
		String imgSrc() default "";
		String method() default "GET";
		String rel() default "";
		String target() default "";
		String url() default "";
		Type value() default Type.DEFAULT;
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>LinkMenu will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Grid}</li>
	 * </ul>
	 * 
	 * <p>LinkMenu will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Link}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface LinkMenu {
		String alias() default "LinkMenu";
		String imgSrc() default "";
		Image.Type imgType() default Image.Type.FA;
		String cssClass() default "dropdownTrigger";	
	}	
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Menu will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>Menu will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Link}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Menu {
		public enum Type {
			CONTEXT;
		}
		String alias() default "Menu";
		String cssClass() default "";		
		String text() default "";
		Type value() default Type.CONTEXT;
	}

	/**
	 * <p>Renders a popup window with content defined by the nested fields within the field
	 * that is decorated with <tt>&#64;Modal</tt>.
	 * 
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Modal will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Tile}</li>
	 * </ul>
	 * 
	 * <p><b>Notes:</b>
	 * <ul>
	 * <li>Default contextual properties are set by <tt>ModalStateEventHandler</tt> during 
	 * the <tt>OnStateLoad</tt> event.</li>
	 * </ul>
	 * 
	 * @since 1.0
	 * @author Tony Lopez
	 * @see com.anthem.oss.nimbus.core.domain.model.state.extension.ModalStateEventHandler
	 *
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD })
	@ViewStyle
	@OnStateLoad
	public @interface Modal {
		public enum Type {
			dialog, slider
		}
		String alias() default "Modal";
		boolean closable() default false;
		ParamContext context() default @ParamContext(enabled = true, visible = false);
		String cssClass() default ""; // new
		int order() default Event.DEFAULT_ORDER_NUMBER;
		boolean resizable() default true;
		Type type() default Type.dialog;
		String width() default "medium";
	}

	/**
	 * <!--TODO Write javadoc-->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface Mode {
		public enum Options {
			Hidden,
			Inherit,
			ReadOnly
		}
		Options value() default Options.Inherit;
	}
	
	/**
	 * <!--TODO Candidate for removal-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiGrid {
		String alias() default "MultiGrid";
		String cssClass() default "question-header";
		String header() default "test";
		String level() default "0";
		boolean postEventOnChange() default false;
	}	
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>MultiSelect will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>MultiSelect should decorate an array or collection.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiSelect {
		String alias() default "MultiSelect";
		String help() default "";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>MultiSelectCard will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>MultiSelectCard should decorate an array or collection.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface MultiSelectCard {
		String alias() default "MultiSelectCard";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Page will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Domain}</li>
	 * </ul>
	 * 
	 * <p>Page will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Tile}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Page {
		String alias() default "Page";
		String cssClass() default ""; 
		boolean defaultPage() default false;
		String imgSrc() default "";
		String route() default ""; // remove
	}
	
	/**
	 * <!--TODO Write javadoc-->
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewParamBehavior
	public @interface PageHeader {
		public enum Property {
			APPTITLE,
			DEFAULT,
			HELP,
			LOGO,
			LOGOUT,
			MENU,
			NOTIFICATIONS,
			NUMBEROFNOTIFICATIONS,
			SETTINGS,
			SUBHEADER,
			SUBTITLE,
			TITLE,
			USERNAME,
			USERROLE;
		}
		Property value() default Property.DEFAULT;
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Paragraph will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * <li>{@link Header}</li>
	 * <li>{@link GlobalHeader}</li>
	 * <li>{@link GlobalFooter}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>Paragraph should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Paragraph {
		String alias() default "Paragraph";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>PickList will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>PickList should decorate an array or collection.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface PickList {
		String alias() default "PickList";
		String cssClass() default "";
		String help() default "";
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
		boolean readOnly() default false;
		String sourceHeader() default "SourceList";
		String targetHeader() default "TargetList";
		boolean showSourceControls() default false;
		boolean showTargetControls() default false;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface PickListAvailable {
		String alias() default "PickListAvailable";
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface PickListSelected {
		String alias() default "PickListSelected";
		boolean postEventOnChange() default false;
		
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Radio will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>Radio should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Radio {
		String alias() default "Radio";
		String controlId() default "";
		String cssClass() default "";
		String help() default "";
		String labelClass() default "anthem-label";
		String level() default "0";
		boolean postEventOnChange() default false;
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Section will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Grid}</li>
	 * <li>{@link Modal}</li>
	 * <li>{@link Section}</li>
	 * <li>{@link Tile}</li>
	 * </ul>
	 * 
	 * <p>Section will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link AccordionMain}</li>
	 * <li>{@link Button}</li>
	 * <li>{@link ButtonGroup}</li>
	 * <li>{@link CardDetail}</li>
	 * <li>{@link CardDetailsGrid}</li>
	 * <li>{@link ComboBox}</li>
	 * <li>{@link Form}</li>
	 * <li>{@link Grid}</li>
	 * <li>{@link Link}</li>
	 * <li>{@link Menu}</li>
	 * <li>{@link Paragraph}</li>
	 * <li>{@link StaticText}</li>
	 * <li>{@link TextBox}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	//TODO: Will have to remove LEFTBAR and HEADER once the leftbarn& header navigation for case is in place
	public @interface Section {
		public enum Type {
			DEFAULT, 
			HEADER,
			MENUPANEL,
			LEFTBAR;
		}
		
		String alias() default "Section";
		String cssClass() default "";
		Type value() default Type.DEFAULT; // HEADER and LEFTBAR should be removed in future
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Signature will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>Signature should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 * @author Rakesh Patel
	 * @author Tony Lopez
	 */
	@Retention(RetentionPolicy.RUNTIME) 
	@Target({ElementType.FIELD}) 
	@ViewStyle 
	public @interface Signature { 
		
		/**
		 * The strategy for how the signature drawing should be captured on the UI.
		 */
		public enum CaptureType {
			
			/**
			 * Signature data is captured in between the mouse down and mouse up events.
			 */
			DEFAULT,
			
			/**
			 * Signature data is captured upon the click event. Capturing will continue until the click 
			 * event is invoked a second time.
			 */
			ON_CLICK;
		}
		
		String acceptLabel() default "Save";
		String alias() default "Signature"; 
		/**
		 * Controls how the signature drawing will be captured.
		 * @see com.antheminc.oss.nimbus.domain.defn.ViewConfig.Signature.CaptureType
		 */
		CaptureType captureType() default CaptureType.DEFAULT; 
		String clearLabel() default "Clear"; 
		String controlId() default "";
		String height() default "60"; 
		String help() default "";
		boolean hidden() default false;
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
		String type() default "signature";
		String cssClass() default "";
		String width() default "345";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>StaticText will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link CardDetail}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>StaticText should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface StaticText {
		String alias() default "StaticText";
		String contentId() default "";
		String cssClass() default "";
	}
		
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>SubHeader will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>Layout Domain</li>
	 * </ul>
	 * 
	 * <p>SubHeader should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface SubHeader {
		String alias() default "SubHeader";
		String cssClass() default "col-sm-6 pb-0 align-top";	//pb-0 is added for the demo. It is temp fix
	}

	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>TextArea will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * </ul>
	 * 
	 * <p>TextArea should decorate a field having a simple type.
	 *
	 * <p> By providing a value for @Max on top of @TextArea would restrict characters max length in @TextArea 
 	 *  
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME) 
	@Target({ElementType.FIELD}) 
	@ViewStyle 
	public @interface TextArea { 
		String alias() default "TextArea"; 
		String controlId() default ""; 
		String help() default ""; 
		boolean hidden() default false; 
		String labelClass() default "anthem-label"; 
		boolean postEventOnChange() default false; 
		boolean readOnly() default false; 
		String rows() default "5"; 
		String type() default "textarea"; 
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>TextBox will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Form}</li>
	 * <li>{@link Section}</li>
	 * </ul>
	 * 
	 * <p>TextBox should decorate a field having a simple type.
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface TextBox {
		String alias() default "TextBox";
		String controlId() default "";
		String help() default "";
		boolean hidden() default false;
		String labelClass() default "anthem-label";
		boolean postEventOnChange() default false;
		boolean readOnly() default false;
		String type() default "text";
		String cssClass() default "";
	}
	
	/**
	 * <p><b>Expected Field Structure</b>
	 * 
	 * <p>Tile will be rendered when annotating a field nested under one of the following components:
	 * <ul>
	 * <li>{@link Page}</li>
	 * </ul>
	 * 
	 * <p>Tile will render nested fields that are decorated with:
	 * <ul>
	 * <li>{@link Header}</li>
	 * <li>{@link Modal}</li>
	 * <li>{@link Section}</li>
	 * <li>{@link Tile}</li>
	 * </ul>
	 * 
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@ViewStyle
	public @interface Tile {
		public enum Size {
			Colorbox, //100%, //25%
			Large, //33%
			Medium, //50%
			Small, //100%
			XSmall
		}
		String alias() default "Tile";
		String imgSrc() default "";
		String cssClass() default "";
		Size size() default Size.Large;
	}
	
	/**
	 * <!--TODO Write javadoc-->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface ViewParamBehavior {
		
	}
	
	/**
	 * <!-- TODO Write javadoc -->
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@ViewStyle
	public @interface ViewRoot {
		String alias() default "root";
		String layout() default "";
	}
	
	/**
	 * <p>Parent annotation used to identify all annotations that are used for rendering the UI. 
	 *
	 * @since 1.0
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value={ElementType.ANNOTATION_TYPE})
	@Inherited
	public @interface ViewStyle {
		
	}
}