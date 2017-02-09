/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.util.CollectionUtils;

import com.anthem.nimbus.platform.spec.model.Findable;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public class CollectionsTemplate<T extends Collection<E>, E> {

	protected final Supplier<T> getter;

	protected final Consumer<T> setter;

	protected final Supplier<? extends T> creator;

	
	public static <E> CollectionsTemplate<List<E>, E> array(Supplier<List<E>> getter, Consumer<List<E>> setter) {
		return new CollectionsTemplate<>(getter, setter, ()->new ArrayList<>());
	}

	public static <E> CollectionsTemplate<List<E>, E> linked(Supplier<List<E>> getter, Consumer<List<E>> setter) {
		return new CollectionsTemplate<>(getter, setter, ()->new LinkedList<>());
	}
	
	
   public boolean isNullOrEmpty() {
		return CollectionUtils.isEmpty(getter.get());
	}

	public T get() {
		return getter.get();
	}

	public void set(T t) {
		setter.accept(t);
	}
	
	
	public int size() {
		return isNullOrEmpty() ? 0 : get().size();
	}
	
	/**
	 * 
	 * @return
	 */
	protected Collection<E> createOrGet() {
		if(get() == null) {
			set(creator.get());
		}
		
		return get();
	}
	
	/**
	 * 
	 * @param elem
	 * @return
	 */
	public CollectionsTemplate<T, E> add(E elem) {
		if(elem != null) {
			createOrGet().add(elem);
		}
		
		return this;
	}
	
	/**
	 * 
	 * @param o
	 * @param toAdd
	 * @return
	 */
	public E getOrAdd(Object o, Supplier<E> toAdd) {
		E exists = find(o);
		if(exists != null) return exists;
		
		E add = toAdd.get();
		if(add != null) {
			createOrGet().add(add);
		}
		
		return add;
	}
	
	@SuppressWarnings("unchecked")
	public <M extends E> M find(Class<M> clazz, Object o) {
		return (M) find(o);
	}
	
	@SuppressWarnings("unchecked")
	public E getElem(int i) {
		if(isNullOrEmpty() || i>=size()) return null;
		
		T col = get();
		if(col instanceof List) {
			return ((List<E>)col).get(i);
		} else {
			return (E)get().toArray(new Object[size()])[i];
		}
	}
	
	@SuppressWarnings("unchecked")
	public E find(Object o) {
		T col = getter.get();
		
		if(CollectionUtils.isEmpty(col)) return null;
		
		for(E elem : col) {
			if(elem != null) {
				if(elem instanceof Findable) {
					if(((Findable<Object>) elem).isFound(o)) {
						return elem;
					}
				} 
				else if(elem.equals(o)) {
					return elem;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public boolean contains(E other) {
		T col = get();
		if(CollectionUtils.isEmpty(col)) return false;
		
		boolean r = col.contains(other);
		return r;
	}
	
}
