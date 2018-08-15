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
package com.antheminc.oss.nimbus;

/**
 * @author Tony Lopez
 *
 */
public class JsonConversionException extends FrameworkRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JsonConversionException() {
		super();
	}
	
    public JsonConversionException(String message) {
		super(message);
	}
	
    public JsonConversionException(Throwable cause) {
		super(cause);
	}
	
    public JsonConversionException(String message, Throwable cause) {
		super(message, cause);
	}
}
