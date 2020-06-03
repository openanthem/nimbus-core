package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.ModelRepositoryFactory;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoSearchByQueryOperationTest extends AbstractFrameworkIngerationPersistableTests {
	
	@Autowired MongoSearchByQueryOperation mongoSearchByQueryOperation;
	
	@Autowired CommandMessageConverter converter;
	
	protected static final String MSQ_ERR_DOMAIN_ALIAS = "testmongosearchbyqueryoperationfailmodel";
	protected static final String MSQ_ERR_PARAM_ROOT = PLATFORM_ROOT + "/" + MSQ_ERR_DOMAIN_ALIAS;	
	
	@Test
	@SuppressWarnings("unchecked")
	public void search_by_aggregation() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(MSQ_CORE_PARAM_ROOT)
				.addAction(Action._new)
				.getMock();
	
	Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
	Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
	assertNotNull(domainRoot_refId);
	
	MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri(MSQ_CORE_PARAM_ROOT).addRefId(domainRoot_refId)
			.addAction(Action._get)
			.getMock();
	
	holder = (Holder<MultiOutput>)controller.handlePost(request2, null);		
	
	Param<?> response = (Param<?>)holder.getState().getSingleResult();
	assertEquals(response.findStateByPath("/targetParameter"),"Assigned");
	}
	
}