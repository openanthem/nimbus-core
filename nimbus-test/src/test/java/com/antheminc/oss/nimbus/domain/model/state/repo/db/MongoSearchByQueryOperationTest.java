package com.antheminc.oss.nimbus.domain.model.state.repo.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

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
		CovidData covidData1 = new CovidData();
		covidData1.setCountry("india");
		covidData1.setCases(199613);
		covidData1.setContinent("Asia");
		covidData1.setId(1L);
		mongo.save(covidData1,"covid19");
		
		CovidData covidData2 = new CovidData();
		covidData2.setCountry("Indonesia");
		covidData2.setCases(27549);
		covidData2.setContinent("Asia");
		covidData2.setId(2L);
		mongo.save(covidData2,"covid19");
		
		CovidData covidData3 = new CovidData();
		covidData3.setCountry("Iran");
		covidData3.setCases(157562);
		covidData3.setContinent("Asia");
		covidData3.setId(3L);
		mongo.save(covidData3,"covid19");

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
		
		MockHttpServletRequest request2 = MockHttpRequestBuilder.withUri("/anthem/thebox/p/testmongosearchbyqueryoperationfailmodel:1/action2/_get")
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request2, null);	
		
		request2 = MockHttpRequestBuilder.withUri("/anthem/thebox/p/testmongosearchbyqueryoperationfailmodel:1/result/_get")
				.getMock();
		
		holder = (Holder<MultiOutput>)controller.handlePost(request2, null);	
		
		Param<?> response = (Param<?>)holder.getState().getSingleResult();
		List<CovidData> covidData = (List<CovidData>)response.getState();
	
		
		// Add valid assertion here to account for collated data
	
	}
	
}