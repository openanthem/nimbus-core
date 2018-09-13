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
package com.antheminc.oss.nimbus.test.domain.session;

import java.util.HashMap;
import java.util.Map;

import com.antheminc.oss.nimbus.domain.session.AbstractSessionProvider;

public class TestSessionProvider extends AbstractSessionProvider {

	private Map<String,Object> sessionMap = new HashMap<String, Object>();

	@Override
	public String getSessionId() {
		return "test-session";
	}

	@Override
	@SuppressWarnings("unchecked")
	public <R> R getAttribute(String key) {
		return (R)sessionMap.get(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		sessionMap.put(key, value);
	}	
	
	@Override
	public boolean removeAttribute(String key) {
		sessionMap.remove(key);
		return true;
	}

}
