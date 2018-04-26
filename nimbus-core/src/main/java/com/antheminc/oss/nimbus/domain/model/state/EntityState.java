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

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.extension.ValidateConditional.ValidationGroup;
import com.antheminc.oss.nimbus.domain.model.config.EntityConfig;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;
import com.antheminc.oss.nimbus.support.pojo.LockTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

	String getConfigId();
	
	<S> Model<S> findModelByPath(String path);
	<S> Model<S> findModelByPath(String[] pathArr);

	<P> Param<P> findParamByPath(String path);
	<P> Param<P> findParamByPath(String[] pathArr);
	
	<P> P findStateByPath(String path);

	void initSetup();
	void initState();
	
	@JsonIgnore
	boolean isStateInitialized();
	void setStateInitialized(boolean initialized);
	
	@JsonIgnore
	EntityStateAspectHandlers getAspectHandlers();
	
	void fireRules();
	
	@JsonIgnore
	ExecutionModel<?> getRootExecution();
	
	@JsonIgnore
	Model<?> getRootDomain();
	
	@JsonIgnore
	LockTemplate getLockTemplate();
	
	@JsonIgnore
	boolean isRoot();
	
	@JsonIgnore
	boolean isMapped();
	
	Mapped<T, ?> findIfMapped();
	
	@Getter @RequiredArgsConstructor @ToString
	public static class ValueAccessor {
		@JsonIgnore
		final private Method readMethod;
		
		@JsonIgnore
		final private Method writeMethod;

		
		@JsonIgnore
		final private MethodHandle readMethodHandle;
		
		@JsonIgnore
		final private MethodHandle writeMethodHandle;
		
	}
	
	public interface Mapped<T, M> extends EntityState<T> {
		
		@JsonIgnore
		@Override
		boolean isMapped();
		
		@JsonIgnore
		EntityState<M> getMapsTo();
	}
	
	public interface ExecutionModel<T> extends Model<T> {
		
		@Override
		boolean isRoot();
//		@Override
//		default boolean isRoot() {
//			return true;
//		}
		
		@Override
		ExecutionModel<T> findIfRoot();
//		@Override
//		default ExecutionModel<T> findIfRoot() {
//			return this;
//		}
		
		@JsonIgnore
		Command getRootCommand();
		
		@JsonIgnore
		ExecutionRuntime getExecutionRuntime();
		
		@JsonIgnore
		Map<String, Object> getParamRuntimes();
		
		<U> U unwrap(Class<U> c);
//		default <U> U unwrap(Class<U> c) {
//			if(c.isInstance(this))
//				return c.cast(this);
//			
//			return null;
//		}
	}
	
	public interface Model<T> extends EntityState<T> { 
		
		//@JsonIgnore 
		@Override
		ModelConfig<T> getConfig();
		
		@JsonIgnore
		Param<T> getAssociatedParam();
		
		@JsonIgnore
		public Param<?> getIdParam();
		
		@JsonIgnore
		public Param<?> getVersionParam();
		
		@JsonIgnore @Override
		Model<?> getRootDomain();
//		@JsonIgnore @Override
//		default Model<?> getRootDomain() {
//			return getAssociatedParam().getRootDomain();
//		}
		
		ExecutionModel<T> findIfRoot();
//		default ExecutionModel<T> findIfRoot() {
//			return null;
//		}
		
		@Override
		MappedModel<T, ?> findIfMapped();
//		@Override
//		default MappedModel<T, ?> findIfMapped() {
//			return null;
//		}
		
		List<Param<? extends Object>> getParams();
		
		ListModel<?> findIfListModel();
//		default ListModel<?> findIfListModel() {
//			return null;
//		}
		
		CollectionsTemplate<List<Param<?>>, Param<?>> templateParams();
		
		T instantiateOrGet();
		T instantiateAndSet();
		
		T getLeafState();
//		default T getLeafState() {
//			return Optional.ofNullable(getAssociatedParam()).map(p->p.getLeafState()).orElse(null);
//		}
		
		T getState();
//		default T getState() {
//			return Optional.ofNullable(getAssociatedParam()).map(p->p.getState()).orElse(null);
//		}
		
		void setState(T state);
//		default void setState(T state) {
//			Optional.ofNullable(getAssociatedParam()).ifPresent(p->p.setState(state));
//		}
		
	}
	
	public interface MappedModel<T, M> extends Model<T>, Mapped<T, M> {
		@Override
		MappedModel<T, M> findIfMapped();
//		@Override
//		default MappedModel<T, M> findIfMapped() {
//			return this;
//		}
		
		@Override
		Model<M> getMapsTo();
	}
	
	public interface ListModel<T> extends Model<List<T>>, ListBehavior<T> {
		@Override
		MappedListModel<T, ?> findIfMapped();
//		@Override
//		default MappedListModel<T, ?> findIfMapped() {
//			return null;
//		}
		
		@Override
		ListModel<T> findIfListModel();
//		@Override
//		default ListModel<T> findIfListModel() {
//			return this;
//		}
		
		ListElemParam<T> createElement(String elemId);
		
		@Override
		ListElemParam<T> add();
		
		@JsonIgnore
		ParamConfig<T> getElemConfig();
//		@JsonIgnore
//		default ParamConfig<T> getElemConfig() {
//			StateType.NestedCollection<T> typeSAC = getAssociatedParam().getType().findIfCollection(); 
//			ParamConfigType.NestedCollection<T> typeConfig = typeSAC.getConfig().findIfCollection();
//			
//			ParamConfig<T> elemConfig = typeConfig.getElementConfig();
//			return elemConfig;
//		}
		
		String getElemConfigId();
//		default String getElemConfigId() {
//			return getElemConfig().getId();
//		}
	}
	
	public interface MappedListModel<T, M> extends ListModel<T>, MappedModel<List<T>, List<M>> {
		@Override
		MappedListModel<T, M> findIfMapped();
//		@Override
//		default MappedListModel<T, M> findIfMapped() {
//			return this;
//		}
		
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
		
		Class<? extends ValidationGroup>[] getActiveValidationGroups();
		void setActiveValidationGroups(Class<? extends ValidationGroup>[] activeValidationGroups);
		
		@JsonIgnore
		boolean isLeaf();
//		@JsonIgnore
//		default boolean isLeaf() {
//			return getConfig().isLeaf();
//		}
		
		@JsonIgnore
		boolean isLeafOrCollectionWithLeafElems();
//		@JsonIgnore
//		default boolean isLeafOrCollectionWithLeafElems() {
//			return isLeaf() || (isCollection() && findIfCollection().isLeafElements());
//		}
		
		LeafParam<T> findIfLeaf();
//		default LeafParam<T> findIfLeaf() {
//			return null;
//		}
		
		MappedParam<T, ?> findIfMapped();
//		default MappedParam<T, ?> findIfMapped() {
//			return null;
//		}
		
		boolean isCollection();
//		default boolean isCollection() {
//			return false;
//		}
		
		boolean isNested();
//		default boolean isNested() {
//			return getType().isNested();
//		}
		
		Model<T> findIfNested();
//		default Model<T> findIfNested() {
//			return isNested() ? getType().<T>findIfNested().getModel() : null;
//		} 
		
		boolean isCollectionElem();
//		default boolean isCollectionElem() {
//			return false;
//		}
		
		ListParam findIfCollection();
//		default ListParam findIfCollection() {
//			return null;
//		}
		
		ListElemParam<T> findIfCollectionElem();
//		default ListElemParam<T> findIfCollectionElem() {
//			return null;
//		}
		
		@JsonIgnore
		boolean isLinked();
//		@JsonIgnore
//		default boolean isLinked() {
//			return false;
//		}
		
		Param<?> findIfLinked();
//		default Param<?> findIfLinked() {
//			return null;
//		}
		
		@JsonIgnore
		boolean isTransient();
//		@JsonIgnore
//		default boolean isTransient() {
//			return false;
//		}
		
		MappedTransientParam<T, ?> findIfTransient();
//		default MappedTransientParam<T, ?> findIfTransient() {
//			return null;
//		}
		
//		@JsonIgnore
//		PropertyDescriptor getPropertyDescriptor();
		
//		@JsonIgnore
//		MethodHandle[] getMethodHandles();

		@JsonIgnore
		ValueAccessor getValueAccessor();
		
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
		
		@Immutable
		@Getter @Setter @RequiredArgsConstructor 
		public static class Message {
			public enum Type {
				INFO,
				WARNING,
				DANGER,
				SUCCESS;
			}
			public enum Context {			
				INLINE,
				GROWL;				
			}
				
			private final String text;
			private final Type type;
			private final Context context;
			

			@Override
			public boolean equals(Object obj) {
				if(obj==null && this.text==null && this.type==null && this.context==null)
					return true;
				
				if(!Message.class.isInstance(obj))
					return false;
				
				Message other = Message.class.cast(obj);
				
				if(StringUtils.equalsIgnoreCase(other.getText(), this.getText())
						&& other.getType()==this.getType() && other.getContext()==this.getContext())
					return true;
			
				return false;
			}
		}
		
		Message getMessage();
		void setMessage(Message msg);
		
		void onStateLoadEvent();
		void onStateChangeEvent(ExecutionTxnContext txnCtx, Action a);
	}
	
	public interface LeafParam<T> extends Param<T> {
	
		@Override
		boolean isLeaf();
		
		@Override
		LeafParam<T> findIfLeaf();
//		@Override
//		default LeafParam<T> findIfLeaf() {
//			return this;
//		}
		
		@JsonIgnore
		T getTransientOldState();
	}
	
	public interface MappedParam<T, M> extends Param<T>, Mapped<T, M>, Notification.Consumer<M> {
		
		@Override
		MappedParam<T, M> findIfMapped();
//		@Override
//		default MappedParam<T, M> findIfMapped() {
//			return this;
//		}

		@JsonIgnore @Override
		Param<M> getMapsTo();
		
		@JsonIgnore
		boolean requiresConversion();
//		@JsonIgnore
//		default boolean requiresConversion() {
//			if(isLeaf()) return false;
//			
//			if(isTransient() && !findIfTransient().isAssinged()) { // when transient is not assigned
//				Class<?> mappedClass = getType().getConfig().getReferredClass();
//				Class<?> mapsToClass = getType().getConfig().findIfNested().getModelConfig().findIfMapped().getMapsToConfig().getReferredClass();
//				
//				return (mappedClass!=mapsToClass);
//			}
//			
//			Class<?> mappedClass = getType().findIfNested().getModel().getConfig().getReferredClass();
//			Class<?> mapsToClass = getMapsTo().getType().findIfNested().getModel().getConfig().getReferredClass();
//
//			// conversion required when mappedClass and mapsToClass are NOT same
//			return (mappedClass!=mapsToClass);
//		}
	}
	
	public interface MappedTransientParam<T, M> extends MappedParam<T, M> {
		
		@Override
		boolean isTransient();
//		@Override
//		default boolean isTransient() {
//			return true;
//		}
		
		@Override
		MappedTransientParam<T, M> findIfTransient();
//		@Override
//		default MappedTransientParam<T, M> findIfTransient() {
//			return this;
//		}

		@JsonIgnore
		boolean isAssinged();
//		@JsonIgnore
//		default boolean isAssinged() {
//			return getMapsTo() != null;
//		}

		void assignMapsTo();
		
		void assignMapsTo(String rootMapsToPath);
//		default void assignMapsTo(String rootMapsToPath) {
//			Param<M> mapsToTransient = findParamByPath(rootMapsToPath);
//			assignMapsTo(mapsToTransient);
//		}
		void assignMapsTo(Param<M> mapsToTransient);
		void unassignMapsTo();
		
		void flush();
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
		MappedListParam<T, ?> findIfMapped();
		
		boolean isCollection();
		
		@JsonIgnore
		boolean isLeafElements();

		@Override
		ListParam<T> findIfCollection();

		@Override
		ListElemParam<T> add();
		
		Pageable getPageable();
		Supplier<Long> getTotalCountSupplier();
		
		Page<T> getPage();
		void setPage(List<T> content, Pageable pageable, Supplier<Long> totalCountSupplier);
		
	}
	
	public interface MappedListParam<T, M> extends ListParam<T>, MappedParam<List<T>, List<M>> {
		@JsonIgnore @Override
		ListParam<M> getMapsTo();
		
		@Override
		MappedListParam<T, M> findIfMapped();
//		@Override
//		default MappedListParam<T, M> findIfMapped() {
//			return this;
//		}

		@Override
		boolean requiresConversion();
//		@Override
//		default boolean requiresConversion() {
//			Class<?> mappedElemClass = getType().findIfCollection().getModel().getElemConfig().getReferredClass();
//			Class<?> mapsToElemClass = getMapsTo().getType().findIfCollection().getModel().getElemConfig().getReferredClass();
//			
//			// conversion required when mappedClass and mapsToClass are NOT same
//			return (mappedElemClass!=mapsToElemClass);
//		}
	}
	
	public interface ListElemParam<E> extends Param<E> {
		String getElemId();
		
		@JsonIgnore
		int getElemIndex();
		
		@JsonIgnore @Override
		ListModel<E> getParentModel();
		
		@Override
		MappedListElemParam<E, ?> findIfMapped();
//		@Override
//		default MappedListElemParam<E, ?> findIfMapped() {
//			return null;
//		}
		
		@Override
		boolean isCollectionElem();
//		@Override
//		default boolean isCollectionElem() {
//			return true;
//		}
		
		@Override
		ListElemParam<E> findIfCollectionElem();
//		@Override
//		default ListElemParam<E> findIfCollectionElem() {
//			return this;
//		}
		
		boolean remove();
	}	
	
	public interface MappedListElemParam<E, M> extends ListElemParam<E>, MappedParam<E, M> {
//		@Override
//		ListElemParam<M> getMapsTo();
		
		@Override
		MappedListElemParam<E, M> findIfMapped();
//		@Override
//		default MappedListElemParam<E, M> findIfMapped() {
//			return this;
//		}
	}
}
