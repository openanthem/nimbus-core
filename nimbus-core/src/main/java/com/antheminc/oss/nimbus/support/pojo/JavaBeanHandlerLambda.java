///**
// *  Copyright 2016-2019 the original author or authors.
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *         http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// */
//package com.antheminc.oss.nimbus.support.pojo;
//
//import java.lang.invoke.MethodHandle;
//import java.util.Optional;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//
//import com.antheminc.oss.nimbus.InvalidConfigException;
//import com.antheminc.oss.nimbus.domain.model.state.EntityState.ValueAccessor;
//import com.antheminc.oss.nimbus.domain.model.state.InvalidStateException;
//
//
///**
// * @author Soham Chakravarti
// *
// */
//public class JavaBeanHandlerLambda implements JavaBeanHandler {
//
//	private JavaBeanHandlerReflection reflection = new JavaBeanHandlerReflection();
//	
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public <T> T getValue(ValueAccessor va, Object target) {
//		if(target==null)
//			return null;
//		
//		try {
//			MethodHandle mh = Optional.ofNullable(va.getReadMethodHandle())
//								.orElseThrow(()->new InvalidConfigException("getter called when none found: "+va));
//			
//			
//			Function getter = (Function)mh.invoke();
//			return (T)getter.apply(target);
//			
//		} catch (Throwable t) {
//			throw new InvalidStateException("POJO Get failed for pd: "+va+" on target: "+target, t);
//		}
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public <T> void setValue(ValueAccessor va, Object target, T value) {
//		try {
//			MethodHandle mh = Optional.ofNullable(va.getWriteMethodHandle())
//					.orElseThrow(()->new InvalidConfigException("setter called when none found: "+va));
//			
//			BiConsumer setter = (BiConsumer)mh.invoke();
//			setter.accept(target, value);
//			
//		} catch (Throwable t) {
//			throw new InvalidStateException("POJO Set failed for pd: "+va+" on target: "+target+" with value: "+value, t);
//		}
//	}
//
//	@Override
//	public <T> T instantiate(Class<T> clazz) {
//		return reflection.instantiate(clazz);
//	}
//	
//}
