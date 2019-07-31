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
package com.antheminc.oss.nimbus.support.json;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;

/**
 * @Author Cheikh Niass on 12/2/16.
 */
public class JsonParsingException extends FrameworkRuntimeException {
	private static final long serialVersionUID = 1L;

    public JsonParsingException(Throwable cause) {
        super(cause);
    }
}
