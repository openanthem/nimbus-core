/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import com.anthem.nimbus.platform.spec.contract.event.EventPublisher;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.SimpleState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.State;
import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @ToString(callSuper=true, of={"isEnabled"})
public class CoreModelConfig<T> extends AbstractModelConfig<T> implements ModelConfig<T>, Serializable {

	private static final long serialVersionUID = 1L;
	

	private boolean isEnabled;
	
	@JsonIgnore
	private transient State<Boolean> enabledState;// = new SimpleState<Boolean>(()->isEnabled(), (v)->setEnabled(v));

    @JsonIgnore @Getter @Setter private List<ParamConfig<?>> params;
	
	@JsonIgnore
	@Getter @Setter private transient ParamConfig<?> idParam;
	
	@JsonIgnore
	@Getter @Setter private transient ParamConfig<?> versionParam;
	
	
	public CoreModelConfig(Class<T> referredClass, MapsTo.Model mapsTo) {
		super(referredClass, mapsTo);
	}
	
	
	/**
	 * 
	 */
	@Transient @JsonIgnore
	private final transient CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams = new CollectionsTemplate<>(
			() -> getParams(), (p) -> setParams(p), () -> new LinkedList<>());

	/**
	 * 
	 */
	@Override @JsonIgnore
	public CollectionsTemplate<List<ParamConfig<?>>, ParamConfig<?>> templateParams() {
		return templateParams;
	}
	
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ModelConfig<?> resolveBackingConfig() {
		return this;
	}
	
	@Override
	public void visit(ParamStateAndConfig<?> param, StateAndConfigSupportProvider provider) {
		this.enabledState = new SimpleState<>(param, () -> isEnabled(), (v) -> isEnabled = v, provider);
	}
	
	@Override
	public CoreModelConfig<T> clone(Class<? extends Annotation> ignore[]) {
		return clone(ignore, new HashMap<Class<T>, ModelConfig<T>>());
	}
	
	
	/**
	 * 
	 * @param ignore
	 * @param visitedModels
	 * @return
	 */
	protected CoreModelConfig<T> clone(Class<? extends Annotation> ignore[], Map<Class<T>, ModelConfig<T>> visitedModels) {
		CoreModelConfig<T> clonedModel = new CoreModelConfig<>(getReferredClass(), getMapsTo());
		visitedModels.put(getReferredClass(), clonedModel);
		
		if(params == null) return clonedModel;
		
		for(ParamConfig<?> param : getParams()) {
			ParamConfig<?> clonedParam = param.clone(ignore, visitedModels);
			clonedModel.templateParams().add(clonedParam);
		}
		
		return clonedModel;
	}


	@Override
	public boolean isView() {
		return false;
	}
	
}
