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
package com.anthem.oss.nimbus.core.session;

/**
 * Simple cache abstraction.
 * 
 * @author Tony Lopez (AF42192)
 *
 * @param <K> the key for the identifying the cache value.
 * @param <V> the cache value.
 */
public interface Cache<K, V> {

	/**
	 * Store a <tt>value</tt> in the underlying cache implementation under the corresponding <tt>id</tt>.
	 * 
	 * @param id unique identifier of the value to store.
	 * @param value the value to store.
	 */
	void put(K id, V value);
	
	/**
	 * Retrieve a value in the underlying cache implementation under the corresponding <tt>id</tt>.
	 * 
	 * @param id unique identifier of the value to retrieve.
	 * @return the value found, otherwise <code>null</code>.
	 */
	V get(K id);
	
	/**
	 * Attempt to delete a value in the underlying cache implementation under the corresponding <tt>id</tt>.
	 * 
	 * @param id unique identifier of the value to delete.
	 * @return <code>true</code> if successful, false otherwise.
	 */
	boolean remove(K id);
	
	/**
	 * Attempt to clear all values in the underlying cache implementation.
	 * 
	 * @return <code>true</code> if successful, false otherwise.
	 */
	boolean clear();
}
