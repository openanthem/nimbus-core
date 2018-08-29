package com.antheminc.oss.nimbus.test.scenarios.s10;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class S10_StackoverflowTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	public ObjectMapper mapper;

	@Test
	public void t00_init() throws Exception {

		Writer jsonWriter = new StringWriter();
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.setCodec(mapper);
		JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonWriter);

		Object controllerResp_new = controller.handleGet(
				MockHttpRequestBuilder.withUri("/client/org/app/p/s10_view").addAction(Action._new).getMock(), null);

//       This also throws stackoverflow error.
//		 mapper.writeValueAsString(controllerResp_new);

		
		ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();
		objectWriter.writeValue(jsonGenerator, controllerResp_new);

	}
}
