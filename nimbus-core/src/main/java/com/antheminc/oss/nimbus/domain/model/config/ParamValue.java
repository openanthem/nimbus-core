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
package com.antheminc.oss.nimbus.domain.model.config;

import java.io.Serializable;

import com.antheminc.oss.nimbus.domain.defn.Model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
@EqualsAndHashCode
public class ParamValue implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Object code;
	
	private String label;
	
	private String desc;
	
	//private boolean isActive = true;
	
	public ParamValue() {}
	
	public ParamValue(Object code) {
		this(code, null, null);
	}
	
	public ParamValue(Object code, String label) {
		this(code, label, null);
	}

	public ParamValue(Object code, String label, String desc) {
		this.code = code;
        setLabel(label);
        setDesc(desc);
	}
}
