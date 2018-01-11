package com.antheminc.oss.nimbus.core.domain.config.builder.attributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Map;

/**
 * <p>Base implementation for defining how annotation attributes and their values should be
 * parsed and mapped.</p>
 * 
 * <p>For example, to ensure that all values of a particular attribute in <tt>@Domain</tt> are
 * prefixed with some value (e.g. <i>prefix</i>), an implementation of 
 * <tt>AnnotationAttributeHandler</tt> (e.g. <tt>AnnotationAttributeHandlerImpl</tt>) could be 
 * defined to ensure the following scenario:</p>
 * 
 * <p>Executing AnnotationAttributeHandlerImpl.generateFrom(...) over an object annotated with <tt>@Domain</tt>:
 * <pre>
 * {@literal @}Domain(value = "home")
 * public class SomeElement {}
 * 
 * AnnotationAttributeHandlerImpl.generateFrom(...).get("value")
 *   <b>returns:</b> "prefix-home"
 * </pre>
 * 
 * <p>Implementing classes should ensure that the returned-value is non-null.</p>
 * 
 * @author Tony Lopez (AF42192)
 * @see com.antheminc.oss.nimbus.core.domain.config.builder.attributes.DefaultAnnotationAttributeHandler
 * 
 */
public interface AnnotationAttributeHandler {

	/**
	 * Generates a mapping of annotation attributes organized with the <tt>annotation</tt> 
	 * name as the map's <i>key</i> and the <tt>annotation</tt> attribute's values as the map's 
	 * <i>value</i>.
	 * 
	 * @param annotatedElement The element which is annotated with <tt>annotation</tt>
	 * @param annotation The <tt>Annotation</tt> from which to generate the key/value mappings for. 
	 * @return A mapping of <tt>annotation</t>>'s attribute names/values as the map's key/value, 
	 * respectively.
	 */
	Map<String, Object> generateFrom(AnnotatedElement annotatedElement, Annotation annotation);
}
