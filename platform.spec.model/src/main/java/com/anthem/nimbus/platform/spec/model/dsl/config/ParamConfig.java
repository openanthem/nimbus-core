/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.anthem.nimbus.platform.spec.model.Findable;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;

/**
 * @author Soham Chakravarti
 *
 */
public interface ParamConfig<P> extends Config<P>, Findable<String>, Serializable {

	@lombok.Data
	public static class Desc implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private String label;
		
		private String hint;
		
		private String help;
	}
	
	public String getCode();
	
	public ParamType getType();
	
	public boolean isActive();
	
	public boolean isRequired();
	
	public boolean isLeaf();
	
	public boolean isView();
	
	public boolean isMapped();
	
	public MapsTo.Path getMapsTo();
	
	/**
	 * Applies for nested param model for scenarios when current param's parent model is mapped and is same as 
	 * nested param model's mapped class value
	 * */
	public boolean isImplicitlyMapped();
	
	public Desc getDesc();
	
	public List<AnnotationConfig> getValidations();
	
	public <T> ParamConfig<P> clone(Class<? extends Annotation> ignore[], Map<Class<T>, ModelConfig<T>> visitedModels);
	
	public List<ParamValue> getValues();
	
	public String getValuesUrl();
	
}
