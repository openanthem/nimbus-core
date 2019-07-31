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

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;

import com.antheminc.oss.nimbus.InvalidConfigException;

/**
 * @author Soham Chakravarti
 *
 */
public final class GenericUtils {

	
	public static boolean isEssentiallyCollection(Class<?> clazz) {
		return Collection.class.isAssignableFrom(clazz) ||	//Collection 
				Page.class.isAssignableFrom(clazz);			//Page
	}
	
	
	public static Class<?> resolveGeneric(Class<?> leafClass, Field f) {
		return resolveGeneric(leafClass, f.getDeclaringClass(), f.getType(), f.getGenericType());
	}
	
	public static Class<?> resolveGeneric(Class<?> leafClass, Class<?> nodeClass, Class<?> nodeFieldTypeClass, Type nodeFieldGenericType) {
		
		//handle non generic types first
		if(nodeFieldGenericType == nodeFieldTypeClass) {
			if(nodeFieldTypeClass.isArray()) {	//Array
				return nodeFieldTypeClass.getComponentType();
			}  
			else if(isEssentiallyCollection(nodeFieldTypeClass)) {	//Collection w/o generic parameter: Collection
				throw throwEx("Found Collection without any generic parameter defined:", leafClass, nodeClass,
						nodeFieldTypeClass, nodeFieldGenericType);
			}
			return nodeFieldTypeClass;
			
		} 
		
		else if(nodeFieldGenericType instanceof ParameterizedType) {
			
			//return node field type if not a collection or Page eg: Address<String>
			if(!(isEssentiallyCollection(nodeFieldTypeClass))) {	
				return nodeFieldTypeClass;
			}
			
			//Collection<E>
			ParameterizedType pType = (ParameterizedType) nodeFieldGenericType;
			Type[] actualArgTypes = pType.getActualTypeArguments();
			Type elemType = actualArgTypes[0];
			
			Class<?> rClass = resolveGeneric(leafClass, nodeClass, nodeFieldTypeClass, elemType);
			return rClass;
		} 
		
		else if(nodeFieldGenericType instanceof GenericArrayType) {
			GenericArrayType gArrayType = (GenericArrayType) nodeFieldGenericType;
			Type compType = gArrayType.getGenericComponentType();
			Class<?> rClass = resolveGeneric(leafClass, nodeClass, nodeFieldTypeClass, compType);
			return rClass;
		} 
		
		else if(nodeFieldGenericType instanceof WildcardType) {
			WildcardType wType = (WildcardType)nodeFieldGenericType;
			Type lt[] = wType.getLowerBounds();
			Type ut[] = wType.getUpperBounds();
			
			//Give preference to upper bounds
			if(ArrayUtils.isNotEmpty(ut)) {
				Class<?> resolvedClass = resolveGeneric(leafClass, nodeClass, nodeFieldTypeClass, ut[0]);
				return resolvedClass;
			} 
			else if(ArrayUtils.isNotEmpty(lt)) {
				Class<?> resolvedClass = resolveGeneric(leafClass, nodeClass, nodeFieldTypeClass, lt[0]);
				return resolvedClass;
			}
			throw throwEx("Generic scenario not handled..yet. Found TypeVariable:", leafClass, nodeClass,
					nodeFieldTypeClass, nodeFieldGenericType);
			
		} 
		
		else if(nodeFieldGenericType instanceof TypeVariable) {
			TypeVariable<?> tVar = (TypeVariable<?>) nodeFieldGenericType;
			
			TypeVariable<? extends Class<?>>[] nodeClassTypParams = nodeClass.getTypeParameters();
			int tVarIndx = ArrayUtils.indexOf(nodeClassTypParams, tVar);
			
			if(tVarIndx == -1) 
				throw throwEx("Generic scenario not handled..yet. Found TypeVariable:", leafClass, nodeClass,
						nodeFieldTypeClass, nodeFieldGenericType);
			
			Class<?> nodeClassGenericTypes[] = GenericTypeResolver.resolveTypeArguments(leafClass, nodeClass);
			Class<?> resolvedClass = nodeClassGenericTypes[tVarIndx];
			//==Class<?> reResolve = resolveGeneric(leafClass, nodeClass, resolvedClass, tVar);
			return resolvedClass;
		}
		
		Class<?> resolvedClass = ResolvableType.forType(nodeFieldGenericType).resolve();
		if(resolvedClass != null) return resolvedClass;
		
		throw throwEx("Unhandled scenario..yet. Found TypeVariable:", leafClass, nodeClass, nodeFieldTypeClass, nodeFieldGenericType);
	}
	
	private static InvalidConfigException throwEx(String errMsg, Class<?> leafClass, Class<?> nodeClass,
			Class<?> nodeFieldTypeClass, Type nodeFieldGenericType) {
		
		return new InvalidConfigException(
				errMsg + " " + nodeFieldGenericType + " for leafClass: " + leafClass + " nodeClass: " + nodeClass
						+ " nodeFieldClass: " + nodeFieldTypeClass + " nodeFieldGenericType: " + nodeFieldGenericType);
	}
	
}
