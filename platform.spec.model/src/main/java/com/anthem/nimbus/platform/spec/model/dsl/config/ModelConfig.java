/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.lang.annotation.Annotation;
import java.util.List;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.Repo;
import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface ModelConfig<T> extends Config<T> {
	
	public boolean isMapped();
	
	public boolean isEnabled();
	
	public MapsTo.Model getMapsTo();
	
	public Repo getRepo();
	
	/**
	 * 
	 * @return
	 */
	public boolean isView();
	
	/**
	 * 
	 * @return
	 */
	public List<? extends ParamConfig<?>> getParams();
	
	/**
	 * 
	 * @return
	 */
	public <B> ModelConfig<B> resolveBackingConfig();
	
	/**
	 * 
	 * @return
	 */
	@JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams();
	
	/**
	 * 
	 * @param ignore
	 * @return
	 */
	public ModelConfig<T> clone(Class<? extends Annotation> ignore[]);

}
