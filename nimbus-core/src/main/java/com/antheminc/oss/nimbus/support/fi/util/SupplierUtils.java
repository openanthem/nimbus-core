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
package com.antheminc.oss.nimbus.support.fi.util;

import java.util.function.Supplier;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.support.fi.ThrowingSupplier;

/**
 * @author Tony Lopez
 *
 */
public class SupplierUtils {

	/**
	 * <p>Convenience method for handling suppliers that throw a <tt>FrameworkRuntimeException</tt>.
	 *  
	 * @param supplier the supplier to execute
	 * @param errMsg the error message to throw if an exception occurs
	 * @return the result of supplierupplier.get()
	 */
	public static <T> Supplier<T> handle(ThrowingSupplier<T, Exception> supplier, String errMsg) {
		return () -> {
		    try {
		    	return supplier.get();
		    } catch (Exception ex) {
		        throw new FrameworkRuntimeException(errMsg, ex);
		    }
		};
	}
}
