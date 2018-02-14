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
package com.antheminc.oss.nimbus.domain.model.config;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString @RequiredArgsConstructor
public class ParamType implements Serializable {

	private static final long serialVersionUID = 1L;

	
	public enum CollectionType {
		array,
		list,
		page
	}
	
	
	final private boolean nested;
	
	final private String name;
	
	final private Class<?> referredClass;
	
	final private boolean array;
	
	public <T> Nested<T> findIfNested() {
		return null;
	}
	
	public <T> NestedCollection<T> findIfCollection() {
		return null;
	}
	
	public boolean isCollection() {
		return false;
	}
	
	
	
	@Getter @Setter @ToString(callSuper=true) 
	public static class Field extends ParamType implements Serializable {
		private static final long serialVersionUID = 1L;

		public Field(boolean isArray, String name, Class<?> referredClass) {
			super(false, resolveName(isArray, name), referredClass, isArray);
		}
		
		private static String resolveName(boolean isArray, String name) {
			return isArray ? "array-"+name : name;
		}
	}
	

	
	@Getter @Setter @ToString(callSuper=true)
	public static class Nested<T> extends ParamType implements Serializable {
		private static final long serialVersionUID = 1L;

		private ModelConfig<T> model;
		
		public Nested(String name, Class<?> referredClass) {
			super(true, name, referredClass, false);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Class<T> getReferredClass() {
			return (Class<T>)super.getReferredClass();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Nested<T> findIfNested() {
			return this;
		}
	}

	@Getter @Setter @ToString(callSuper=true)
	public static class NestedCollection<T> extends Nested<List<T>> implements Serializable {
		private static final long serialVersionUID = 1L;
		
		final private CollectionType collectionType;
		
		private ParamConfig<T> elementConfig;
		
		public NestedCollection(String name, Class<?> referredClass, CollectionType collectionType) {
			super(name, referredClass);
			this.collectionType = collectionType;
		}
		
		// TODO Temp till custom Serializer is implemented
		public List<? extends ParamConfig<?>> getElementModelParams() {
			if(getElementConfig().getType().isNested()) {
				return getElementConfig().getType().findIfNested().getModel().getParams();
			}
			return null;
		}
		
		@Override
		public boolean isCollection() {
			return true;
		}
		
		public boolean isLeafElements() {
			return elementConfig.isLeaf();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public NestedCollection<T> findIfCollection() {
			return this;
		}
		
		@Override
		public Class<List<T>> getReferredClass() {
			return super.getReferredClass();
		}
	}
}
