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
package com.antheminc.oss.nimbus.converter.csv;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.converter.Importer.ErrorHandling;
import com.antheminc.oss.nimbus.converter.Importer.WriteStrategy;
import com.antheminc.oss.nimbus.converter.tabular.TabularDataFileImporter;
import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.DefaultFileImportGateway;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.MyPojo;

/**
 * @author Tony Lopez
 *
 */
public class TabularDataFileImporterTest extends AbstractFrameworkIngerationPersistableTests {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Autowired
	private DefaultFileImportGateway fileImportGateway;
	
	@Autowired
	private TabularDataFileImporter tabularDataFileImporter;
	
	@Test
	public void testUploadCommandDSL() throws FileNotFoundException, IOException {
		uploadSampleCsv(WriteStrategy.COMMAND_DSL);
	}

	@Test
	public void testUploadModelRepository() throws FileNotFoundException, IOException {
		uploadSampleCsv(WriteStrategy.MODEL_REPOSITORY);
	}

	@Test
	public void testUploadOverrideOnError() throws FileNotFoundException, IOException {
		List<Object[]> failureData = new ArrayList<>();
		tabularDataFileImporter.setSilentErrorHandler((e, rowData) -> {
			failureData.add(rowData);
		});
		uploadMismatchedCsv(ErrorHandling.SILENT);
		Assert.assertEquals(1, failureData.size());
	}

	@Test
	public void testUploadSilentErrorHandler() throws FileNotFoundException, IOException {
		uploadMismatchedCsv(ErrorHandling.SILENT);
	}

	@Test
	public void testUploadStrictErrorHandler() throws FileNotFoundException, IOException {
		exception.expect(FrameworkRuntimeException.class);
		uploadMismatchedCsv(ErrorHandling.STRICT);
	}

	@SuppressWarnings("unchecked")
	private void uploadSampleCsv(WriteStrategy writeStrategy) throws FileNotFoundException, IOException {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(PLATFORM_ROOT).addNested("/event/upload")
				.addParam(TabularDataFileImporter.ARG_WRITE_STRATEGY, writeStrategy.toString()).getMock();
		MockMultipartFile csvFile = new MockMultipartFile("sample-upload-data.csv",
				new FileInputStream("src/test/resources/sample-upload-data.csv"));
		Assert.assertTrue(this.fileImportGateway.doImport(req, "mypojo", csvFile));

		MockHttpServletRequest getReq = MockHttpRequestBuilder.withUri(PLATFORM_ROOT).addNested("/mypojo")
				.addAction(Action._search).addParam("fn", "example").getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handleGet(getReq, null);
		List<MyPojo> actual = (List<MyPojo>) response.getState().getSingleResult();

		Assert.assertEquals(3, actual.size());
		Assert.assertEquals("A", actual.get(0).getMyColumn1());
		Assert.assertEquals(1, actual.get(0).getMyColumn2());
		Assert.assertEquals("B", actual.get(1).getMyColumn1());
		Assert.assertEquals(2, actual.get(1).getMyColumn2());
		Assert.assertNull(actual.get(2).getMyColumn1());
		Assert.assertEquals(3, actual.get(2).getMyColumn2());
	}

	@SuppressWarnings("unchecked")
	private void uploadMismatchedCsv(ErrorHandling errorHandling) throws FileNotFoundException, IOException {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(PLATFORM_ROOT).addNested("/event/upload")
				.addParam(TabularDataFileImporter.ARG_ERROR_HANDLING, errorHandling.toString()).getMock();
		MockMultipartFile csvFile = new MockMultipartFile("sample-upload-data-mismatch.csv",
				new FileInputStream("src/test/resources/sample-upload-data-mismatch.csv"));
		Assert.assertTrue(this.fileImportGateway.doImport(req, "mypojo", csvFile));

		MockHttpServletRequest getReq = MockHttpRequestBuilder.withUri(PLATFORM_ROOT).addNested("/mypojo")
				.addAction(Action._search).addParam("fn", "example").getMock();
		Holder<MultiOutput> response = (Holder<MultiOutput>) this.controller.handleGet(getReq, null);
		List<MyPojo> actual = (List<MyPojo>) response.getState().getSingleResult();

		Assert.assertEquals(2, actual.size());
		Assert.assertEquals("A", actual.get(0).getMyColumn1());
		Assert.assertEquals(1, actual.get(0).getMyColumn2());
		Assert.assertEquals("C", actual.get(1).getMyColumn1());
		Assert.assertEquals(3, actual.get(1).getMyColumn2());
	}
}
