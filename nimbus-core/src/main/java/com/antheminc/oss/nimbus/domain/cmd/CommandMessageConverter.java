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
import java.util.Map.Entry;
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

/**
 * @author Soham Chakravarti
 *
 */
public class CommandMessageConverter {
	
	public static final String EMPTY_JSON_REGEX = "(^\\{\\s*\\}$)";
	
	private final ObjectMapper om;
	
	public CommandMessageConverter(BeanResolverStrategy beanResolver) {
		this.om = beanResolver.get(ObjectMapper.class);
	}
	
	public <T> T read(ParamConfig<?> pConfig, String json) {
		return read(pConfig, json, null);
	}
	
	/**
	 * <p>
	 * This method call effectively (1) iterates over the <tt>json</tt> string, (2) retrieves the 
	 * key/value pairs, and (3) then iterates over key/value pairs again to perform the setState logic.
	 * </p>
	 * 
	 * TODO Efficiency improvement: We should be able to avoid (3) by hooking this logic into Jackson directly.
	 * 
	 * @param p the param to set
	 * @param json the json to read
	 */
	public void readAndSet(Param<Object> p, String json) {
		// exit condition: set the leaf param and return
		if (p.isLeaf()) {
			Object updated = read(p.getConfig(), json);
			p.setState(updated);
			
		// otherwise, p is nested -- now traverse it's nested params
		} else {
			// iterate over the json string and essentially retrieve the key/value pairs 
			JsonNode tree = (JsonNode) read(p.getConfig(), json, p);
			
			Iterator<Entry<String, JsonNode>> iterator = tree.fields();
			while(iterator.hasNext()) {
				Entry<String, JsonNode> entry = iterator.next();
				try {
					// recursively traverse to set pNested state if it is a leaf param
					Param<Object> pNested = p.findParamByPath(Constants.SEPARATOR_URI.code + entry.getKey()); 
					readAndSet(pNested, om.writeValueAsString(entry.getValue()));
					
				} catch (Exception e) {
					throw new FrameworkRuntimeException("Failed to convert JsonNode " + entry.getValue() + " to JSON");
				}
				
			}

		}
		
	}
	
	private <T> T read(ParamConfig<?> pConfig, String json, Param<Object> param) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			final Object model;
											
			if(pConfig.getType().isCollection())
				model = om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, pConfig.getType().findIfCollection().getElementConfig().getReferredClass()));
			
			else if(pConfig.getType().isArray())
				model = om.readValue(json, om.getTypeFactory().constructArrayType(pConfig.getReferredClass()));
			
			else {
				
				if (null == param || pConfig.isLeaf()) {
					model = om.readValue(json, pConfig.getReferredClass());
					
				} else {
					// TODO instead of returning the JsonNode, iterate over the param's nested children
					// and set the values according to the provided json.
					ObjectReader reader = om.readerFor(new TypeReference<Map<String, JsonNode>>() {});
					model = reader.readTree(json);
					
				}
			}
			
			return (T)model;
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of "+pConfig
					+"\n json:\n"+json, ex);
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
	
	public String write(Object model) {
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
 