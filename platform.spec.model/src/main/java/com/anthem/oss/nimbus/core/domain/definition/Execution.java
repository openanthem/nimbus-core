/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Configs;
import com.anthem.oss.nimbus.core.domain.definition.Executions.InputParams;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Inputs;
import com.anthem.oss.nimbus.core.domain.definition.Executions.OutputParams;
import com.anthem.oss.nimbus.core.domain.definition.Executions.Outputs;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig.Fetch;
import com.fasterxml.jackson.annotation.JsonIgnore;
 
/**
 * @author Soham Chakravarti
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
@Inherited
public @interface Execution {

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@Repeatable(Configs.class)
	@Execution
	public @interface Config {
		
		String url();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Repeatable(Inputs.class)
	@Execution
	public @interface Input {
		
		Action[] value();
		
		Class<? extends Annotation>[] ignore() default { JsonIgnore.class, CreatedBy.class, CreatedDate.class,
				LastModifiedBy.class, LastModifiedDate.class };
				
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE})
		public @interface Default {
			Input value() default @Input({Action._new, Action._replace, Action._update, Action._search, Action._lookup});
		}
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE})
		public @interface Default_Exclude_Search {
			Input value() default @Input({Action._new, Action._replace, Action._update, Action._lookup});
		}
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@Repeatable(InputParams.class)
	@Execution
	public @interface InputParam {
		
		String alias() default "{inherited}";
		
		Action[] value();
		
		public static final Action[] ID_DEFAULT = {Action._get, Action._info, Action._lookup}; 
		
		public static final Action[] VER_DEFAULT = {Action._replace, Action._update, Action._delete, Action._lookup};
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Repeatable(Outputs.class)
	@Execution
	public @interface Output {
		
		Action[] value();
		
		boolean paginated() default false;
		
		Fetch fetch() default Fetch.LAZY;
		
		Class<? extends Annotation>[] ignore() default { JsonIgnore.class, CreatedBy.class, CreatedDate.class,
				LastModifiedBy.class, LastModifiedDate.class };
				
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE})
		public @interface Default {
			Output value() default @Output({Action._get, Action._update, Action._search, Action._lookup});
		} 
		
		@Retention(RetentionPolicy.RUNTIME)
		@Target({ElementType.TYPE})
		public @interface Default_Exclude_Search {
			Input value() default @Input({Action._get, Action._update, Action._lookup});
		}
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD})
	@Repeatable(OutputParams.class)
	@Execution
	public @interface OutputParam {
		
		Action[] value();
		
		public static final Action[] ID_DEFAULT = {Action._new, Action._delete, Action._replace, Action._lookup};
	}
}
