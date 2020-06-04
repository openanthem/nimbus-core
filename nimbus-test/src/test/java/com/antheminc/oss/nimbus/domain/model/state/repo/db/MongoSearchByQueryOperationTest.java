package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MongoSearchByQueryOperationTest extends AbstractFrameworkIngerationPersistableTests {
	
	protected static final String MSQ_ERR_DOMAIN_ALIAS = "testmongosearchbyqueryoperationfailmodel";
	protected static final String MSQ_ERR_PARAM_ROOT = PLATFORM_ROOT + "/" + MSQ_ERR_DOMAIN_ALIAS;	
	
	
	
	@Before
	public void load() {
		Covid19 covid1= new Covid19();
		covid1.setContinent("Asia");
		covid1.setId(1L);
		mongo.save(covid1);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void search_by_aggregation() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri("/anthem/thebox/p/testmongosearchbyqueryoperationfailmodel")
				.addAction(Action._new)
				.getMock();
	
	Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
	Long domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(holder);
	assertNotNull(domainRoot_refId);
	
	MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri("/anthem/thebox/p/testmongosearchbyqueryoperationfailmodel:1/action/_get")
			.addAction(Action._get)
			.getMock();
	
	holder = (Holder<MultiOutput>)controller.handlePost(request2, null);		
	
	Param<?> response = (Param<?>)holder.getState().getSingleResult();
	assertEquals(response.findStateByPath("/targetParameter"),"Assigned");
	}
	
}