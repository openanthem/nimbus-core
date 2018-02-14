/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s2;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.JsonUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S2_ValidateColElemConfigTest extends AbstractFrameworkIntegrationTests {
	
	private static final String K_URI_VR = PLATFORM_ROOT + "/s2v_main";
	
	@Test
	public void t() {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(K_URI_VR)
			.addAction(Action._new)
			.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertNotNull(controllerResp);
		
		Object o = ExtractResponseOutputUtils.extractOutput(controllerResp);
		assertNotNull(o);
		
		System.out.println(JsonUtils.get().convert(o));
	}
}
