/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.IsNull;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.anthem.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 * 
 */
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorConfigTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private DomainConfigBuilder domainConfigBuilder;
	
	@Autowired
	private MockMvc mvc;
	
	private MockHttpServletRequest createRequest(String domainPath, Action a) {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(domainPath)
				.addAction(a).getMock();
		
		return req;
	}

	@Test
	public void t00_json() throws Exception {
		 
		mvc.perform(post(createRequest(VIEW_PARAM_ROOT, Action._config).getRequestURI())
					.content("{}")
					.contentType(APPLICATION_JSON_UTF8))
	               	.andExpect(status().isOk())
	               	.andExpect(jsonPath("$.result.0.result.outputs[0].value", IsNull.notNullValue()))
	               	.andReturn()
	               	.getResponse()
	               	.getContentAsString()
	               ;
	}
	
	@Test
	public void t01_get_config_noRefId_domainRoot() {
		Object resp = controller.handlePost(createRequest(VIEW_PARAM_ROOT, Action._config), null);
		assertNotNull(resp);
		
		assertSame(domainConfigBuilder.getRootDomain(VIEW_DOMAIN_ALIAS), ExtractResponseOutputUtils.extractOutput(resp));
	}
	
//	@Test
//	public void t01_get_config_noRefId_domainRoot_action_get() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_update() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_replace() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_delete() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_nav() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_process() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t02_get_config_noRefId_domainRoot_action_search() {
//		MockHttpServletRequest getConfig_Req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
//				.addAction(Action._new).addBehavior(Behavior.$config).getMock();
//		
//		
//	}
//	
//	@Test
//	public void t01_get_config_noRefId_nestedDomain() {
//		
//	}
	
}
