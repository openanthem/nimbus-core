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
package com.antheminc.oss.nimbus.support.fi.util;

import java.util.Optional;
import java.util.function.Supplier;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.support.fi.ThrowingSupplier;

/**
 * @author Tony Lopez
 *
 */
public class SupplierUtils {

	public static final String REQUIRE_ERROR_MSG = "Field must not be null.";

	/**
	 * <p> Get the value given from the provided supplier or throw a
	 * {@link FrameworkRuntimeException}.
	 * @param s the supplier to retrieve
	 * @return the supplier value
	 */
	public static <T> T acquire(Supplier<T> s) {
		return acquire(s, REQUIRE_ERROR_MSG);
	}

	/**
	 * <p> Get the value given from the provided supplier or throw a
	 * {@link RuntimeException}.
	 * @param s the supplier to retrieve
	 * @param errorMsg the error message to use in the thrown exception
	 * @return the supplier value
	 */
	public static <T> T acquire(Supplier<T> s, RuntimeException e) {
		return Optional.ofNullable(s.get()).orElseThrow(() -> e);
	}

	/**
	 * <p> Get the value given from the provided supplier or throw a
	 * {@link FrameworkRuntimeException}.
	 * @param s the supplier to retrieve
	 * @param errorMsg the error message to use in the thrown exception
	 * @return the supplier value
	 */
	public static <T> T acquire(Supplier<T> s, String errorMsg) {
		return acquire(s, new FrameworkRuntimeException(errorMsg));
	}

	/**
	 * <p>Convenience method for handling suppliers that wraps any thrown
	 * exceptions into a <tt>FrameworkRuntimeException</tt>.
	 * 
	 * @param supplier the supplier to execute
	 * @param errMsg the error message to throw if an exception occurs
	 * @return the result of supplierupplier.get()
	 */
	public static <T> Supplier<T> handle(ThrowingSupplier<T, Exception> supplier, String errMsg) {
		return handle(supplier, errMsg, FrameworkRuntimeException.class);
	}

	/**
	 * <p>Convenience method for handling suppliers that wraps any thrown
	 * exceptions into an exception of type {@code exceptionClass}. <p>If for
	 * any reason {@code exceptionClass} fails to instantiate, a
	 * {@link FrameworkRuntimeException} will be thrown.
	 * 
	 * @param supplier the supplier to execute
	 * @param errMsg the error message to throw if an exception occurs
	 * @param exceptionClass the exception class to instantiate and throw.
	 * @return the result of supplierupplier.get()
	 */
	public static <T> Supplier<T> handle(ThrowingSupplier<T, Exception> supplier, String errMsg,
			Class<? extends RuntimeException> exceptionClass) {
		return () -> {
			try {
				return supplier.get();
			} catch (Exception cause) {
				RuntimeException e;
				try {
					e = exceptionClass.getConstructor(String.class, Throwable.class).newInstance(errMsg, cause);
				} catch (Exception configException) {
					throw new FrameworkRuntimeException(
							"Failed to create exception for " + exceptionClass.getSimpleName(), configException);
				}
				throw e;
			}
		};
	}

	private SupplierUtils() {
	}
}
