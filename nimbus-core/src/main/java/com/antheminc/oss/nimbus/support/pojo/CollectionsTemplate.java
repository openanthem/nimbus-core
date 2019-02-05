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
package com.antheminc.oss.nimbus.support.pojo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import com.antheminc.oss.nimbus.entity.Findable;

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
	
	protected Collection<E> createOrGet() {
		if(get() == null) {
			set(creator.get());
		}
		
		return get();
	}
	
	public CollectionsTemplate<T, E> add(E elem) {
		if(elem != null) {
			createOrGet().add(elem);
		}
		
		return this;
	}
	
	public E remove(Object o) {
		E foundElem = find(o);
		
		Optional.ofNullable(foundElem)
			.ifPresent(e->get().remove(e));
		
		return foundElem;
	}
	
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
	
	public int indexOf(E elem) {
		if(isNullOrEmpty())
			return -1;
		
		if(getter.get() instanceof List) {
			return ((List<E>)getter.get()).indexOf(elem);
		} 
		
		ArrayList<E> col = new ArrayList<>(getter.get());
		return col.indexOf(elem);
	}
	
	public int lastIndexOf(E elem) {
		if(isNullOrEmpty())
			return -1;
		
		if(getter.get() instanceof List) {
			return ((List<E>)getter.get()).lastIndexOf(elem);
		} 
		
		ArrayList<E> col = new ArrayList<>(getter.get());
		return col.lastIndexOf(elem);
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
	
	public boolean contains(E other) {
		T col = get();
		if(CollectionUtils.isEmpty(col)) return false;
		
		boolean r = col.contains(other);
		return r;
	}
	
	
	/**
	 * Sorts the input list with provided comparator. This method updates the input list with sorted elements 
	 * 
	 * @param src
	 * @param comparator
	 * @param reverse
	 */
	public static <E> void sortSelf(final List<E> src, final Comparator<E> comparator) {
		Optional.ofNullable(src).ifPresent(list -> list.sort(comparator));
	}
	
	/**
	 * Sorts the input list with provided comparator in reverse. This method updates the input list containing sorted elements 
	 * 
	 * @param src
	 * @param comparator
	 * @param reverse
	 */
	public static <E> void sortSelfReverse(final List<E> src, final Comparator<E> comparator) {
		 Optional.ofNullable(src).ifPresent(list -> list.sort(comparator.reversed()));
	}
	
	
	/**
	 * Sorts the input list with provided comparator. This method returns the new list containing sorted elements, input list remains unchanged
	 * 
	 * @param src
	 * @param comparator
	 * @param reverse
	 */
	public static <E> List<E> sortReturnNew(final List<E> src, final Comparator<E> comparator) {
		return Optional.ofNullable(src)
				.orElseGet(Collections::emptyList)
				.stream()
				.sorted(comparator)
				.collect(Collectors.toList());
	}
	
	/**
	 * Sorts the input list with provided comparator in reverse. This method returns the new list containing sorted elements, input list remains unchanged
	 * 
	 * @param src
	 * @param comparator
	 * @param reverse
	 */
	public static <E> List<E> sortReverseReturnNew(final List<E> src, final Comparator<E> comparator) {
		return sortReturnNew(src, comparator.reversed());
	}
	
}
