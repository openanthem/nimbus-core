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
package com.antheminc.oss.nimbus.test.domain.support;

import com.antheminc.oss.nimbus.domain.defn.Constants;

/**
 * @author Tony Lopez
 *
 */
public abstract class AbstractFrameworkTest {

	protected static final String CLIENT_ID = "hooli";
	protected static final Long TENANT_ID = 1L;
	protected static final String APP_ID = "thebox";
	protected static final String CMD_PREFIX = "/" + CLIENT_ID + "/" + TENANT_ID + "/" + APP_ID;
	protected static final String PLATFORM_ROOT = CMD_PREFIX + "/"
			+ Constants.MARKER_URI_PLATFORM.code;
}
