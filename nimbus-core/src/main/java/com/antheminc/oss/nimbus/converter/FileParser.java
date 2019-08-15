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
package com.antheminc.oss.nimbus.converter;

import java.io.InputStream;

import com.antheminc.oss.nimbus.domain.cmd.Command;

/**
 * <p>A common file parser object.
 * 
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
public interface FileParser {

	/**
	 * <p>Parse the given resource from the using the provided {@link Command} as a context.
	 * 
	 * @param stream The resource to parse
	 * @param context the {@link Command} to use as context.
	 */
	void parse(InputStream stream, Command context);
}
