package com.antheminc.oss.nimbus.domain.config.builder;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

public class ExcludePackageScanTest extends AbstractFrameworkIntegrationTests  {
	
	@Rule
	public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void testShouldNotScanTheConfiguredBasePackage(){		
        MockHttpServletRequest request = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+"/sampleExcludeEntity").addAction(Action._new).getMock();									
		thrown.expect(InvalidConfigException.class);
		thrown.expectMessage("Domain model config not found for root-alias: sampleExcludeEntity");
		controller.handlePost(request, null);
	}	
}
