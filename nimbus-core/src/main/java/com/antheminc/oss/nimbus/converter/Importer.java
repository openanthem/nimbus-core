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
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;


/**
 * <p>A base importer interface for handling the import of data.
 * 
 * @author Sandeep Mantha
 * @author Tony Lopez
 *
 */
public interface Importer {

	public static enum ErrorHandling {
		/**
		 * <p>Re-throws any thrown data parsing exception when an error parsing
		 * row data occurs.
		 */
		SILENT,

		/**
		 * <p>Silently continues processing when an error parsing row data
		 * occurs.
		 */
		STRICT;
	}

	public static enum WriteStrategy {
		/**
		 * <p>Use Command DSL's _new implementation to write each record of row
		 * data processed.
		 */
		COMMAND_DSL,

		/**
		 * <p>Use the provided domain's {@link ModelRepository} implementation
		 * to write each record of row data processed.
		 */
		MODEL_REPOSITORY;
	}

	/**
	 * <p>Import data from the provided resource by converting each record of
	 * data into a Java object and then saving that object. The java object each
	 * record of data will be converted to is determined by the configuration in
	 * the provided {@code Command}.
	 * 
	 * @param command the command that will mandate the conversion rules for
	 *            converting {@code file} into to a Java object
	 * @param stream the object containing the data to import
	 */
	<T> void doImport(Command command, InputStream stream);

	/**
	 * <p>Tell whether or not this importer supports the given file type extension
	 * @param extension the file type extension to check
	 * @return {@code true} if supported, {@code false} otherwise
	 */
	public boolean supports(String extension);
}
