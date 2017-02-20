/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Transient;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ExecutionStateTree;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationResult;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.Notification;
import com.anthem.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultModelState<T> extends AbstractEntityState<T> implements Model<T>, Serializable {
	
    private static final long serialVersionUID = 1L;

    @JsonIgnore final private Param<T> associatedParam;
    
    private List<Param<? extends Object>> params;

	@JsonIgnore private transient ValidationResult validationResult;
	
	@JsonIgnore private ExecutionStateTree executionStateTree;
	
	public DefaultModelState(Param<T> associatedParam, ModelConfig<T> config, StateBuilderSupport provider/*, Model<?> backingCoreModel*/) {
		super(config, provider);
		
		Objects.requireNonNull(associatedParam, "Associated Param for Model must not be null.");
		this.associatedParam = associatedParam;
		super.setPath(getAssociatedParam().getPath());
	}

	@Override
	public ModelConfig<T> getConfig() {
		return (ModelConfig<T>)super.getConfig();
	}
	
	
	@Transient @JsonIgnore @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private final transient CollectionsTemplate<List<EntityState.Param<? extends Object>>, EntityState.Param<? extends Object>> templateParams = new CollectionsTemplate<>(
			() -> getParams(), (p) -> setParams(p), () -> new LinkedList<>());

	@JsonIgnore @Override
	public CollectionsTemplate<List<EntityState.Param<?>>, EntityState.Param<?>> templateParams() {
		return templateParams;
	}
	
	@Override
	public void fireRules() {
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.fireRules(getAssociatedParam()));
	}
	
	@JsonIgnore @Override
	public RootModel<?> getRootModel() {
		if(getAssociatedParam() == null) return this.findIfRoot();
		
		return getAssociatedParam().getRootModel();
	}
	
	@Override
	public T instantiateOrGet() {
		T instance = getProvider().getParamStateGateway()._getRaw(this.getAssociatedParam());
		return instance==null ? instantiateAndSet() : instance; 
	}
	
	@Override
	public T instantiateAndSet() {
		T newInstance =  getProvider().getParamStateGateway()._instantiateAndSet(this.getAssociatedParam());
		 
		getAssociatedParam().notifySubscribers(new Notification<>(this.getAssociatedParam(), ActionType._newModel, this.getAssociatedParam()));
		return newInstance;
	}
	
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		return getAssociatedParam()==null ? null : getAssociatedParam().findParamByPath(pathArr);
	}	
}