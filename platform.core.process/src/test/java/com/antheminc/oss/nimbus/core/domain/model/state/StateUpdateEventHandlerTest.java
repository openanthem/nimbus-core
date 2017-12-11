/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.core.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.platform.spec.model.dsl.binder.Holder;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreAssociatedEntity;

import test.com.antheminc.oss.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StateUpdateEventHandlerTest extends AbstractFrameworkIngerationPersistableTests {
 
	@SuppressWarnings("unchecked")
	@Test
	public void t03_WhenCoreEntityUpdated_ThenUpdateAssociatedEntity() {
		String refId = createOrGetDomainRoot_RefId();
		
		// Create sample_coreassociationentity with entityId = refId
		MockHttpServletRequest newReq = MockHttpRequestBuilder.withUri(CORE_ASSOCIATEDPARAM_ROOT)
				.addAction(Action._new)
				.addParam("fn", "_initEntity")
				.addParam("target", "entityId")
				.addParam("json", "\""+refId+"\"")
				.getMock();
		
		Object resp = controller.handleGet(newReq, null);
		assertNotNull(resp);
		final MultiOutput extractedResp = MultiOutput.class.cast(Holder.class.cast(resp).getState());
		assertNotNull(extractedResp);
		assertNotNull(extractedResp.getOutputs());
		
		// execute ceu_sampleassociatedentity/action_updateStatus to update the status of sample_coreassociationentity where entityId = refId
		MockHttpServletRequest newReq2 = MockHttpRequestBuilder.withUri(VIEW_CEU_PARAM_ROOT+"/action_updateStatus")
				.addAction(Action._get)
				.addParam("rawPayload", "\""+refId+"\"")
				.getMock();
		
		Object resp2 = controller.handleGet(newReq2, null);
		assertNotNull(resp2);
		final MultiOutput extractedResp2 = MultiOutput.class.cast(Holder.class.cast(resp2).getState());
		assertNotNull(extractedResp2);
		assertNotNull(extractedResp2.getOutputs());
		
		// validate the above execution indeed updated the status to Cancelled and persisted it in mongo
		MockHttpServletRequest newReq3 = MockHttpRequestBuilder.withUri(CORE_ASSOCIATEDPARAM_ROOT)
				.addAction(Action._search)
				.addParam("fn", "query")
				.addParam("where", CORE_ASSOCIATED_DOMAIN_ALIAS+".status.eq('Cancelled')")
				.getMock();
		
		Object resp3 = controller.handleGet(newReq3, null);
		assertNotNull(resp3);
		final MultiOutput extractedResp3 = MultiOutput.class.cast(Holder.class.cast(resp3).getState());
		assertNotNull(extractedResp3);
		assertNotNull(extractedResp3.getSingleResult());
		
		List<SampleCoreAssociatedEntity> associatedEntities = (List<SampleCoreAssociatedEntity>)extractedResp3.getSingleResult();
		assertNotNull(associatedEntities);
		
		associatedEntities.forEach(associatedEntity -> assertEquals("Cancelled", associatedEntity.getStatus()));
	}
}
