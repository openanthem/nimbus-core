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
package com.antheminc.oss.nimbus.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * 
 * @author Tony Lopez
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class DefaultAnnotationAttributeHandlerTest {

	@InjectMocks
	private DefaultAnnotationAttributeHandler testee;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	@interface Foo {
		String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	@interface Bar {
		String value();
		String value2();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@Inherited
	@interface Baz {}

	@Foo(value = "FooAnnotation_value")
	@Bar(value = "BarAnnotation_value", value2 = "BarAnnotation_value2")
	@Baz
	private final String testVariable = "testVariable_value";
	
	@Test
	public void t1_generateFrom_singleAttribute() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("testVariable");
		final Annotation annotation = annotatedElement.getAnnotation(Foo.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(1, actual.size());
		Assert.assertEquals("FooAnnotation_value", actual.get("value"));
	}
	
	@Test
	public void t2_generateFrom_noAttributes() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("testVariable");
		final Annotation annotation = annotatedElement.getAnnotation(Baz.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertTrue(actual.isEmpty());
	}
	
	@Test
	public void t3_generateFrom_multipleAttributes() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("testVariable");
		final Annotation annotation = annotatedElement.getAnnotation(Bar.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals(2, actual.size());
		Assert.assertEquals("BarAnnotation_value", actual.get("value"));
		Assert.assertEquals("BarAnnotation_value2", actual.get("value2"));
	}
}
