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
package com.antheminc.oss.nimbus.domain.cmd.exec;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.util.Assert;

import com.antheminc.oss.nimbus.UniqueIdGenerationUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * @author Soham Chakravarti
 *
 */
@Data
public class ExecuteError implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String message;
	
	private String label;
	
	private String uniqueId;
	
	public static final String CODE_SUFFIX = ".message";
	
	
	public ExecuteError() {
		
	}
	
	public ExecuteError(Class<? extends Throwable> exClass, String message) {
		Assert.notNull(exClass, "Exception class must not be null");
		setCode(exClass.getName() + CODE_SUFFIX);
		
		setUniqueId(UniqueIdGenerationUtil.generateUniqueId());
		
		setMessage(message);
	}

	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") 
	private LocalDateTime dateTime;
	
}
