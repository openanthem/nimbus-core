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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

/**
 * <p>A simple registry implementation
 * 
 * @author Tony Lopez
 *
 */
public interface Registry<T> {

	/**
	 * <p>Register the provided value within the registry.
	 * 
	 * @param value the value to register
	 * @return {@code true} if the value was successfully registered
	 */
	boolean register(T value);
	
	/**
	 * <p>Determine wither or not the value exists within the registry.
	 * 
	 * @param value the value to check whether or not it is in the registry
	 * @return {@code true} if the value exists within the registry
	 */
	boolean exists(T value);
}
