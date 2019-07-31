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

/**
 * <p>A base interface for processing rows of a data file.
 * 
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
public interface RowProcessable {

	/**
	 * <p>A strategy interface for handling row processing.
	 * 
	 * @author Sandeep Mantha
	 * @author Tony Lopez
	 *
	 */
	@FunctionalInterface
	public static interface RowProcessingHandler<T> {
		void write(T bean);
	}

	/**
	 * <p>A strategy interface for handling row errors.
	 * 
	 * @author Sandeep Mantha
	 * @author Tony Lopez
	 *
	 */
	@FunctionalInterface
	public static interface RowErrorHandler {
		void handleError(RuntimeException exception, Object[] rowData);
	}

	/**
	 * <p>Execute a consumer that receives an argument containing the
	 * deserialized row data as a Java object.
	 * @param beanConsumer the consumer to execute
	 */
	void onRowProcess(RowProcessingHandler<?> writer);

	/**
	 * <p>Execute a consumer that executes error handling instructions. The
	 * provided arguments consist of: <ul><li>the thrown exception</li><li>the
	 * error data</li></ul>.
	 * @param onError the consumer to execute
	 */
	void onRowProcessError(RowErrorHandler onError);

	/**
	 * <p>Set whether or not the rows should be processed in parallel.
	 * @param parallel when {@code true}, processes the associated row data in
	 *            parallel
	 */
	void setParallel(boolean parallel);
}
