/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Transient;

import com.antheminc.oss.nimbus.core.domain.command.execution.ValidationResult;
import com.antheminc.oss.nimbus.core.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification;
import com.antheminc.oss.nimbus.core.domain.model.state.Notification.ActionType;
import com.antheminc.oss.nimbus.core.util.CollectionsTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.StdConverter;

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
	
	public DefaultModelState(Param<T> associatedParam, ModelConfig<T> config, EntityStateAspectHandlers provider/*, Model<?> backingCoreModel*/) {
		super(config, provider);
		
		Objects.requireNonNull(associatedParam, "Associated Param for Model must not be null.");
		this.associatedParam = associatedParam;
	}

	@Transient @JsonIgnore @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private final transient CollectionsTemplate<List<EntityState.Param<? extends Object>>, EntityState.Param<? extends Object>> templateParams = new CollectionsTemplate<>(
			() -> getParams(), (p) -> setParams(p), () -> Collections.synchronizedList(new LinkedList<>()));

	@JsonIgnore @Override
	public CollectionsTemplate<List<EntityState.Param<?>>, EntityState.Param<?>> templateParams() {
		return templateParams;
	}
	
	@Override
	public String getPath() {
		return getAssociatedParam().getPath();
	}
	
	@Override
	public String getBeanPath() {
		return getAssociatedParam().getBeanPath();
	}
	
	@Override
	protected void initStateInternal() {
		if(templateParams().isNullOrEmpty())
			return;
		
		getParams().stream()
			.forEach(Param::initState);
	}
	
	@Override
	public ModelConfig<T> getConfig() {
		return (ModelConfig<T>)super.getConfig();
	}
	
	@JsonIgnore @Override
	public Param<?> getIdParam() {
		return findParamByPath(getConfig().getIdParam().getCode());
	}
	
	@JsonIgnore @Override
	public Param<?> getVersionParam() {
		return findParamByPath(getConfig().getVersionParam().getCode());
	}
	
	@Override
	public void fireRules() {
		Optional.ofNullable(getRulesRuntime())
			.ifPresent(rt->rt.fireRules(getAssociatedParam()));
	}
	
	@JsonIgnore @Override
	public ExecutionModel<?> getRootExecution() {
		if(getAssociatedParam() == null) return this.findIfRoot();
		
		return getAssociatedParam().getRootExecution();
	}
	
	@Override
	public T instantiateOrGet() {
//		if(getConfig().getReferredClass()==StateContextEntity.class) {
//			return (T)createOrGetRuntimeEntity();
//		}
		
		T instance = getAspectHandlers().getParamStateGateway()._getRaw(this.getAssociatedParam());
		return instance==null ? instantiateAndSet() : instance; 
	}
	
	@Override
	public T instantiateAndSet() {
		T newInstance =  getAspectHandlers().getParamStateGateway()._instantiateAndSet(this.getAssociatedParam());
		 
		getAssociatedParam().emitNotification(new Notification<>(this.getAssociatedParam(), ActionType._newModel, this.getAssociatedParam()));
		return newInstance;
	}
	
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		return getAssociatedParam()==null ? null : getAssociatedParam().findParamByPath(pathArr);
	}	
	
	@JsonIgnore
	@Override
	public T getState() {
		return Model.super.getState();
	}
	
	public static class ParamsConverter extends StdConverter<List<?>, List<?>> {
		@Override
		public List<?> convert(List<?> in) {
			return in==null ? null : new LinkedList<>(in);
		}
	}
	
	@JsonSerialize(converter=ParamsConverter.class)
	@Override
	public List<Param<? extends Object>> getParams() {
		return params;
	}
}