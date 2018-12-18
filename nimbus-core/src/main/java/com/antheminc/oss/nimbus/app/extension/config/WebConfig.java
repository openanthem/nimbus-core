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
package com.antheminc.oss.nimbus.app.extension.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.antheminc.oss.nimbus.channel.web.WebSessionIdLoggerInterceptor;
import com.antheminc.oss.nimbus.support.json.CustomDateDeserializer;
import com.antheminc.oss.nimbus.support.json.CustomDateSerializer;
import com.antheminc.oss.nimbus.support.json.CustomLocalDateDeserializer;
import com.antheminc.oss.nimbus.support.json.CustomLocalDateSerializer;
import com.antheminc.oss.nimbus.support.json.CustomLocalDateTimeDeserializer;
import com.antheminc.oss.nimbus.support.json.CustomLocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Configures classloader to load resources from custom locations
 * 
 * @author Rohit Bajaj
 * @author Tony Lopez
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	private static final String CLASSPATH_STATIC = "classpath:./static/";
	private static final String FORWARD = "forward:";

	private final String defaultView;
	private final String[] staticResourcePaths;

	public WebConfig() {
		this.staticResourcePaths = this.getDefaultStaticResourcePaths();
		this.defaultView = "/index.html";
	}

	/**
	 * <p>Adds Custom LocalDate serializer and deserializer during spring bean
	 * initialization With this, every LocalDate field will be serialized and
	 * deserialized in the form MM/dd/yyyy No need of using @JsonSerializer
	 * and @JsonDeserializer.
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer addCustomLocalDateSerializerDeserializer() {
		return new Jackson2ObjectMapperBuilderCustomizer() {

			@Override
			public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
				jacksonObjectMapperBuilder.serializationInclusion(Include.NON_NULL);
				jacksonObjectMapperBuilder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
				jacksonObjectMapperBuilder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

				jacksonObjectMapperBuilder.deserializerByType(LocalDate.class, new CustomLocalDateDeserializer());
				jacksonObjectMapperBuilder.serializerByType(LocalDate.class, new CustomLocalDateSerializer());
				jacksonObjectMapperBuilder.serializerByType(LocalDateTime.class, new CustomLocalDateTimeSerializer());
				jacksonObjectMapperBuilder.deserializerByType(LocalDateTime.class,
						new CustomLocalDateTimeDeserializer());
				jacksonObjectMapperBuilder.serializerByType(Date.class, new CustomDateSerializer());
				jacksonObjectMapperBuilder.deserializerByType(Date.class, new CustomDateDeserializer());
			}

		};
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new WebSessionIdLoggerInterceptor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		final String[] staticResourceLocation = new String[] { CLASSPATH_STATIC };
		for (String path : this.staticResourcePaths) {
			if (!registry.hasMappingForPattern(path)) {
				registry.addResourceHandler(path).addResourceLocations(staticResourceLocation);
			}
		}
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName(FORWARD + this.defaultView);
	}

	/**
	 * <p>Get the default list of resources that should be served as a static resource.
	 * @return an array containing a list of resource paths.
	 */
	private String[] getDefaultStaticResourcePaths() {
		return new String[] { "/index.html", "/vendor**bundle*js", "/polyfills**bundle*js", "/inline**bundle*js",
				"/scripts**bundle*js", "/main**bundle*js", "/styles**bundle*js", "/**ttf", "/**otf", "/**woff",
				"/**woff2" };
	}
}
