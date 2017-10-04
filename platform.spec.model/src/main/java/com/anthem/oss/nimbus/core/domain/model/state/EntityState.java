/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.EntityConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamType;
import com.anthem.oss.nimbus.core.domain.model.state.internal.StateContextEntity;
import com.anthem.oss.nimbus.core.util.CollectionsTemplate;
import com.anthem.oss.nimbus.core.util.LockTemplate;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public interface EntityState<T> {

	public String getPath();
	
	@JsonIgnore
	public String getBeanPath();
	
	//@JsonIgnore
	public EntityConfig<T> getConfig();

	default String getConfigId() {
		return getConfig().getConfigId();
	}
	
	public <S> Model<S> findModelByPath(String path);
	public <S> Model<S> findModelByPath(String[] pathArr);

	public <P> Param<P> findParamByPath(String path);
	public <P> Param<P> findParamByPath(String[] pathArr);
	
	default <P> P findStateByPath(String path) {
		Param<P> param = findParamByPath(path);
		
		if(param==null)
			throw new InvalidConfigException("Param not found for given path: "+path+" relative to current param/model: "+this);
		
		return param.getState();
	}

	public void initSetup();
	public void initState();
	
	public EntityStateAspectHandlers getAspectHandlers();
	
	public void fireRules();
	
	@JsonIgnore
	public ExecutionModel<?> getRootExecution();
	
	@JsonIgnore
	public Model<?> getRootDomain();
	
	@JsonIgnore
	public LockTemplate getLockTemplate();
	
	default public boolean isRoot() {
		return false;
	}
	
	default boolean isMapped() {
		return false;
	}
	default public Mapped<T, ?> findIfMapped() {
		return null;
	}
	
	public interface Mapped<T, M> extends EntityState<T> {
		@Override
		default boolean isMapped() {
			return true;
		}
		@Override
		default public Mapped<T, M> findIfMapped() {
			return this;
		}
		public EntityState<M> getMapsTo();
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
		public Command getRootCommand();
		
		@JsonIgnore
		public ExecutionRuntime getExecutionRuntime();
		
		@JsonIgnore
		public Map<String, Object> getParamRuntimes();
		
		default public <U> U unwrap(Class<U> c) {
			if(c.isInstance(this))
				return c.cast(this);
			
			return null;
		}
	}
	
	public interface Model<T> extends EntityState<T> { 
		
		//@JsonIgnore 
		@Override
		public ModelConfig<T> getConfig();
		
		@JsonIgnore
		public Param<T> getAssociatedParam();
		
		@JsonIgnore @Override
		default Model<?> getRootDomain() {
			return getAssociatedParam().getRootDomain();
		}
		
		default ExecutionModel<T> findIfRoot() {
			return null;
		}
		
		@Override
		default public MappedModel<T, ?> findIfMapped() {
			return null;
		}
		
		public List<Param<? extends Object>> getParams();
		
		default public ListModel<?> findIfListModel() {
			return null;
		}
		
		public CollectionsTemplate<List<Param<?>>, Param<?>> templateParams();
		
		public T instantiateOrGet();
		public T instantiateAndSet();
		
		default public T getLeafState() {
			return Optional.ofNullable(getAssociatedParam()).map(p->p.getLeafState()).orElse(null);
		}
		
		default public T getState() {
			return Optional.ofNullable(getAssociatedParam()).map(p->p.getState()).orElse(null);
		}
		default public void setState(T state) {
			Optional.ofNullable(getAssociatedParam()).ifPresent(p->p.setState(state));
		}
		
	}
	
	public interface MappedModel<T, M> extends Model<T>, Mapped<T, M> {
		@Override
		default public MappedModel<T, M> findIfMapped() {
			return this;
		}
		
		@Override
		public Model<M> getMapsTo();
	}
	
	public interface ListModel<T> extends Model<List<T>>, ListBehavior<T> {
		@Override
		default public MappedListModel<T, ?> findIfMapped() {
			return null;
		}
		
		@Override
		default ListModel<T> findIfListModel() {
			return this;
		}
		
		public ListElemParam<T> createElement(String elemId);
		
		@Override
		public ListElemParam<T> add();
		
		//@JsonIgnore
		default public ParamConfig<T> getElemConfig() {
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
		default public MappedListModel<T, M> findIfMapped() {
			return this;
		}
		@Override
		public ListModel<M> getMapsTo();
	}
	
	public interface Param<T> extends EntityState<T>, State<T>, Notification.Producer<T> {//, Notification.ObserveOn<MappedParam<?, T>, Param<T>> {
		//@JsonIgnore 
		@Override
		public ParamConfig<T> getConfig();
		
		public T getLeafState();
		
		@JsonIgnore
		public Model<?> getParentModel();
		
		public StateType getType();
		
		//@JsonIgnore
		public Model<StateContextEntity> getContextModel();
		
		default boolean isLeaf() {
			return getConfig().isLeaf();
		}
		
		default public MappedParam<T, ?> findIfMapped() {
			return null;
		}
		
		default public boolean isCollection() {
			return false;
		}
		
		default public boolean isNested() {
			return getType().isNested();
		}
		
		default public Model<T> findIfNested() {
			return isNested() ? getType().<T>findIfNested().getModel() : null;
		} 
		
		default public boolean isCollectionElem() {
			return false;
		}
		
		default public ListParam findIfCollection() {
			return null;
		}
		
		default public ListElemParam<T> findIfCollectionElem() {
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
		default public boolean isTransient() {
			return false;
		}
		
		default public MappedTransientParam<T, ?> findIfTransient() {
			return null;
		}
		
		@JsonIgnore
		public PropertyDescriptor getPropertyDescriptor();
		
		@JsonIgnore
		public boolean isActive();
		public void activate();
		public void deactivate();
		
	}
	
	public interface MappedParam<T, M> extends Param<T>, Mapped<T, M>, Notification.Consumer<M> {
		@Override
		default public MappedParam<T, M> findIfMapped() {
			return this;
		}

		@JsonIgnore @Override
		public Param<M> getMapsTo();
		
		@JsonIgnore
		default public boolean requiresConversion() {
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
		default public boolean isAssinged() {
			return getMapsTo() != null;
		}
		
		default void assignMapsTo(String rootMapsToPath) {
			Param<M> mapsToTransient = findParamByPath(rootMapsToPath);
			assignMapsTo(mapsToTransient);
		}
		public void assignMapsTo(Param<M> mapsToTransient);
		public void unassignMapsTo();
	}
	
	public interface ListBehavior<T> {
		/*
		public boolean remove(Param<T> p);
		public Param<T> remove(int i);
		public Param<T> set(int i, Param<T> p);*/
		
		public String toElemId(int i);
		public int fromElemId(String elemId);
		
		public int size();
		
		public T getState(int i);
		public T getLeafState(int i);
		
		public boolean add(T elem);
		public Param<T> add();
		public boolean add(ListElemParam<T> pColElem);
		
		public boolean remove(ListElemParam<T> pColElem);
		
		public void clear();
		
		public boolean contains(Param<?> other);
	}
	
	
	public interface ListParam<T> extends Param<List<T>>, ListBehavior<T> {
		@Override
		public StateType.NestedCollection<T> getType();
		
		@Override
		default public MappedListParam<T, ?> findIfMapped() {
			return null;
		}
		
		default public boolean isCollection() {
			return true;
		}
		
		@Override
		default ListParam<T> findIfCollection() {
			return this;
		}
		
		public ListElemParam<T> createElement();
		
		@Override
		public ListElemParam<T> add();
		
	}
	
	public interface MappedListParam<T, M> extends ListParam<T>, MappedParam<List<T>, List<M>> {
		@JsonIgnore @Override
		public ListParam<M> getMapsTo();
		
		@Override
		default public MappedListParam<T, M> findIfMapped() {
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
		public String getElemId();
		
		@JsonIgnore
		public int getElemIndex();
		
		@JsonIgnore @Override
		public ListModel<E> getParentModel();
		
		@Override
		default public MappedListElemParam<E, ?> findIfMapped() {
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
		
		public boolean remove();
	}	
	public interface MappedListElemParam<E, M> extends ListElemParam<E>, MappedParam<E, M> {
//		@Override
//		public ListElemParam<M> getMapsTo();
		
		@Override
		default public MappedListElemParam<E, M> findIfMapped() {
			return this;
		}
	}
}
