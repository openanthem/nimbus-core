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
package com.antheminc.oss.nimbus.converter.excel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * <p>A simple conversion contract for excel to csv file converters.
 * 
 * @author Tony Lopez
 * @author Sandeep Mantha
 *
 */
public interface ExcelToCsvConverter {

	/**
	 * <p>Convert an excel input stream to it's .csv equivalent using any
	 * configured {@code settings} provided.
	 * @param inputStream the excel input stream to convert
	 * @param settings parsing configuration settings to apply
	 * @return the converted .csv {@link File}
	 * @throws IOException
	 */
	List<File> convert(InputStream inputStream, ExcelParserSettings settings) throws IOException;
}
