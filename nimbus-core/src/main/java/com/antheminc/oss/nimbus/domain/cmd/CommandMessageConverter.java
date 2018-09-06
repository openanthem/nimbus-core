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
package com.antheminc.oss.nimbus.domain.cmd;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.JsonConversionException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.fi.util.SupplierUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Soham Chakravarti
 * @author Tony Lopez
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class CommandMessageConverter {
	
	public static final String EMPTY_JSON_REGEX = "(^\\{\\s*\\}$)";
	
	private final ObjectMapper om;
	
	public CommandMessageConverter(BeanResolverStrategy beanResolver) {
		this.om = beanResolver.get(ObjectMapper.class);
	}
	
	/**
	 * <p>Given a JSON string that is of any JSON string format representing the provided type, 
	 * this method attempts to convert it to an Array instance of <tt>clazz</tt>.</p>
	 * 
	 * @param clazz the type to convert to
	 * @param json the JSON string to convert
	 * @return the converted object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public <T> T toArrayOfType(Class<T> clazz, String json) {
		return conversionTemplate(json, SupplierUtils.handle( 
				() -> getOm().readValue(json, getOm().getTypeFactory().constructArrayType(clazz)), 
				"Failed to convert from JSON to instance of Array with type "+clazz+"\n json:\n"+json,
				JsonConversionException.class));
	}
	
	/**
	 * <p>Given a JSON string that is of JSONArray string format, this method attempts 
	 * to convert it into a collection implementation of <tt>collectionClazz</tt> containing 
	 * <tt>elemClazz</tt> typed elements.</p>
	 * 
	 * @param elemClazz the expected element type of the converted collection's elements
	 * @param collectionClazz the expected collection implementation to convert to
	 * @param json the JSON string to convert
	 * @return the converted collection of elements
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public <S extends Collection<T>, T> S toCollectionFromArray(Class<T> elemClazz, Class<S> collectionClazz, String json) {
		return conversionTemplate(json, SupplierUtils.handle( 
				() -> getOm().readValue(json, getOm().getTypeFactory().constructCollectionType(collectionClazz, elemClazz)), 
				"Failed to convert from JSON Array to instance of "+elemClazz+" collection "+collectionClazz+"\n json:\n"+json,
				JsonConversionException.class));
	}
	
	/**
	 * <p>Given any object <tt>model</tt>, this method attempts to convert that object into 
	 * it's JSON string representation.</p>
	 * 
	 * @param model the object to convert
	 * @return the converted JSON string
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public String toJson(Object model) {
		if(model==null) 
			return null;
		
		return SupplierUtils.handle( 
				() -> getOm().writeValueAsString(model), 
				"Failed to convert from model to JSON, modelClass: "+ model.getClass()+ "\n modelInstance: "+model,
				JsonConversionException.class).get();
	}
	
	/**
	 * <p>Given a JSON string that is of any JSON string format, this method attempts to convert 
	 * it into to a <tt>Map<String, JsonNode></tt> object, where the map's keys represent the  
	 * JSON property names and the map's values are corresponding values in <tt>JsonNode</tt>
	 * format. This map is then packaged into a <tt>JsonNode</tt> for easy access.</p>
	 * 
	 * <p>This method is useful when needing to perform traversal of the given JSON string.</p>
	 * 
	 * @param json the JSON string to convert
	 * @return the converted object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public JsonNode toJsonNodeTree(String json) {
		return conversionTemplate(json, SupplierUtils.handle( 
				() -> getOm().readerFor(new TypeReference<Map<String, JsonNode>>() {}).readTree(json), 
				"Failed to convert from JSON to instance of JsonNode\n json:\n",
				JsonConversionException.class));
	}
	
	/**
	 * <p>Given a JSON string that is of any JSON string format representing the provided type, 
	 * this method attempts to convert it to an List instance of <tt>clazz</tt>.</p>
	 * 
	 * @param clazz the type to convert to
	 * @param json the JSON string to convert
	 * @return the converted list object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public <T> T toListOfType(Class<T> clazz, String json) {
		return conversionTemplate(json, SupplierUtils.handle( 
				() -> getOm().readValue(json, getOm().getTypeFactory().constructCollectionType(List.class, clazz)), 
				"Failed to convert from JSON to instance of List with type "+clazz+"\n json:\n"+json,
				JsonConversionException.class));
	}

	/**
	 * <p>Given a JSON string that is of any JSON string format representing the <i>referredClass</i> 
	 * of the provided <tt>param</tt>, this method attempts to convert it to the 
	 * <i>referredClass</i>.</p>
	 * 
	 * @param param the param containing the <i>referredClass</i> configuration to determine the 
	 * conversion type
	 * @param json the JSON string to convert
	 * @return the converted object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public <T> T toReferredType(Param<?> param, String json) {
		return toReferredType(param.getConfig(), json);
	}
	
	/**
	 * <p>Given a JSON string that is of any JSON string format representing the <i>referredClass</i> 
	 * of the provided <tt>pConfig</tt>, this method attempts to convert it to the 
	 * <i>referredClass</i>.</p>
	 * 
	 * @param pConfig the param config containing the <i>referredClass</i> configuration to determine 
	 * the conversion type
	 * @param json the JSON string to convert
	 * @return the converted object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	@SuppressWarnings("unchecked")
	public <T> T toReferredType(ParamConfig<?> pConfig, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
			
		if(pConfig.getType().isCollection())
			return (T) toListOfType(pConfig.getType().findIfCollection().getElementConfig().getReferredClass(), json);
		
		else if(pConfig.getType().isArray())
			return (T) toArrayOfType(pConfig.getReferredClass(), json);
		
		else {
			return (T) toType(pConfig.getReferredClass(), json);
			
		}
	}
	
	/**
	 * <p>Given a JSON string that is of any JSON string format representing the provided type, 
	 * this method attempts to convert it to an instance of <tt>clazz</tt>.</p>
	 * 
	 * @param clazz the type to convert to
	 * @param json the JSON string to convert
	 * @return the converted object
	 * @throws JsonConversionException if any failure is encountered during the conversion
	 */
	public <T> T toType(Class<T> clazz, String json) {
		return conversionTemplate(json, SupplierUtils.handle( 
				() -> getOm().readValue(json, clazz), 
				"Failed to convert from JSON to instance of "+clazz+"\n json:\n"+json,
				JsonConversionException.class));
	}
	
	/**
	 * <p>Convenience template method for handling null check and returning a result</p> 
	 * @param json the JSON string to convert
	 * @param supplier the supplier to execute
	 * @return the supplier execution result
	 */
	private <T> T conversionTemplate(String json, Supplier<T> supplier) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		return supplier.get();
	}
}
