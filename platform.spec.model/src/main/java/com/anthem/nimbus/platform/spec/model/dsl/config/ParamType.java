/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

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
		list,
		page
	}
	
	
	final private boolean nested;
	
	final private String name;
	
	final private Class<?> referredClass;
	
	private CollectionType collection;
	
	
	public <T> Nested<T> findIfNested() {
		return null;
	}
	
	protected ParamType newInstance(boolean nested, String name) {
		return newInstance(nested, name);
	}
	
	public <T> ParamType clone(Class<? extends Annotation> ignore[], Map<Class<T>, ModelConfig<T>> visitedModels) {
		return this;
	}
	
	
	
	@Getter @Setter @ToString(callSuper=true) 
	public static class Field extends ParamType implements Serializable {

		private static final long serialVersionUID = 1L;

		
		public Field(String name, Class<?> referredClass) {
			super(false, name, referredClass);
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
		public Nested<T> findIfNested() {
			return this;
		}
		
		@Override
		protected Nested<T> newInstance(boolean nested, String name) {
			return new Nested<>(name, getReferredClass());
		}
		
		/**
		 * 
		 */
		@SuppressWarnings("unchecked")
		@Override
		public <M> Nested<T> clone(Class<? extends Annotation> ignore[], Map<Class<M>, ModelConfig<M>> visitedModels) {
			Nested<T> cloned = newInstance(true, getName());
			cloned.setCollection(getCollection());

			cloned.model = (model == null) ? null
					: (visitedModels.containsKey(model.getReferredClass())
							? (ModelConfig<T>) visitedModels.get(model.getReferredClass()) : model.clone(ignore));

			return cloned;
		}
	}

}
