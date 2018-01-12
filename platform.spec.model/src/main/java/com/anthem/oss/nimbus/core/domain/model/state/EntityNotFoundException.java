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
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class EntityNotFoundException extends FrameworkRuntimeException {
	
    private static final long serialVersionUID = 1L;
	
    private Class<?> clazz;
    

    public EntityNotFoundException(Class<?> clazz) {
    	super();
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Class<?> clazz) {
    	super(message);
        this.clazz = clazz;
    }
    
    public EntityNotFoundException(String message, Throwable cause, Class<?> clazz) {
    	super(message, cause);
        this.clazz = clazz;
    }

    public EntityNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Class<?> clazz) {
    	super(message, cause, enableSuppression, writableStackTrace);
        this.clazz = clazz;
    }
    
}
