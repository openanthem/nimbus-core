package com.anthem.oss.nimbus.core.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateDeserializer;
import com.anthem.nimbus.platform.spec.serializer.CustomLocalDateSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Configures classloader to load resources from custom locations
 * 
 * @author Rohit Bajaj
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	private static final String TARGET_FRONTEND = "file:./target/frontend/";
	private static final String CLASSPATH_STATIC = "classpath:./static/";
	public static final Map<String, String[]> RESOURCE_MAPPINGS = new HashMap<>();
	static {
		// The second argument of String[] is to tell where to look in a jar file.
		RESOURCE_MAPPINGS.put("/index.html", 			new String[] {"file:./target/frontend/index.html", CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/systemjs*", 			new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/scripts/**", 			new String[] {"file:./target/frontend/scripts/", "classpath:/static/scripts/"});
		RESOURCE_MAPPINGS.put("/styles/**", 			new String[] {"file:./target/frontend/styles/", "classpath:/static/styles/"});
		RESOURCE_MAPPINGS.put("/js/**",					new String[] {"file:./target/frontend/js/", "classpath:/static/js/"});
		RESOURCE_MAPPINGS.put("/node_modules/**", 		new String[] {"file:./target/frontend/node_modules/", "classpath:/static/node_modules/"});
		RESOURCE_MAPPINGS.put("/utils/**", 				new String[] {"file:./target/frontend/utils/", "classpath:/static/utils/"});
		RESOURCE_MAPPINGS.put("/webapp/**", 			new String[] {"file:./target/frontend/webapp/", "classpath:/static/webapp/"});
		RESOURCE_MAPPINGS.put("/resources/**", 			new String[] {CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/vendor**bundle*js", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/vendor**bundle*js", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/polyfills**bundle*js", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/inline**bundle*js", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/scripts**bundle*js", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/main**bundle*js", 		new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("//styles**bundle*css", 	new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/**ttf", 				new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/**ttf", 				new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/**woff",				new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/**woff2", 				new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/browser-sync*", 		new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
		RESOURCE_MAPPINGS.put("/updates*", 				new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
		registry.addViewController("/ui/").setViewName("forward:/index.html");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		for(Entry<String, String[]> entry : RESOURCE_MAPPINGS.entrySet()) {
			if (!registry.hasMappingForPattern(entry.getKey())) {
				registry.addResourceHandler(entry.getKey()).addResourceLocations(entry.getValue());
			}
		}
	}
	
	@Bean(name = "default.ObjectMapper")
	public ObjectMapper objectMapper() {
		ObjectMapper om = Jackson2ObjectMapperBuilder.json()
				.annotationIntrospector(new JacksonAnnotationIntrospector())
				.build();
	
		om.registerModule(new JavaTimeModule());
	    om.registerModule(this.buildDefaultModule());
		om.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return om;
	}
	
	/**
	 * Builds a default module for serializers and deserializers, bean serializer
	 * and deserializer modifiers, registration of subtypes and mix-ins
	 * as well as some other commonly needed objects.
	 * 
	 * This method can be overridden to provide additional defaults during instantiation.
	 * 
	 * @return the default <tt>Module</tt> object
	 */
	protected Module buildDefaultModule() {
		final SimpleModule defaultModule = new SimpleModule();
	    defaultModule.addSerializer(LocalDate.class, new CustomLocalDateSerializer());
	    defaultModule.addDeserializer(LocalDate.class, new CustomLocalDateDeserializer());
	    return defaultModule;
	}
}