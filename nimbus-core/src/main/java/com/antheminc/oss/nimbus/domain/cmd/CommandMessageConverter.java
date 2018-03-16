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
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	public Object convert(ParamConfig<?> pConfig, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			Object model = pConfig.getType().isCollection() 
								? om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, pConfig.getType().findIfCollection().getElementConfig().getReferredClass()))
										: pConfig.getType().isArray()
											? om.readValue(json, om.getTypeFactory().constructArrayType(pConfig.getReferredClass()))
													:om.readValue(json, pConfig.getReferredClass());
			return model;
			
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of "+pConfig
					+"\n json:\n"+json, ex);
		}
	}
	
	public <T> T convert(Class<T> clazz, String json) {
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
	
	public List convertArray(Class<?> elemClazz, Class<? extends Collection> collClazz, String json) {
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
	
	public String convert(Object model) {
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
 