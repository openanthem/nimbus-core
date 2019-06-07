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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import java.util.function.Function;

/**
 * Utility class containing common path methods used within the framework.
 * 
 * @author Tony Lopez
 *
 */
public class PathUtils {

	/**
	 * <p>Iterates through the values of <tt>arr</tt> and uses each element to invoke <tt>finderFn</tt>. The Function <tt>finderFn</tt> is 
	 * a search function that expects a key <b>K</b> provided by <tt>arr</tt>.</p>
	 * 
	 * <p>The first non-null value retrieved as a result of <tt>finderFn</tt> will be returned. If <tt>arr</tt> is null or no value is found,
	 * <tt>null</tt> will be returned.</p>
	 * 
	 * @param arr the array of key objects
	 * @param finderFn the search function to evaluate
	 * @return the first found element as a result of <tt>finderFn</tt>, otherwise null 
	 */
	public static <T, K> T findFirstByPath(K[] arr, Function<K, T> finderFn) {
		if (null == arr) {
			return null;
		}
		for (final K key : arr) {
			final T found = finderFn.apply(key);
			if (null != found) {
				return found;
			}
		}
		return null;
	}
}
