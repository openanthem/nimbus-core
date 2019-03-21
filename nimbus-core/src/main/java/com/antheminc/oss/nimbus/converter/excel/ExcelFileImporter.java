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

	public final static String[] SUPPORTED_EXTENSIONS = new String[] { "xlsx" , "xls"};	
	
	@Override
	public <T> void doImport(Command command, InputStream stream) {
		List<File> csvFiles = null;
		try {
			csvFiles = getToCsvConverter().convert(stream, buildExcelParserSettings(command));
			for(File csvFile: csvFiles) {
				getTabularDataFileImporter().doImport(command, new FileInputStream(csvFile));
				csvFile.delete();
			}
		} catch (IOException e) {
			throw new FrameworkRuntimeException(e);
		} finally {
			if (null != csvFiles && !csvFiles.isEmpty()) {
				csvFiles.forEach(File::delete);
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
