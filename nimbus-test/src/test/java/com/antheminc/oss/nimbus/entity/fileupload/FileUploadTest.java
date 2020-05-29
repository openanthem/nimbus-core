package com.antheminc.oss.nimbus.entity.fileupload;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.entity.fileUpload.FileAttributes;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileUploadTest extends AbstractFrameworkIntegrationTests {

	
	//This test case will upload a test file to the GridFs collection and 
	//then download the file and match against the test file content.
	@Test
	public void testFileUploadDownload() throws IOException {
		// Create a temp file for testing
		String fileName = "gotham.txt";
		String fileContent = "Beware!! This is Batman's file";
		String fileType = "text/plain";
	
	MultipartFile file = new MockMultipartFile(fileName,
			fileName,
			fileType,
            fileContent.getBytes(StandardCharsets.UTF_8));
	
	String uploadUri = PLATFORM_ROOT+"/fileAttribute/upload";
	Holder<MultiOutput>  uploadResponseHolder =  (Holder<MultiOutput> ) controller.handleFileUpload(MockHttpRequestBuilder.withUri(uploadUri).getMock(), file);
	
	Param<?> uploadResponseParam =  (Param<?>) uploadResponseHolder.getState().findFirstParamOutputEndingWithPath("/fileAttribute").getValue();
	FileAttributes uploadResponse = (FileAttributes)uploadResponseParam.getState();
	Long fileInternalId = uploadResponse.getId();
	assertNotNull(uploadResponse.getId());
	assertNotNull(uploadResponse.getExternalId());
	
	

	
	// Now test file Download
	String downLoadUri = PLATFORM_ROOT+"/fileAttribute/download?id="+fileInternalId.toString();
	MockHttpServletResponse response = new MockHttpServletResponse();
	
	controller.handleFileDownload(response,MockHttpRequestBuilder.withUri(downLoadUri).addParam("id", fileInternalId.toString()).getMock() );
	assertEquals(response.getContentType(), fileType);
	assertEquals(response.getContentAsString(),fileContent);
	
	}
}
