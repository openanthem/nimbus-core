/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s4;

import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S4_CommandRootEventTest extends AbstractFrameworkIntegrationTests {
	
	private static final String K_URI_CR = PLATFORM_ROOT + "/s4c_main";
	
	
	private Object createNew_VR() {
		return controller.handleGet(
					MockHttpRequestBuilder.withUri(K_URI_CR).addAction(Action._new).getMock(), 
					null);
	}

	
	@Test
	public void t01_openForm_new_unassigned() throws Exception {
		Object controllerResp_new = createNew_VR();
		//Param<?> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
		Long refId = ExtractResponseOutputUtils.extractDomainRootRefId(controllerResp_new);
		
		// get call
		Object controllerResp_get = controller.handleGet(
				MockHttpRequestBuilder.withUri(K_URI_CR).addRefId(refId).addNested("/events_output").addAction(Action._get).getMock(), null);
		
		assertNotNull(controllerResp_get);
	}
}
