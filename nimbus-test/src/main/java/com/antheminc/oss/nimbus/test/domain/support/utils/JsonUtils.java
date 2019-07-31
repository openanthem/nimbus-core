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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author Soham Chakravarti
 *
 */
public final class JsonUtils {

	private ObjectMapper om;
	
	private JsonUtils() {
		this.om = Jackson2ObjectMapperBuilder.json()
					.annotationIntrospector(new JacksonAnnotationIntrospector())
					//.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
					//.autoDetectFields(true)
					//.autoDetectGettersSetters(true)
					//.modules(new JavaTimeModule())
					.build();
	}
	
	public static JsonUtils get() {
		return new JsonUtils();
	}

	public <T> T convert(Class<T> clazz, CommandMessage cmdMsg) {
		if(cmdMsg==null) return null;

		final String json = cmdMsg.getRawPayload();
		return convert(clazz, json);
	}
	
	public <T> T convert(Class<T> clazz, String json) {
		if(StringUtils.isEmpty(json)) return null;
		
		try {
			T model = om.readValue(json, clazz);
			return model;
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from JSON to instance of "+clazz
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
