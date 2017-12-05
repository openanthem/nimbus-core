package com.antheminc.oss.nimbus.core.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import javax.validation.constraints.NotNull;

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
public class ConstraintAnnotationAttributeHandlerTest {

	@InjectMocks
	private ConstraintAnnotationAttributeHandler testee;
	
	@NotNull(message = "Enter a value for t1_str")
	private final String t1_str = null;
	
	@NotNull
	private final String t2_str = null;
	
	@Test
	public void t1_generateFrom_messageProvided() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("t1_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals("Enter a value for t1_str", actual.get("message"));
	}
	
	@Test
	public void t2_generateFrom_noMessageProvided() throws Exception {
		final Field annotatedElement = this.getClass().getDeclaredField("t2_str");
		final Annotation annotation = annotatedElement.getAnnotation(NotNull.class);
		final Map<String, Object> actual = this.testee.generateFrom(annotatedElement, annotation);
		Assert.assertEquals("", actual.get("message"));
	}
}
