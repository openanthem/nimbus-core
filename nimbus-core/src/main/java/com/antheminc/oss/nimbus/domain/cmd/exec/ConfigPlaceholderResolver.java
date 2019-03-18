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

import com.antheminc.oss.nimbus.domain.model.config.ExecutionConfig.Context;

/**
 * @author Tony Lopez
 * @since 1.3
 *
 */
public interface ConfigPlaceholderResolver {

	/**
	 * <p>Resolve any config placeholders that are present in the the provided
	 * path and return the resolved result.
	 * @param context the context object from which to retrieve config
	 *            placeholders
	 * @param pathToResolve the path to resolve against
	 * @return the resolved path
	 */
	String resolve(Context context, String pathToResolve);

}
