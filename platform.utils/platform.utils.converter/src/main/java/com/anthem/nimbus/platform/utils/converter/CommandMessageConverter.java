/**
 * 
 */
package com.anthem.nimbus.platform.utils.converter;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class CommandMessageConverter {
	
	private ObjectMapper om;
	
	public CommandMessageConverter() {
		this.om = Jackson2ObjectMapperBuilder.json()
					.annotationIntrospector(new JacksonAnnotationIntrospector())
					//.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
					//.autoDetectFields(true)
					//.autoDetectGettersSetters(true)
					//.modules(new JavaTimeModule())
					.build();
	}

	public <T> T convert(Class<T> clazz, CommandMessage cmdMsg) {
		if(cmdMsg==null) return null;

		final String json = cmdMsg.getRawPayload();
		return convert(clazz, json);
	}
	
	public <T> T convert(Class<T> clazz, String json) {

		
		if(StringUtils.isEmpty(json)) return null;
		
		try {
			om.registerModule(new JavaTimeModule());
			om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
			
			T model = om.readValue(json, clazz);
			
			return model;
		} catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to convert from JSON to instance of "+clazz
					+"\n json:\n"+json, ex);
		}
	}
	
	
	public String convert(Object model) {
		if(model==null) return null;
		
		try {
			String json = om.writeValueAsString(model);
			return json;
		} catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to convert from model to JSON, modelClass: "+ model.getClass()
					+ "\n modelInstance: "+model, ex);
		}
	}
}
 