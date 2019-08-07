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
package com.antheminc.oss.nimbus.domain.config.builder;

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
 * @author Tony Lopez
 * @see com.antheminc.oss.nimbus.domain.config.builder.attributes.DefaultAnnotationAttributeHandler
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
