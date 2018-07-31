/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.audit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AuditOnDetachedParamEditTest extends AbstractFrameworkIntegrationTests {

	private static final String VIEW_ENTITY = "sample_entity_view";
	private static final String VIEW_ENTITY_ROOT = PLATFORM_ROOT + "/" + VIEW_ENTITY;
	
	private static Long ENTITY_ID;
	
	@Test
	public void t00_new() throws Exception {
		SampleCoreEntity core = new SampleCoreEntity();
		core.setId(new Long(1L));
		core.setAudit_String("test");
		mongo.save(core, "sample_core");
		
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addAction(Action._new)
				.getMock();
			
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		Param<?> p = ExtractResponseOutputUtils.extractOutput(controllerResp);
		assertThat(p).isNotNull();
		
		ENTITY_ID = p.findStateByPath("/.m/id");
		assertThat(ENTITY_ID).isNotNull();
			
		
		HttpServletRequest httpReq2 = MockHttpRequestBuilder.withUri(VIEW_ENTITY_ROOT)
				.addRefId(ENTITY_ID)
				.addNested("/page_orange/vtOrange/vsSampleGrid1/sampleGrid1")
				.addAction(Action._get)
				.getMock();
			
		Object controllerResp2 = controller.handleGet(httpReq2, null);
		assertThat(controllerResp2).isNotNull();
		
		MultiOutput mOut = MultiOutput.class.cast(Holder.class.cast(controllerResp2).getState());
		
		Object state = p.findParamByPath("/page_orange/vtOrange/vsSampleGrid1/sampleGrid1").getLeafState();
		assertNotNull(state);
		
		p.findParamByPath("/page_orange/vtOrange/vsSampleGrid1/sampleGrid1/0/audit_String").setState("abc");
			
	}
	
}
