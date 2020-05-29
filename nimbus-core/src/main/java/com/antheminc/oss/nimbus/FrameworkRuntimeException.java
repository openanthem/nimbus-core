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
package com.antheminc.oss.nimbus;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecuteError;

/**
 * @author Soham Chakravarti
 *
 */
public class FrameworkRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    final private ExecuteError execEx;
    

	public FrameworkRuntimeException() {
		this.execEx = create(null);
	}
	
    public FrameworkRuntimeException(String message) {
		super(message);
		this.execEx = create(message);
	}
	
    public FrameworkRuntimeException(Throwable cause) {
		super(cause);
		this.execEx = create(null);
	}
	
    public FrameworkRuntimeException(String message, Throwable cause) {
		super(message, cause);
		this.execEx = create(message);
	}
	
    private ExecuteError create(String msg) {
		return new ExecuteError(this.getClass(), msg);
	}

    @Override
	public String getMessage() {
		return constructMessage(super.getMessage());
	}
	
    @Override
	public String getLocalizedMessage() {
		return constructMessage(super.getLocalizedMessage());
	}
	
    private String constructMessage(String embedMsg) {
		return new StringBuilder().append("[Ex-UID:").append(execEx.getUniqueId()).append("] ").append(embedMsg).toString();
	}
	
    public ExecuteError getExecuteError() {
		return execEx;
	}
	
}
