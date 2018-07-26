/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.defaultconfigconditional;

import static org.assertj.core.api.Assertions.assertThat;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultConfigConditionalTest  extends AbstractFrameworkIntegrationTests {

	private static final String VIEW_ENTITY = "sampledefaultconfigconditionalview";
	private static final String VIEW_ENTITY_ROOT = PLATFORM_ROOT + "/" + VIEW_ENTITY;
	
	private static Long ENTITY_ID;
	
	public static enum Match {
		EQ,
		NOT_EQ;
	}
	
	
	@Test
	public void t00_createEntity() throws Exception {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addAction(Action._new)
				.getMock();
			
			Object controllerResp = controller.handleGet(httpReq, null);
			assertThat(controllerResp).isNotNull();
			
			Param<?> p = ExtractResponseOutputUtils.extractOutput(controllerResp);
			assertThat(p).isNotNull();
			
			ENTITY_ID = p.findStateByPath("/.m/id");
			assertThat(ENTITY_ID).isNotNull();
	}
	
	
	@Test
	public void t01_defaultConfig() throws Exception {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addRefId(ENTITY_ID)
				.addNested("/upateStatusWithDefaultConfig")
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		assertStatus("A", Match.EQ);
	}
	
	
	@Test
	public void t02_defaultConfigWithCondition_true() throws Exception {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addRefId(ENTITY_ID)
				.addNested("/upateStatusWithDefaultConfigConditional_true")
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		assertStatus("AB", Match.EQ);
	}
	
	@Test
	public void t03_defaultConfigWithCondition_false() throws Exception {
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addRefId(ENTITY_ID)
				.addNested("/upateStatusWithDefaultConfigConditional_false")
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		assertStatus("D", Match.NOT_EQ);
	}
	
	
	private void assertStatus(String value, Match match) {
		HttpServletRequest httpReq2 = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addRefId(ENTITY_ID)
				.addAction(Action._get)
				.getMock();
		
		Object controllerResp2 = controller.handleGet(httpReq2, null);
		assertThat(controllerResp2).isNotNull();
		
		Param<?> p = ExtractResponseOutputUtils.extractOutput(controllerResp2);
		assertThat(p).isNotNull();
		
		if(match == Match.EQ)
			assertThat(p.findParamByPath("/.d/.m/status").getState()).isEqualTo(value);
		else
			assertThat(p.findParamByPath("/.d/.m/status").getState()).isNotEqualTo(value);
	}
	
	
}
