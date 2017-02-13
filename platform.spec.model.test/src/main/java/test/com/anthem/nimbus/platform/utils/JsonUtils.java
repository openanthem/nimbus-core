/**
 * 
 */
package test.com.anthem.nimbus.platform.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.CommandMessage;
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
