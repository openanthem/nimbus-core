package com.antheminc.oss.nimbus.test.scenarios.s10;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class S10_StackoverflowTest extends AbstractFrameworkIntegrationTests{
	  @Autowired
	    public ObjectMapper mapper;
	  
//	  @Autowired
//	    public ObjectWriter mapper2;
	
	@Test
	public void t00_init() throws Exception {
		
		
		
		Object controllerResp_new = controller.handleGet(MockHttpRequestBuilder.withUri("/client/org/app/p/s10_view")
				.addAction(Action._new).getMock(), null);
		
		mapper.writeValueAsString(controllerResp_new);
		
//		mapper2.writeValue(generator, controllerResp_new);
		
		
		
//		controllerResp_new  s10_view
//		Assert.assertNotNull(ExtractResponseOutputUtils.extractOutput(controllerResp_new));
	}
}
