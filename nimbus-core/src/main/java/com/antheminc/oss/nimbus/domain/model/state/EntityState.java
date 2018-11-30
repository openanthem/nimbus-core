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
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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
	void initState(boolean doInternalStateInit);
	
	default void initState() {
		initState(true);
	}
	
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
		private final PropertyDescriptor pd;
		
		public Method getReadMethod() {
			return pd.getReadMethod();
		}
		
		public Method getWriteMethod() {
			return pd.getWriteMethod();
		}
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
		void setState(T state);

		boolean isNew();
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
		
		/**
		 * <p>Execute {@code consumer} on each of the nested params belonging to this param instance.
		 * <p>This method will only traverse the nested params directly underneath the parent in the param
		 * hierarchy.
		 * @param consumer the method to execute
		 */
		default void traverseChildren(Consumer<Param<?>> consumer) {
			traverse(consumer, 1, false);
		}
		
		/**
		 * <p>Execute {@code consumer} on each of the params belonging to the parent of this param instance, excluding
		 * this param instance.
		 * <p>This method will only traverse the sibling params directly underneath the parent in the param
		 * hierarchy. If needing to traverse recursively, use {@link #traverseSiblingsRecursively(Consumer)}.
		 * @param consumer the method to execute
		 */
		default void traverseSiblings(Consumer<Param<?>> consumer) {
			traverseParent(consumer, false, false);
		}
		
		/**
		 * <p>Execute {@code consumer} on each of the params belonging to the parent of this param instance, excluding
		 * this param instance.
		 * <p>This method will traverse recursively. 
		 * @param consumer the method to execute
		 */
		default void traverseSiblingsRecursively(Consumer<Param<?>> consumer) {
			traverseParent(consumer, true, false);
		}
		
		/**
		 * <p>Execute {@code consumer} on each of the params belonging to the parent of this param instance, including
		 * this param instance.
		 * <p>This method will only traverse the nested params directly underneath the parent in the param
		 * hierarchy. If needing to traverse recursively, use {@link #traverseParentRecursively(Consumer)}.
		 * @param consumer the method to execute
		 */
		default void traverseParent(Consumer<Param<?>> consumer) {
			traverseParent(consumer, false, true);
		}
		
		/**
		 * <p>Execute {@code consumer} on each of the params belonging to the parent of this param instance, including
		 * this param instance.
		 * <p>This method will traverse recursively. 
		 * @param consumer the method to execute
		 */
		default void traverseParentRecursively(Consumer<Param<?>> consumer) {
			traverseParent(consumer, true, true);
		}
		
		/**
		 * <p>Execute {@code consumer} on each of the params belonging to the parent of this param instance. Providing 
		 * {@code recursive} will give the ability to recursively execute {@code consumer} on each nested param found 
		 * under each parent model param.
		 * <p>{@code includeSelf} may be used to include whether or not {@code consumer} should be executed on the root
		 * param instance, or the first param in the param tree hierarchy.
		 * @param consumer the method to execute
		 * @param recursive when {@code true}, will recursively execute {@code consumer} on each available nested param
		 * found under parent model params
		 * @param includeSelf when {@code true}, includes the root param instance in the set of params to traverse
		 */
		default void traverseParent(Consumer<Param<?>> consumer, boolean recursive, boolean includeSelf) {
			
			List<Param<?>> params;
			if (null == getParentModel() || null == (params = getParentModel().getParams())) {
				return;
			}
			
			Stream<Param<?>> pStream = params.stream();
			
			if (!includeSelf) {
				pStream = pStream.filter(p -> p != this);
			}
			
			pStream.forEach(p -> {
				if (!recursive) {
					consumer.accept(p);
				} else {
					p.traverse(consumer, true);
				}
			});
		}
		
		/**
		 * <p>Find all available nested params of this param instance and executes {@code consumer} from 
		 * the context of each param found.
		 * <p>This method will execute {@code consumer} recursively on all nested params found underneath
		 * this param's hierarchy.
		 * <p>If needing to execute {@code consumer} from this param instance as well, consider 
		 * {@link #traverse(Consumer, boolean)}
		 * @param consumer the method to execute
		 */
		default void traverse(Consumer<Param<?>> consumer) {
			traverse(consumer, false);
		}
		
		/**
		 * <p>Find all available nested params of this param instance and executes {@code consumer} from 
		 * the context of each param found.
		 * <p>This method will execute {@code consumer} recursively on all nested params found underneath
		 * this param's hierarchy.
		 * @param consumer the method to execute
		 * @param execute whether or not to execute {@code consumer} on this param instance.
		 */
		default void traverse(Consumer<Param<?>> consumer, boolean execute) {
			traverse(consumer, Integer.MAX_VALUE, execute);
		}
		
		/**
		 * <p>Find all available nested params of this param instance and executes {@code consumer} from 
		 * the context of each param found (including the root param instance).
		 * <p>Providing {@code depth} will limit the levels of recursion applied when traversing this
		 * param instance. If {@code depth} is greater than the actual depth of the nested param tree,
		 * the method will simply return at that point.
		 * @param consumer the method to execute
		 * @param depth the number of levels of recursion this method will traverse into this param 
		 * instance. If depth is less than 0, this method will exit.
		 */
		default void traverse(Consumer<Param<?>> consumer, int depth) {
			traverse(consumer, depth, true);
		}
		
		/**
		 * <p>Find all available nested params of this param instance and executes {@code consumer} from 
		 * the context of each param found.
		 * <p>Providing {@code depth} will limit the levels of recursion applied when traversing this
		 * param instance. If {@code depth} is greater than the actual depth of the nested param tree,
		 * the method will simply return at that point.
		 * @param consumer the method to execute
		 * @param depth the number of levels of recursion this method will traverse into this param 
		 * instance. If depth is less than 0, this method will exit.
		 * @param execute whether or not to execute {@code consumer} on this param instance.
		 */
		default void traverse(Consumer<Param<?>> consumer, int depth, boolean execute) {
			if (depth-- <= -1) {
				return;
			}
			
			List<Param<?>> nestedParams;
			if (isNested() && null != (nestedParams = findIfNested().getParams())) {
				for(Param<?> nestedParam: nestedParams) {
					nestedParam.traverse(consumer, depth, true);
				}
			}
			
			if (execute) {
				consumer.accept(this);
			}
		}
		
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
		
		boolean isEmpty();
		
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
		
		Set<LabelState> getLabels();
		void setLabels(Set<LabelState> labelState);
		LabelState getDefaultLabel();
		LabelState getLabel(String localeLanguageTag);
		
		StyleState getStyle();
		void setStyle(StyleState styleState);
		
		@Getter @Setter @ToString 
		public static class LabelState {
			private String locale; //default en-US
			private String text;		
			private String helpText;
			private String cssClass;
			
			public LabelState() {
				this.locale = Locale.getDefault().toLanguageTag();
			}
			
			@Override
			public boolean equals(Object obj) {
				if(obj==null && this.text==null)
					return true;
				
				if(!LabelState.class.isInstance(obj))
					return false;
				
				LabelState other = LabelState.class.cast(obj);
				
				if(StringUtils.equalsIgnoreCase(other.getLocale(), this.getLocale()) 
						&& StringUtils.equalsIgnoreCase(other.getText(), this.getText()))
					return true;
			
				return false;
			}
			
			@Override
			public int hashCode() {
				String concat = this.locale + this.text;
				return concat.hashCode();
			}
		}
		
		@Getter @Setter @ToString 
		public static class StyleState {
			
			private String cssClass;
			
			@Override
			public boolean equals(Object obj) {
				if(obj == null) {
					return false;
				}
				
				if (!LabelState.class.isInstance(obj)) {
					return false;
				}
				
				StyleState rhs = StyleState.class.cast(obj);
				
				return new EqualsBuilder()
						.append(this.cssClass, rhs.cssClass)
						.isEquals();
			}
			
			@Override
			public int hashCode() {
				return new HashCodeBuilder()
						.append(this.cssClass)
						.toHashCode();
			}
		}
		
		@Immutable
		@Getter @Setter @RequiredArgsConstructor @ToString
		public static class Message {
			public enum Type {
				INFO,
				WARNING,
				DANGER,
				SUCCESS;
			}
			public enum Context {			
				INLINE,
				TOAST
			}
				
			@JsonIgnore
			private final String uniqueId;
			private final String text;
			private final Type type;
			private final Context context;
			private final String styleClass;
			
			

			@Override
			public boolean equals(Object obj) {
				if(obj==null && this.text==null && this.type==null && this.context==null)
					return true;
				
				if(!Message.class.isInstance(obj))
					return false;
				
				Message other = Message.class.cast(obj);
				
				if(StringUtils.equalsIgnoreCase(other.getUniqueId(), this.getUniqueId()))
					return true;
			
				return false;
			}
			
			@Override
			public int hashCode() {
				String concat = "" + uniqueId;
				return concat.hashCode();
			}
		}

		Set<Message> getMessages();
		void setMessages(Set<Message> msgs);
		
		boolean hasContextStateChanged();
		
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
		boolean addAll(List<T> elems);
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
		
		Map<String, Set<LabelState>> getElemLabels();	
		void setElemLabels(Map<String, Set<LabelState>> elemLabels);
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
