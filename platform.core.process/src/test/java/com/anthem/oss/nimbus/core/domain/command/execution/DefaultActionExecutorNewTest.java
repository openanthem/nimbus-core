/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

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

import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;


/**
 * @author Soham Chakravarti
 *
 */
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorNewTest extends AbstractFrameworkIngerationPersistableTests {

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void t00_json_view() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT)
				.addAction(Action._new).getMock();
		
		 
		mvc.perform(post(req.getRequestURI())
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
	public void t01_json_core() throws Exception {
		MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_PARAM_ROOT)
				.addAction(Action._new).getMock();
		
		String json = mvc.perform(post(req.getRequestURI())
				.content("{}")
				.contentType(APPLICATION_JSON_UTF8))
               	.andExpect(status().isOk())
               	.andExpect(jsonPath("$.result.0.result.outputs[0].value", IsNull.notNullValue()))
               	.andReturn()
               	.getResponse()
               	.getContentAsString()
               ;
		
		System.out.println("@@@");
		System.out.println(json);
	}
}
