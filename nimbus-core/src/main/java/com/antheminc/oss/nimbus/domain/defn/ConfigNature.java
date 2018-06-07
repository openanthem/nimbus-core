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
package com.antheminc.oss.nimbus.domain.defn;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;

/**
 * @author Soham Chakravarti
 *
 */
public interface ConfigNature {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value=ElementType.FIELD)
	@ConfigExtension
	public @interface Ignore {
		
		/**
		 * default is empty, which indicates that the field in class should not be constructed as parameter within framework but will <br>
		 * be included in pojo representation of the entity <br>
		 * <br>
		 * Providing a listener type would indicate only those event listeners to be excluded from listening to change events <br>
		 * For e.g., {@LinkPlain ListenerType.websocket} would prevent updates from param being sent to registered client (such as presentation)  
		 */
		ListenerType[] listeners() default { };
	}
	
}
