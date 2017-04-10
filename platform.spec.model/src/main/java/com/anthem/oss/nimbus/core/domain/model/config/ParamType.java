/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.config;

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
			super(false, resolveName(isArray, name), referredClass);
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
			super(true, name, referredClass);
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
