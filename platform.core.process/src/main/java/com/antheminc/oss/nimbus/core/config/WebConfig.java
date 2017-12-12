package com.antheminc.oss.nimbus.core.config;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.antheminc.oss.nimbus.platform.spec.serializer.CustomLocalDateDeserializer;
import com.antheminc.oss.nimbus.platform.spec.serializer.CustomLocalDateSerializer;

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
		RESOURCE_MAPPINGS.put("/jslibs/**", 			new String[] {TARGET_FRONTEND, CLASSPATH_STATIC});

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
	
	/**
	 * 
	 * Adds Custom LocalDate serializer and deserializer during spring bean initialization
	 * With this, every LocalDate field will be serialized and deserialized in the form MM/dd/yyyy
	 * No need of using @JsonSerializer and @JsonDeserializer
	 * 
	 */
	@Bean
    public Jackson2ObjectMapperBuilderCustomizer addCustomLocalDateSerializerDeserializer() {
        return new Jackson2ObjectMapperBuilderCustomizer() {

            @Override
            public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
                jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new CustomLocalDateDeserializer());
                jacksonObjectMapperBuilder.serializerByType(LocalDate.class, new CustomLocalDateSerializer());
            }

        };
    }
}