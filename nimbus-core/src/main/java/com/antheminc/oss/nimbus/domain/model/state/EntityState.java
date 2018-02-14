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
package com.antheminc.oss.nimbus.domain.model.state;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamType;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
import com.antheminc.oss.nimbus.support.pojo.LockTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
public interface EntityState<T> {

	String getPath();
	
	@JsonIgnore
	String getBeanPath();
	
	//@JsonIgnore
	EntityConfig<T> getConfig();

	default String getConfigId() {
		return getConfig().getConfigId();
	}
	
	<S> Model<S> findModelByPath(String path);
	<S> Model<S> findModelByPath(String[] pathArr);

	<P> Param<P> findParamByPath(String path);
	<P> Param<P> findParamByPath(String[] pathArr);
	
	default <P> P findStateByPath(String path) {
		Param<P> param = findParamByPath(path);
		
		if(param==null)
			throw new InvalidConfigException("Param not found for given path: "+path+" relative to current param/model: "+this);
		
		return param.getState();
	}

	void initSetup();
	void initState();
	
	boolean isStateInitialized();
	void setStateInitialized(boolean initialized);
	
	EntityStateAspectHandlers getAspectHandlers();
	
	void fireRules();
	
	@JsonIgnore
	ExecutionModel<?> getRootExecution();
	
	@JsonIgnore
	Model<?> getRootDomain();
	
	@JsonIgnore
	LockTemplate getLockTemplate();
	
	default boolean isRoot() {
		return false;
	}
	
	default boolean isMapped() {
		return false;
	}
	default Mapped<T, ?> findIfMapped() {
		return null;
	}
	
	public interface Mapped<T, M> extends EntityState<T> {
		@Override
		default boolean isMapped() {
			return true;
		}
		@Override
		default Mapped<T, M> findIfMapped() {
			return this;
		}
		EntityState<M> getMapsTo();
	}
	
	public interface ExecutionModel<T> extends Model<T> {
		@Override
		default boolean isRoot() {
			return true;
		}
		
		@Override
		default ExecutionModel<T> findIfRoot() {
			return this;
		}
		
		@JsonIgnore
		Command getRootCommand();
		
		@JsonIgnore
		ExecutionRuntime getExecutionRuntime();
		
		@JsonIgnore
		Map<String, Object> getParamRuntimes();
		
		default <U> U unwrap(Class<U> c) {
			if(c.isInstance(this))
				return c.cast(this);
			
			return null;
		}
	}
	
	public interface Model<T> extends EntityState<T> { 
		
		//@JsonIgnore 
		@Override
		ModelConfig<T> getConfig();
		
		@JsonIgnore
		Param<T> getAssociatedParam();
		
		public Param<?> getIdParam();
		public Param<?> getVersionParam();
		
		@JsonIgnore @Override
		default Model<?> getRootDomain() {
			return getAssociatedParam().getRootDomain();
		}
		
		default ExecutionModel<T> findIfRoot() {
			return null;
		}
		
		@Override
		default MappedModel<T, ?> findIfMapped() {
			return null;
		}
		
		List<Param<? extends Object>> getParams();
		
		default ListModel<?> findIfListModel() {
			return null;
		}
		
		CollectionsTemplate<List<Param<?>>, Param<?>> templateParams();
		
		T instantiateOrGet();
		T instantiateAndSet();
		
		default T getLeafState() {
			return Optional.ofNullable(getAssociatedParam()).map(p->p.getLeafState()).orElse(null);
		}
		
		default T getState() {
			return Optional.ofNullable(getAssociatedParam()).map(p->p.getState()).orElse(null);
		}
		default void setState(T state) {
			Optional.ofNullable(getAssociatedParam()).ifPresent(p->p.setState(state));
		}
		
	}
	
	public interface MappedModel<T, M> extends Model<T>, Mapped<T, M> {
		@Override
		default MappedModel<T, M> findIfMapped() {
			return this;
		}
		
		@Override
		Model<M> getMapsTo();
	}
	
	public interface ListModel<T> extends Model<List<T>>, ListBehavior<T> {
		@Override
		default MappedListModel<T, ?> findIfMapped() {
			return null;
		}
		
		@Override
		default ListModel<T> findIfListModel() {
			return this;
		}
		
		ListElemParam<T> createElement(String elemId);
		
		@Override
		ListElemParam<T> add();
		
		//@JsonIgnore
		default ParamConfig<T> getElemConfig() {
			StateType.NestedCollection<T> typeSAC = getAssociatedParam().getType().findIfCollection(); 
			ParamType.NestedCollection<T> typeConfig = typeSAC.getConfig().findIfCollection();
			
			ParamConfig<T> elemConfig = typeConfig.getElementConfig();
			return elemConfig;
		}
		
		default String getElemConfigId() {
			return getElemConfig().getConfigId();
		}
	}
	
	public interface MappedListModel<T, M> extends ListModel<T>, MappedModel<List<T>, List<M>> {
		@Override
		default MappedListModel<T, M> findIfMapped() {
			return this;
		}
		@Override
		ListModel<M> getMapsTo();
	}
	
	public interface Param<T> extends EntityState<T>, State<T>, Notification.Producer<T> {//, Notification.ObserveOn<MappedParam<?, T>, Param<T>> {
		//@JsonIgnore 
		@Override
		ParamConfig<T> getConfig();
		
		T getLeafState();
		
		@JsonIgnore
		Model<?> getParentModel();
		
		StateType getType();
		
//		@JsonIgnore M7
//M8	Model<StateContextEntity> getContextModel();
		
		default boolean isLeaf() {
			return getConfig().isLeaf();
		}
		
		default boolean isLeafOrCollectionWithLeafElems() {
			return isLeaf() || (isCollection() && findIfCollection().isLeafElements());
		}
		
		default LeafParam<T> findIfLeaf() {
			return null;
		}
		
		default MappedParam<T, ?> findIfMapped() {
			return null;
		}
		
		default boolean isCollection() {
			return false;
		}
		
		default boolean isNested() {
			return getType().isNested();
		}
		
		default Model<T> findIfNested() {
			return isNested() ? getType().<T>findIfNested().getModel() : null;
		} 
		
		default boolean isCollectionElem() {
			return false;
		}
		
		default ListParam findIfCollection() {
			return null;
		}
		
		default ListElemParam<T> findIfCollectionElem() {
			return null;
		}
		
		@JsonIgnore
		default boolean isLinked() {
			return false;
		}
		
		default Param<?> findIfLinked() {
			return null;
		}
		
		@JsonIgnore
		default boolean isTransient() {
			return false;
		}
		
		default MappedTransientParam<T, ?> findIfTransient() {
			return null;
		}
		
		@JsonIgnore
		PropertyDescriptor getPropertyDescriptor();
		
		@JsonIgnore
		boolean isActive();
		void activate();
		void deactivate();
	
		boolean isVisible();
		void setVisible(boolean visible);
		
		boolean isEnabled();
		void setEnabled(boolean enabled);
		
		List<ParamValue> getValues();
		void setValues(List<ParamValue> values);
		
		@Getter @Setter @EqualsAndHashCode
		public static class Message {
			public enum Type {
				INFO,
				WARNING,
				DANGER,
				SUCCESS;
			}
				
			private String text;
			private Type type;

		}
		
		Message getMessage();
		void setMessage(Message msg);
		
		
		void onStateLoadEvent();
		void onStateChangeEvent(ExecutionTxnContext txnCtx, Action a);
	}
	
	public interface LeafParam<T> extends Param<T> {
	
		@Override
		default LeafParam<T> findIfLeaf() {
			return this;
		}
		
		T getTransientOldState();
	}
	
	public interface MappedParam<T, M> extends Param<T>, Mapped<T, M>, Notification.Consumer<M> {
		@Override
		default MappedParam<T, M> findIfMapped() {
			return this;
		}

		@JsonIgnore @Override
		Param<M> getMapsTo();
		
		@JsonIgnore
		default boolean requiresConversion() {
			if(isLeaf()) return false;
			
			if(isTransient() && !findIfTransient().isAssinged()) { // when transient is not assigned
				Class<?> mappedClass = getType().getConfig().getReferredClass();
				Class<?> mapsToClass = getType().getConfig().findIfNested().getModel().findIfMapped().getMapsTo().getReferredClass();
				
				return (mappedClass!=mapsToClass);
			}
			
			Class<?> mappedClass = getType().findIfNested().getModel().getConfig().getReferredClass();
			Class<?> mapsToClass = getMapsTo().getType().findIfNested().getModel().getConfig().getReferredClass();

			// conversion required when mappedClass and mapsToClass are NOT same
			return (mappedClass!=mapsToClass);
		}
	}
	
	public interface MappedTransientParam<T, M> extends MappedParam<T, M> {
		@Override
		default boolean isTransient() {
			return true;
		}
		
		@Override
		default MappedTransientParam<T, M> findIfTransient() {
			return this;
		}

		@JsonIgnore
		default boolean isAssinged() {
			return getMapsTo() != null;
		}
		
		default void assignMapsTo(String rootMapsToPath) {
			Param<M> mapsToTransient = findParamByPath(rootMapsToPath);
			assignMapsTo(mapsToTransient);
		}
		void assignMapsTo(Param<M> mapsToTransient);
		void unassignMapsTo();
	}
	
	public interface ListBehavior<T> {
		/*
		boolean remove(Param<T> p);
		Param<T> remove(int i);
		Param<T> set(int i, Param<T> p);*/
		
		String toElemId(int i);
		int fromElemId(String elemId);
		
		int size();
		
		T getState(int i);
		T getLeafState(int i);
		
		boolean add(T elem);
		Param<T> add();
		boolean add(ListElemParam<T> pColElem);
		
		boolean remove(ListElemParam<T> pColElem);
		
		void clear();
		
		boolean contains(Param<?> other);
	}
	
	
	public interface ListParam<T> extends Param<List<T>>, ListBehavior<T> {
		@Override
		StateType.NestedCollection<T> getType();
		
		@Override
		default MappedListParam<T, ?> findIfMapped() {
			return null;
		}
		
		default boolean isCollection() {
			return true;
		}
		
		default boolean isLeafElements() {
			return getType().isLeafElements();
		}
		
		@Override
		default ListParam<T> findIfCollection() {
			return this;
		}
		
		ListElemParam<T> createElement();
		
		@Override
		ListElemParam<T> add();
		
	}
	
	public interface MappedListParam<T, M> extends ListParam<T>, MappedParam<List<T>, List<M>> {
		@JsonIgnore @Override
		ListParam<M> getMapsTo();
		
		@Override
		default MappedListParam<T, M> findIfMapped() {
			return this;
		}

		@Override
		default boolean requiresConversion() {
			Class<?> mappedElemClass = getType().findIfCollection().getModel().getElemConfig().getReferredClass();
			Class<?> mapsToElemClass = getMapsTo().getType().findIfCollection().getModel().getElemConfig().getReferredClass();
			
			// conversion required when mappedClass and mapsToClass are NOT same
			return (mappedElemClass!=mapsToElemClass);
		}
	}
	
	public interface ListElemParam<E> extends Param<E> {
		String getElemId();
		
		@JsonIgnore
		int getElemIndex();
		
		@JsonIgnore @Override
		ListModel<E> getParentModel();
		
		@Override
		default MappedListElemParam<E, ?> findIfMapped() {
			return null;
		}
		
		@Override
		default boolean isCollectionElem() {
			return true;
		}
		
		@Override
		default ListElemParam<E> findIfCollectionElem() {
			return this;
		}
		
		boolean remove();
	}	
	
	public interface MappedListElemParam<E, M> extends ListElemParam<E>, MappedParam<E, M> {
//		@Override
//		ListElemParam<M> getMapsTo();
		
		@Override
		default MappedListElemParam<E, M> findIfMapped() {
			return this;
		}
	}
}
