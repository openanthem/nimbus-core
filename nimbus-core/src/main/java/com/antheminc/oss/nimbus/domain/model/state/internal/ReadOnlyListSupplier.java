/**
 *  Copyright 2016-2019 the original author or authors.
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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import com.antheminc.oss.nimbus.InvalidOperationAttemptedException;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.ListParam;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class ReadOnlyListSupplier<E> implements List<E> {

	private static final String ERROR_MSG = "param: ";
	private final ListParam<E> p;
	
	private CollectionsTemplate<List<Param<?>>, Param<?>> templateParams() {
		return p.getType().getModel().templateParams();
	}
	
	private InvalidOperationAttemptedException ex() {
		return new InvalidOperationAttemptedException(ERROR_MSG + this.p);
	}
	
	@Override
	public E get(int index) {
		return p.getLeafState(index);
	}
	
	@Override
	public int size() {
		return p.size();
	}

	@Override
	public boolean isEmpty() {
		return templateParams().isNullOrEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return templateParams().find(o) != null;
	}

	@Override
	public Iterator<E> iterator() {
		throw ex();
	}

	@Override
	public Object[] toArray() {
		return Optional.ofNullable(p.getLeafState()).map(List::toArray).orElse(Collections.emptyList().toArray());
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return a;
	}

	@Override
	public boolean add(E e) {
		throw ex();
	}

	@Override
	public boolean remove(Object o) {
		throw ex();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw ex();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw ex();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw ex();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw ex();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw ex();
	}

	@Override
	public void clear() {
		throw ex();
	}

	@Override
	public E set(int index, E element) {
		throw ex();
	}

	@Override
	public void add(int index, E element) {
		throw ex();
	}

	@Override
	public E remove(int index) {
		throw ex();
	}

	@Override
	public int indexOf(Object o) {
		throw ex();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw ex();
	}

	@Override
	public ListIterator<E> listIterator() {
		throw ex();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		throw ex();
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw ex();
	}
}
