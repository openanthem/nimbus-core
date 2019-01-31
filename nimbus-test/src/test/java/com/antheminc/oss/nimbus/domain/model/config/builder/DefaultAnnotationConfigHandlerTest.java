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
package com.antheminc.oss.nimbus.domain.model.config.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Constraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.env.StandardEnvironment;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.config.builder.AnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.config.builder.DefaultAnnotationConfigHandler;
import com.antheminc.oss.nimbus.domain.config.builder.attributes.ConstraintAnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.config.builder.attributes.DefaultAnnotationAttributeHandler;
import com.antheminc.oss.nimbus.domain.model.config.AnnotationConfig;

/**
 * 
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class DefaultAnnotationConfigHandlerTest {

	@SuppressWarnings("unused")
	private final String foo = null;
	
	@NotNull(message = "bar can not be null")
	private final String bar = null;
	
	@NotNull(message = "baz can not be null")
	@Pattern(regexp="foo.*bar", message = "baz must conform to standards")
	private final String baz = null;
	
	@Size(min=3, max=5, message="Required number of selections not met")
	private final String sizeBaz = null;
	
	private BeanResolverStrategy beanResolver;
	
	private AnnotationConfigHandler annotationConfigHandler;
	
	@Before
	public void init() {
		this.beanResolver = Mockito.mock(BeanResolverStrategy.class);
		Map<Class<? extends Annotation>, AnnotationAttributeHandler> attributeHandlers = new HashMap<>();
		attributeHandlers.put(Constraint.class, new ConstraintAnnotationAttributeHandler(this.beanResolver));
		
		annotationConfigHandler =  new DefaultAnnotationConfigHandler(new DefaultAnnotationAttributeHandler(), attributeHandlers, new StandardEnvironment());
	}
	
	@Test
	public void t1_handle_annotationNotFound() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("foo");
		Assert.assertNull(annotationConfigHandler.handle(annotatedElement, Constraint.class));
	}
	
	@Test
	public void t2_handle_singleAnnotationConfig() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("bar");
		final List<AnnotationConfig> actual = annotationConfigHandler.handle(annotatedElement, Constraint.class);
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals("NotNull", actual.get(0).getName());
		Assert.assertEquals("bar can not be null", actual.get(0).getAttributes().get("message"));
		Assert.assertEquals(0, ((Object[]) actual.get(0).getAttributes().get("groups")).length);
		Assert.assertEquals(0, ((Object[]) actual.get(0).getAttributes().get("payload")).length);
	}
	
	@Test
	public void t3_handleSingle_annotationNotFound() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("foo");
		Assert.assertNull(annotationConfigHandler.handleSingle(annotatedElement, Constraint.class));
	}
	
	@Test(expected = InvalidConfigException.class)
	public void t4_handleSingle_multipleConfigsFound() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("baz");
		Assert.assertNull(annotationConfigHandler.handleSingle(annotatedElement, Constraint.class));
	}
	
	@Test
	public void t5_handleSingle_singleConfig() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("bar");
		final AnnotationConfig actual = annotationConfigHandler.handleSingle(annotatedElement, Constraint.class);
		Assert.assertEquals("NotNull", actual.getName());
		Assert.assertEquals("bar can not be null", actual.getAttributes().get("message"));
		Assert.assertEquals(0, ((Object[]) actual.getAttributes().get("groups")).length);
		Assert.assertEquals(0, ((Object[]) actual.getAttributes().get("payload")).length);
	}
	
	@Test
	public void t6_handle_multiAnnotationConfig() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("baz");
		final List<AnnotationConfig> actual = annotationConfigHandler.handle(annotatedElement, Constraint.class);
		Assert.assertEquals(2, actual.size());
		Assert.assertEquals("Pattern", actual.get(1).getName());
		Assert.assertEquals("baz must conform to standards", actual.get(1).getAttributes().get("message"));
		Assert.assertEquals("foo.*bar", actual.get(1).getAttributes().get("regexp"));
	}
	
	@Test
	public void t7_handleSize_singleConfig() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("sizeBaz");
		final AnnotationConfig actual = annotationConfigHandler.handleSingle(annotatedElement, Constraint.class);
		Assert.assertEquals("Size", actual.getName());
		Assert.assertEquals(3, actual.getAttributes().get("min"));
		Assert.assertEquals(5, actual.getAttributes().get("max"));
		Assert.assertEquals("Required number of selections not met", actual.getAttributes().get("message"));
	}
}
