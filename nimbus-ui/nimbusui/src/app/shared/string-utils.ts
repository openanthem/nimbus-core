/**
 * @license
 * Copyright 2016-2018 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
'use strict';

/**
 * \@author Tony.Lopez
 * \@whatItDoes Provides utility string methods
 */
export class StringUtils {

    /**
     * Converts the provided string to it's "kebab" case equivalent
     *  -> A Sample Value -> a-sample-value
     *  -> B_Sample_Value -> b-sample-value
     * @param value the value to convert
     */
    public static toKebabCase(value: string): string {
        return value
            .replace(/([a-z])([A-Z])/g, '$1-$2')
            .replace(/[\s_]+/g, '-')
            .toLowerCase();
    }
}