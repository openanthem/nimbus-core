package com.antheminc.oss.nimbus.core.domain.config.builder.attributes;

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
 * @author Tony Lopez (AF42192)
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
