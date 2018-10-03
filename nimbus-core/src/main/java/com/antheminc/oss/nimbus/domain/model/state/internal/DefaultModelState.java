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
package com.antheminc.oss.nimbus.domain.model.state.internal;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.annotation.Transient;

import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationResult;
import com.antheminc.oss.nimbus.domain.model.config.EventHandlerConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Model;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.Notification;
import com.antheminc.oss.nimbus.domain.model.state.Notification.ActionType;
import com.antheminc.oss.nimbus.domain.model.state.event.StateEventHandlers.OnStateLoadNewHandler;
import com.antheminc.oss.nimbus.domain.model.state.support.DefaultJsonModelSerializer;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
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
@JsonSerialize(using=DefaultJsonModelSerializer.class)
@Getter @Setter
public class DefaultModelState<T> extends AbstractEntityState<T> implements Model<T>, Serializable {
	
    private static final long serialVersionUID = 1L;

    @Setter(AccessLevel.NONE)
    @JsonIgnore private Param<T> associatedParam;
    
    private List<Param<? extends Object>> params;

	@JsonIgnore private transient ValidationResult validationResult;
	
	@JsonIgnore private transient boolean isNew;
	
	public DefaultModelState() {
		this.associatedParam = null;
	}
	
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
	
	@JsonIgnore
	@Override
	public String getPath() {
		return getAssociatedParam().getPath();
	}
	
	@Override
	public String getBeanPath() {
		return getAssociatedParam().getBeanPath();
	}
	
	@Override
	public boolean isNew() {
		return getRootExecution().isNew() ? true : this.isNew;
	}
	
	@Override
	protected void initStateInternal() {
		if(templateParams().isNullOrEmpty())
			return;
		
		getParams().stream()
			.forEach(Param::initState);
		
		// hook up on state load new events
		if(isNew())
			onStateLoadNewEvent(this);

	}
	
	// TODO : move to runtime.eventDelegate
	protected static void onStateLoadNewEvent(Model<?> m) {
		EventHandlerConfig eventHandlerConfig = m.getConfig().getEventHandlerConfig();
		if(eventHandlerConfig!=null && eventHandlerConfig.getOnStateLoadNewAnnotations()!=null) {
			eventHandlerConfig.getOnStateLoadNewAnnotations().stream()
				.forEach(ac->{
					OnStateLoadNewHandler<Annotation> handler = eventHandlerConfig.getOnStateLoadNewHandler(ac);
					if(handler.shouldAllow(ac, m.getAssociatedParam())) {
						handler.onStateLoadNew(ac, m.getAssociatedParam());
					}
				});
		}
	}
	
	@JsonIgnore
	@Override
	public ModelConfig<T> getConfig() {
		return (ModelConfig<T>)super.getConfig();
	}
	
	@JsonIgnore @Override
	public Param<?> getIdParam() {
		return findParamByPath(getConfig().getIdParamConfig().getCode());
	}
	
	@JsonIgnore @Override
	public Param<?> getVersionParam() {
		return findParamByPath(getConfig().getVersionParamConfig().getCode());
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
		if(instance==null) {
			instance = instantiateAndSet(); 
		} 
			
		return instance;
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

	@JsonIgnore
	@Override
	public Model<?> getRootDomain() {
		return getAssociatedParam().getRootDomain();
	}

	@Override
	public ExecutionModel<T> findIfRoot() {
		return null;
	}

	@Override
	public MappedModel<T, ?> findIfMapped() {
		return null;
	}

	@Override
	public ListModel<?> findIfListModel() {
		return null;
	}

	@JsonIgnore
	@Override
	public T getLeafState() {
		return Optional.ofNullable(getAssociatedParam()).map(p->p.getLeafState()).orElse(null);
	}

	@JsonIgnore
	@Override
	public T getState() {
		return Optional.ofNullable(getAssociatedParam()).map(p->p.getState()).orElse(null);
	}

	@Override
	public void setState(T state) {
		Optional.ofNullable(getAssociatedParam()).ifPresent(p->p.setState(state));
	}
}