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
 * \@whatItDoes 
 * 
 * \@howToUse 
 * 
 */
export class URLUtils {

    /**
     * Extracts the domain from the current url.
     * @param url the URL to extract information from
     */
    public static getDomain(url: string): string {
        let urlParts = url.split('/');
        return urlParts[(urlParts.length - 2)];
    }

    /**
     * Extracts the page from the current url.
     * @param url the URL to extract information from
     */
    public static getPage(url: string): string {
        let urlParts = url.split('/');
        return urlParts[(urlParts.length - 1)];
    }

    /**
     * Extracts the domain and page in format "domain/page" from the current url.
     * @param url the URL to extract information from
     */
    public static getDomainPage(url: string): string {
        return `${URLUtils.getDomain(url)}/${URLUtils.getPage(url)}`;
    }
}