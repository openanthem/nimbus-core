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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>Uses Univocity to convert excel files to their .csv equivalent and stores
 * that file in the local filesystem's temporary storage.
 * 
 * @author Tony Lopez
 * @author Sandeep Mantha
 */
@RequiredArgsConstructor
@Getter
public class UnivocityExcelToCSVConverter implements ExcelToCsvConverter {

	private final CsvWriterSettings csvWriterSettings;
	
	@Override
	public List<File> convert(InputStream inputStream, ExcelParserSettings settings) throws IOException {
		try (Workbook workbook = WorkbookFactory.create(inputStream)) {
			return convert(workbook, settings);
		} catch (EncryptedDocumentException | InvalidFormatException e) {
			throw new FrameworkRuntimeException(
					"Failed to create an Excel workbook object from the provided input stream.", e);
		}
	}

	private List<File> convert(Workbook workbook, ExcelParserSettings excelParserSettings) throws IOException {
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		DataFormatter formatter = new DataFormatter(true);

		int[] sheetNumbers = excelParserSettings.getSheetNumbersToParse();
		if (excelParserSettings.isParseFirstSheetOnly()) {
			sheetNumbers = new int[] { 0 };
		} else if (excelParserSettings.isParseAllSheets()) {
			sheetNumbers = ArrayUtils.toPrimitive(
					IntStream.rangeClosed(0, workbook.getNumberOfSheets() - 1).boxed().toArray(Integer[]::new));
		}

		List<File> csvFiles = new ArrayList<>();
		for (int sheetNumber : sheetNumbers) {
			Sheet sheet = workbook.getSheetAt(sheetNumber);
			File csvFile = parseSheet(sheet, formatter, evaluator);
			csvFiles.add(csvFile);
		}
		return csvFiles;
	}

	private File parseSheet(Sheet sheet, DataFormatter formatter, FormulaEvaluator evaluator) throws IOException {
		Iterator<Row> rowIterator = sheet.iterator();

		File csvFile = File.createTempFile(RandomStringUtils.randomAlphanumeric(8), ".csv");

		CsvWriter writer = new CsvWriter(csvFile, getCsvWriterSettings());
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			List<String> rowCsv = convertRowToCSV(row, formatter, evaluator);
			writer.writeRow(rowCsv);
		}
		writer.close();

		return csvFile;
	}

	private List<String> convertRowToCSV(Row row, DataFormatter formatter, FormulaEvaluator evaluator) {
		Cell cell = null;
		int lastCellNum = 0;
		List<String> csvLine = new ArrayList<String>();
		if (row != null) {
			lastCellNum = row.getLastCellNum();
			for (int i = 0; i <= lastCellNum; i++) {
				cell = row.getCell(i);
				if (cell == null) {
					csvLine.add("");
				} else {
					if (cell.getCellTypeEnum() != CellType.FORMULA) {
						csvLine.add(formatter.formatCellValue(cell));
					} else {
						csvLine.add(formatter.formatCellValue(cell, evaluator));
					}
				}
			}
		}
		return csvLine;
	}
}
