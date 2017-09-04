/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.config.ParamConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandMessageConverter {
	
	public static final String EMPTY_JSON_REGEX = "(^\\{\\s*\\}$)";
	
	private ObjectMapper om;
	
	public CommandMessageConverter() {
		this.om = Jackson2ObjectMapperBuilder.json()
					.annotationIntrospector(new JacksonAnnotationIntrospector())
					//.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
					//.autoDetectFields(true)
					//.autoDetectGettersSetters(true)
					//.modules(new JavaTimeModule())
					.build();
		
		om.registerModule(new JavaTimeModule());
		om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

	}



	public Object convert(ParamConfig<?> pConfig, String json) {
		if(StringUtils.isEmpty(json) || Pattern.matches(EMPTY_JSON_REGEX, json)) 
			return null;
		
		try {
			Object model = pConfig.getType().isCollection() 
								? om.readValue(json, om.getTypeFactory().constructCollectionType(List.class, pConfig.getType().findIfCollection().getElementConfig().getReferredClass()))
										: om.readValue(json, pConfig.getReferredClass());
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
 