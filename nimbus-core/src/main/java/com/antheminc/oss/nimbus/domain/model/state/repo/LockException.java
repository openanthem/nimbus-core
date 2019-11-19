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
package com.antheminc.oss.nimbus.domain.model.state.repo;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;

/**
 * @author Sandeep Mantha
 *
 */
public class LockException extends FrameworkRuntimeException{

private static final long serialVersionUID = 1L;
	
	public LockException() { }

    public LockException(String message) {
		super(message);
	}
    
    public LockException(Throwable cause) {
		super(cause);
	}

    public LockException(String message, Throwable cause) {
		super(message, cause);
	}

	public LockException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		
		super(message, cause, enableSuppression, writableStackTrace);
	}
}


