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
package com.antheminc.oss.nimbus.support.pojo;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Assert;
import org.junit.Test;

import com.antheminc.oss.nimbus.entity.person.Address;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
public class GenericResolverTest {

	@Getter @Setter
	public static class Base<ID extends Serializable, A extends Address> {
		private String id;
		private String[] names;
		private ID[] idArray;
		
		private Set<A> addresses;
		private A oneAddress;
		private Address nonCollectionParameterizedField;
		private Set<? extends A> wildcardAddresses;
		
		private List<Long> longList;
	}
	
	@Getter @Setter
	public static class SubClass<T extends Collection<E>, E> extends Base<String, Address> {
		private List<T> complicatedGeneric;
		private T genericSubclassLoneRanger;
	}
	
	public static class LeafClass extends SubClass<Set<Integer>, Integer> {
		
	}
	
	
	@Test
	public void test_id() {
		Class<?> c = subClassField("id");
		Assert.assertEquals(String.class, c);
	}

	@Test
	public void test_names() {
		Class<?> c = subClassField("names");
		Assert.assertEquals(String.class, c);
	}
	
	@Test
	public void test_idArray() {
		Class<?> c = subClassField("idArray");
		Assert.assertEquals(String.class, c);
	}
	
	@Test
	public void test_addresses() {
		Class<?> c = subClassField("addresses");
		Assert.assertEquals(Address.class, c);
	}

	@Test
	public void test_oneAddress() {
		Class<?> c = subClassField("oneAddress");
		Assert.assertEquals(Address.class, c);
	}

	
	@Test
	public void test_nonCollectionParameterizedField() {
		Class<?> c = subClassField("nonCollectionParameterizedField");
		Assert.assertEquals(Address.class, c);
	}
	
	@Test
	public void test_wildcardAddresses() {
		Class<?> c = subClassField("wildcardAddresses");
		Assert.assertEquals(Address.class, c);
	}	
	
	@Test
	public void test_longList() {
		Class<?> c = subClassField("longList");
		Assert.assertEquals(Long.class, c);
	}	
	
	@Test
	public void test_complicatedGeneric() {
		Class<?> c = subClassField("complicatedGeneric");
		Assert.assertEquals(Set.class, c);
	}
	
	@Test
	public void test_genericSubclassLoneRanger() {
		Class<?> c = subClassField("genericSubclassLoneRanger");
		Assert.assertEquals(Set.class, c);
	}
	
	private static Class<?> subClassField(String fNm) {
		Field f = FieldUtils.getField(LeafClass.class, fNm, true);
		return GenericUtils.resolveGeneric(LeafClass.class, f.getDeclaringClass(), f.getType(), f.getGenericType());
	}
}
