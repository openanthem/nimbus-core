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
package com.antheminc.oss.nimbus.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;

/**
 * 
 * @author Tony Lopez
 * @author Sandeep Mantha - added test cases for date validator
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConstraintAnnotationAttributeHandlerTest {

	private ConstraintAnnotationAttributeHandler testee;
	
	private BeanResolverStrategy beanResolver;
	
	private PropertyResolver propertyResolver;
	
	private Environment env;
	
	@NotNull(message = "Enter a value for t1_str")
	private final String t1_str = null;
	
	@NotNull
	private final String t2_str = null;
	
	@NotNull(message = "${spel.validation.message}")
	private final String t3_str = null;
    
    @Before
    public void setUp() {
    	this.propertyResolver = Mockito.mock(PropertyResolver.class);
    	this.env = Mockito.mock(Environment.class);
        this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
        Mockito.when(this.beanResolver.get(PropertyResolver.class)).thenReturn(this.propertyResolver);
        Mockito.when(this.beanResolver.get(Environment.class)).thenReturn(this.env);
    	this.testee = new ConstraintAnnotationAttributeHandler(this.beanResolver);
    }
    
	@Test
	public void testUseCustomMessage() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("t1_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals("Enter a value for t1_str", actual.get("message"));
	}
	
	/**
	 * This test case passes the SpEL expression back to the framework chain,
	 * where it will be resolved elsewhere.
	 */
	@Test
	public void testUseCustomMessageWithSpel() throws Exception {
		String expected = "${spel.validation.message}";
		final Field annotatedElement = this.getClass().getDeclaredField("t3_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(expected, actual.get("message"));
	}
	
	@Test
	public void testUseClientDefinedDefaultMessage() throws Exception {
		String expected = "Very Important Field!";
		final Field annotatedElement = this.getClass().getDeclaredField("t2_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		Mockito.when(this.propertyResolver.getProperty("javax.validation.constraints.NotNull.message")).thenReturn(expected);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(expected, actual.get("message"));
	}
	
	@Test
	public void testUseFrameworkDefinedDefaultMessage() throws Exception {
		String expected = "Field is required.";
		final Field annotatedElement = this.getClass().getDeclaredField("t2_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(expected, actual.get("message"));
	}
	
	@Test
	public void testUndefinedDefaultMessage() throws Exception {
		this.testee = new ConstraintAnnotationAttributeHandler(this.beanResolver, "blank");
		String expected = "{javax.validation.constraints.NotNull.message}";
		final Field annotatedElement = this.getClass().getDeclaredField("t2_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(expected, actual.get("message"));
	}
}
