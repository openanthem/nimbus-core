/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.SimpleState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.State;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"isActive", "isRequired", "type", "annotations", "validations", "values"})
public class CoreParamConfig<P> extends AbstractParamConfig<P> implements Serializable {

	private static final long serialVersionUID = 1L;
	

	@Setter private ParamType type;	
	
	private boolean isActive = true;
	
	private boolean isRequired = false;
	
	
	@JsonIgnore
	@Setter private List<Annotation> annotations;
	
	@Setter private List<AnnotationConfig> validations;
	
	@Setter private List<ParamValue> values;
	
	@Setter @JsonIgnore private transient Supplier<List<ParamValue>> valuesGetter;
	
	@Setter private Desc desc = new Desc();
	
	public List<ParamValue> getValues() {
		if(getValuesGetter() != null) {
			List<ParamValue> values = getValuesGetter().get();
			if(values != null) 
				this.values = values;
		}
		return values;
	}
	
	@JsonIgnore
	private transient State<Boolean> activeState;// = new SimpleState<Boolean>(()->isActive(), (v)->setActive(v));

	@JsonIgnore
	private transient State<Boolean> requiredState;// = new SimpleState<Boolean>(()->isRequired(), (v)->setRequired(v));
	
	
	
	public CoreParamConfig(String code, MapsTo.Path mapsTo, ModelConfig<?> parentModel) {
		super(code, mapsTo, () -> parentModel);
	}
	
	
	@Override
	public boolean isView() {
		return false;
	}
	
	/**
	 * 
	 */
	@Override
	public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider provider) {
		this.activeState = new SimpleState<>(param, () -> isActive(), (v) -> isActive = v, provider);
		this.requiredState = new SimpleState<>(param, () -> isRequired(), (v) -> isRequired = v, provider);
	}
	
	protected CoreParamConfig<P> newInstance(String code) {
		return new CoreParamConfig<>(code, getMapsTo(), getParentModel().get());
	}
	
	/**
	 * 
	 */
	@Override
	public <T> CoreParamConfig<P> clone(Class<? extends Annotation> ignore[], Map<Class<T>, ModelConfig<T>> visitedModels) {
		if(ArrayUtils.isNotEmpty(ignore) && !CollectionUtils.isEmpty(getAnnotations())) {
			for(Annotation a : getAnnotations()) {
				if(ArrayUtils.contains(ignore, a.annotationType())) return null;
			}
		}
		
		CoreParamConfig<P> clonedParam = newInstance(getCode());
		copyTo(clonedParam, ignore, visitedModels);
		return clonedParam;
	}
	
	/**
	 * 
	 * @param copyTo
	 * @param ignore
	 * @param visitedModels
	 */
	protected <T> void copyTo(CoreParamConfig<P> copyTo, Class<? extends Annotation> ignore[],
			Map<Class<T>, ModelConfig<T>> visitedModels) {
		
		//no clone
		copyTo.setDesc(desc);
		copyTo.setValidations(getValidations());
		
		ParamType clonedType = getType().clone(ignore, visitedModels);
		copyTo.setType(clonedType);
	}
	
}
