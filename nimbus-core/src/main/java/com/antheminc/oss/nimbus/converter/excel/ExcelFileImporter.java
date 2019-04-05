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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.converter.Importer;
import com.antheminc.oss.nimbus.converter.tabular.TabularDataFileImporter;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepository;
import com.antheminc.oss.nimbus.support.CommandUtils;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * <p>An excel file importer interface that imports data from an excel file into
 * the provided {@link ModelRepository}.
 * 
 * <p>This implementation first converts the excel file into a .csv equivalent
 * to make use of generic CSV parsing features.
 * 
 * @author Tony Lopez
 * @author Sandeep Mantha
 * @see com.antheminc.oss.nimbus.converter.excel.ExcelToCsvConverter
 * @see com.antheminc.oss.nimbus.converter.tabular.TabularDataFileImporter
 * 
 */
@RequiredArgsConstructor
@Getter
@Setter
public class ExcelFileImporter implements Importer {

	private final ExcelToCsvConverter toCsvConverter;
	private final TabularDataFileImporter tabularDataFileImporter;

	public final static String[] SUPPORTED_EXTENSIONS = new String[] { "xlsx", "xls" };

	public final static JustLogit LOG = new JustLogit(ExcelFileImporter.class);

	@Override
	public <T> void doImport(Command command, InputStream stream) {
		List<File> csvFiles = null;
		try {
			try {
				csvFiles = getToCsvConverter().convert(stream, buildExcelParserSettings(command));
			} catch (IOException ioe) {
				throw new FrameworkRuntimeException("Failed to convert the provided input stream to csv.", ioe);
			}

			for (File csvFile : csvFiles) {
				try {
					getTabularDataFileImporter().doImport(command, new FileInputStream(csvFile));
				} catch (FileNotFoundException e) {
					throw new FrameworkRuntimeException(
							"Import for " + csvFile + " as excel file with command " + command + " failed.", e);
				}
			}
		} finally {
			if (null != csvFiles && !csvFiles.isEmpty()) {
				for (File csvFile : csvFiles) {
					try {
						boolean deleted = csvFile.delete();
						if (!deleted) {
							LOG.warn(() -> "Failed to delete file " + csvFile
									+ " during the import process. File scheduled for deletion.");
							csvFile.deleteOnExit();
						}
					} catch (Exception e) {
						LOG.error(() -> "Encountered exception when attempting to delete file " + csvFile
								+ " during the import process. Consider removing this file manually!", e);
					}
				}
			}
		}
	}

	private ExcelParserSettings buildExcelParserSettings(Command command) {
		ExcelParserSettings settings = new ExcelParserSettings();
		CommandUtils.copyRequestParams(settings, command);
		return settings;
	}

	@Override
	public boolean supports(String extension) {
		return ArrayUtils.contains(SUPPORTED_EXTENSIONS, extension);
	}

}
