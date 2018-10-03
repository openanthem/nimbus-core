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
package com.antheminc.oss.nimbus.test.domain.mock;

import com.antheminc.oss.nimbus.domain.model.config.ParamConfigType;
import com.antheminc.oss.nimbus.domain.model.state.StateType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Tony Lopez
 *
 */
@Getter @Setter @ToString
public class MockStateType extends StateType {

	private static final long serialVersionUID = 1L;

	private String name;
	private boolean collection;
	private boolean nested;
	private boolean array;
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private boolean isTransient;
	
	public MockStateType(ParamConfigType config) {
		super(config);
	}
	
	@Override
	public boolean isTransient() {
		return this.isTransient;
	}
	
	public void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}
}
