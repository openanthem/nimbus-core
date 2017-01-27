/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.view.dsl.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

import com.anthem.nimbus.platform.spec.contract.event.EventPublisher;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.SimpleState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.State;
import com.anthem.nimbus.platform.spec.model.dsl.config.AnnotationConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"isVisible", "core", "uiNatures"})
public class ViewParamConfig<P> extends CoreParamConfig<P> implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@JsonIgnore	private final transient CoreParamConfig<P> core;

	private Boolean visible = Boolean.TRUE;
	
	private Boolean enabled = Boolean.TRUE;

	@Setter private List<AnnotationConfig> uiNatures;

	@Setter private AnnotationConfig uiStyles;

	@JsonIgnore
	private transient State<Boolean> visibleState;// = new SimpleState<Boolean>(()->isVisible(), (v)->setVisible(v));
	
	@JsonIgnore
    private transient State<Boolean> enabledState;
	
	public ViewParamConfig(String code, CoreParamConfig<P> core, MapsTo.Path mapsTo, ModelConfig<?> parentModel) {
		super(code, mapsTo, parentModel);
		this.core = core;
	}
	

	@Override
	public boolean isView() {
		return true;
	}
	
	/**
	 * 
	 */
	@Override
	public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider provider) {
		if(core != null) {
			super.visit(param, provider);
		}
		
		super.visit(param, provider);
		this.visibleState = new SimpleState<>(param, ()->getVisible(), (v)->visible=v, provider);
		this.enabledState = new SimpleState<>(param, () -> getEnabled(), (v) -> enabled = v, provider);
	}

	@Override @JsonIgnore
	public ParamType getType() {
		return (core != null) ? core.getType() : super.getType();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isActive() {
		return (core != null) ? core.isActive() : super.isActive();
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isRequired() {
		return (core != null) ? core.isRequired() : super.isRequired();
	}

	/**
	 * 
	 */
	@Override
	public Desc getDesc() {
		return (core != null) ? core.getDesc() : super.getDesc();
	}

	
	/**
	 * 
	 */
	@Override
	public List<AnnotationConfig> getValidations() {
		return (core != null) ? core.getValidations() : super.getValidations();
	}

	/**
	 * 
	 */
	@Override
	public List<Annotation> getAnnotations() {
		return (core != null) ? core.getAnnotations() : super.getAnnotations();
	}

	/**
	 * 
	 */
	@Override
	public List<ParamValue> getValues() {
		return (core != null) ? core.getValues() : super.getValues();
	}

	/*
	@Override
	public <T> ParamConfig clone(Class<? extends Annotation>[] ignore, Map<Class<T>, ModelConfig<T>> visitedModels) {
		if(!isMapped()) {
			CoreParamConfig clonedCore = core.clone(ignore, visitedModels);
		
			ViewParamConfig clonedView = new ViewParamConfig(getCode(), clonedCore, getMapsTo());
			return clonedView;
		}
		
		throw new UnsupportedOperationException("Yet to implement clone of mapped view param. "
				+ "Required cloning of mapped models first, then referencing the same in view config.");
	}*/
	
	
	/*
	@Override @JsonIgnore
	public void setType(ParamType type) {
		if(core!=null) core.setType(type); else super.setType(type);
	}
	
	@Override
	public void setActive(boolean isActive) {
		if(core!=null) core.setActive(isActive); else super.setActive(isActive);
	}
	
	@Override
	public void setRequired(boolean isRequired) {
		if(core!=null) core.setRequired(isRequired); else super.setRequired(isRequired);
	}
	
	@Override
	public void setDesc(Desc desc) {
		if(core!=null) core.setDesc(desc); else super.setDesc(desc);
	}
	
	@Override
	public void setValidations(List<AnnotationConfig> validations) {
		if(core!=null) core.setValidations(validations); else super.setValidations(validations);
	}
	
	@Override
	public void setAnnotations(List<Annotation> annotations) {
		if(core!=null) core.setAnnotations(annotations); else super.setAnnotations(annotations);
	}
	
	@Override
	public void setValues(List<ParamValue> values) {
		if(core!=null) core.setValues(values); else super.setValues(values);
	} 
	*/
	
}
