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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Soham Chakravarti
 * @author Tony Lopez
 *
 */
public class CommandMessageConverter {
	
	public static final String EMPTY_JSON_REGEX = "(^\\{\\s*\\}$)";
	
	private final ObjectMapper om;
	
	public CommandMessageConverter(BeanResolverStrategy beanResolver) {
		this.om = beanResolver.get(ObjectMapper.class);
	}
	
	/**
	 * <p>Uses the provided <tt>json</tt> to explicitly update the state of the provided param <tt>p</tt>. 
	 * Explicitly update means that only the values provided in the given <tt>json</tt> will be updated in 
	 * <tt>p</tt>'s state. Other state values will be preserved.</p>
	 * 
	 * <p>This process is as follows:</p>
	 * 
	 * <ul>
	 * <li>Retrieves the key/value pairs from the provided <tt>json</tt> <i>(where key is the field name and value is the field's value)</i>
	 * <li>Uses the keys to traverse <tt>p</tt> and set the state of each leaf param with the corresponding value</li>
	 * </ul>
	 * 
	 * <p>While traversing <tt>p</tt>, support is provided for nested params of the following:</p>
	 * 
	 * <ol>
	 * <li>Leaf param (primitive or literal type)</li>
	 * <li>Complex param (object type)</li>
	 * <li>Collection<tt>&lt;T&gt;</tt> param</li>
	 * <ol>
	 * <li><tt>T</tt> is a primitive or literal type</li>
	 * <li><tt>T</tt> is an object type</li>
	 * </ol>
	 * </ol>
	 * 
	 * 
	 * @param p the param to set
	 * @param json the json to read
	 */
	// TODO Add support for collection of collections
	public void update(Param<Object> p, String json) {
		// exit condition 1: p is a leaf -- can't traverse any further
		if (p.isLeaf()) {
			Object updated = read(p.getConfig(), json);
			p.setState(updated);
			
		// otherwise, p is nested -- now traverse and handle it's nested params
		} else {
			// iterate over the json string and essentially retrieve the key/value pairs 
			JsonNode tree = readToJsonNode(json);
			
			if (!tree.isArray()) {
				// traverse and update nested params with the provided json
				tree.fields().forEachRemaining(entry -> {
					Param<Object> pNested = p.findParamByPath(Constants.SEPARATOR_URI.code + entry.getKey());
					update(pNested, toJson(entry.getValue()));
				});
				
			} else {
				
				if (!p.isCollection()) {
					throw new FrameworkRuntimeException("Attempted to update " + p + " as a collection, but it is not a collection. " +
							"JSON was: " + json);
				}
				
				// exit condition 2: collection param is not instantiated -- can't traverse any further so
				// set the state to the object created from the provided json
				if (null == p.findIfCollection().getValues() || p.findIfCollection().getValues().isEmpty()) {
					Object collectionState = this.read(p.getConfig(),  json);
					p.setState(collectionState);
					return;
				}
				
				// traverse and update a collection's collection elements with the provided json 
				ArrayNode array = (ArrayNode) tree;
				Iterator<JsonNode> iterator = array.iterator();
				for(int index = 0; iterator.hasNext(); index++) {
					JsonNode entry = iterator.next();
					Param<Object> pNested = p.findParamByPath(Constants.SEPARATOR_URI.code + index);
					update(pNested, toJson(entry));
				}
			}
		}
		
	}
	
	public <T> T read(ParamConfig<?> pConfig, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			final Object model;
											
			if(pConfig.getType().isCollection())
				model = om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, pConfig.getType().findIfCollection().getElementConfig().getReferredClass()));
			
			else if(pConfig.getType().isArray())
				model = om.readValue(json, om.getTypeFactory().constructArrayType(pConfig.getReferredClass()));
			
			else {
				model = om.readValue(json, pConfig.getReferredClass());
				
			}
			
			return (T) model;
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of "+pConfig
					+"\n json:\n"+json, ex);
		}
	}
	
	private JsonNode readToJsonNode(String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			ObjectReader reader = om.readerFor(new TypeReference<Map<String, JsonNode>>() {});
			return reader.readTree(json);
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of JsonNode"
					+"\n json:\n" + json, ex);
		}
	}
	
	public <T> T read(Class<T> clazz, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			T model = om.readValue(json, clazz);
			return model;
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of "+clazz
					+"\n json:\n"+json, ex);
		}
	}
	
	public List readArray(Class<?> elemClazz, Class<? extends Collection> collClazz, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			List<?> model = om.readValue(json, om.getTypeFactory().constructCollectionType(collClazz, elemClazz));
			return model;
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON Array to instance of "+elemClazz+" collection "+collClazz 
					+"\n json:\n"+json, ex);
		}
	}
	
	public String toJson(Object model) {
		if(model==null) return null;
		
		try {
			String json = om.writeValueAsString(model);
			return json;
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from model to JSON, modelClass: "+ model.getClass()
					+ "\n modelInstance: "+model, ex);
		}
	}

}
