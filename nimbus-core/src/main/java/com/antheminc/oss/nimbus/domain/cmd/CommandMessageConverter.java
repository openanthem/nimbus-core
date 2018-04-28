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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

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
	
	public <T> T toReferredType(Param<?> param, String json) {
		return toReferredType(param.getConfig(), json);
	}
	
	public <T> T toReferredType(ParamConfig<?> pConfig, String json) {
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
	
	public JsonNode toJsonNodeTree(String json) {
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
	
	public <T> T toType(Class<T> clazz, String json) {
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
	
	public List<?> toCollectionFromArray(Class<?> elemClazz, Class<? extends Collection<?>> collClazz, String json) {
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
