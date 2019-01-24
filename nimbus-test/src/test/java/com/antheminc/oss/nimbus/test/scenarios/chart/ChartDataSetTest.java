/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.chart;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.entity.aggregate.chart.DataGroup;
import com.antheminc.oss.nimbus.entity.aggregate.chart.DataPoint;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.chart.core.SampleChart;

/**
 * @author Rakesh Patel
 *
 */
public class ChartDataSetTest  extends AbstractFrameworkIntegrationTests {

	
	@SuppressWarnings("unchecked")
	@Test
	public void t0_testChartAggregation() {
		mongo.dropCollection("samplechart");
		mongo.insert(new SampleChart(Action._new, "pet"), "samplechart");
		mongo.insert(new SampleChart(Action._search, "pet"), "samplechart");
		mongo.insert(new SampleChart(Action._new, "owner"), "samplechart");
		mongo.insert(new SampleChart(Action._search, "owner"), "samplechart");
		mongo.insert(new SampleChart(Action._new, "visit"), "samplechart");
		mongo.insert(new SampleChart(Action._new, "visit"), "samplechart");
		mongo.insert(new SampleChart(Action._new, "vet"), "samplechart");
		mongo.insert(new SampleChart(Action._new, "audit"), "samplechart");
		mongo.insert(new SampleChart(Action._search, "audit"), "samplechart");
		
		
		String request = "/samplechart/_search?fn=query&where={\n" + 
				"    aggregate: \"samplechart\",\n" + 
				"    pipeline: [\n" + 
				"	    {\n" + 
				"			$match: {\n" + 
				"                \"action\": {$in: [\"_new\",\"_search\"]}\n" + 
				"			}\n" + 
				"		},\n" + 
				"        {\n" + 
				"			$group: {\n" + 
				"                _id: {\"action\": \"$action\", \"root\":\"$domainRoot\"},\n" + 
				"			    count: {$sum: 1}\n" + 
				"			}\n" + 
				"		},\n" + 
				"        {\n" + 
				"			$group: {\n" + 
				"			    _id: \"$_id.action\",\n" + 
				"			    dataPoints: {$addToSet: {x: \"$_id.root\", y: \"$count\"}}\n" + 
				"			}\n" + 
				"		},\n" + 
				"        {\n" + 
				"			$project: {\n" + 
				"			    _class: {$literal:\"com.antheminc.oss.nimbus.entity.aggregate.chart.DataGroup\"},\n" + 
				"			    legend: {$concat:[\"Action \", \"$_id\"]},\n" + 
				"			    dataPoints:1,\n" + 
				"			    _id: 0\n" + 
				"			}\n" + 
				"		}\n" + 
				"\n" + 
				"	]\n" + 
				"}";
		
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+request)
				.addAction(Action._search)
				.getMock();
			
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		List<DataGroup> dataGrps = (List<DataGroup>) ExtractResponseOutputUtils.extractOutput(controllerResp);
		assertThat(dataGrps).isNotEmpty();
		
		assertThat(dataGrps).extracting("dataPoints").isNotEmpty();
		
		assertThat(dataGrps).flatExtracting(grp ->  {
						List<String> dataPoints = grp.getDataPoints().stream().map(dp -> grp.getLegend()+dp.getX()+dp.getY()).collect(Collectors.toList());
						return dataPoints;
					}
				)
				.contains("Action _newpet1", "Action _newowner1", "Action _newvisit2", "Action _searchpet1", "Action _searchaudit1");
	}		

	
	@SuppressWarnings("unchecked")
	@Test
	public void t1_testChartAggregationOnView() {
		mongo.dropCollection("samplechartaggregateview");
		
		DataGroup dg1 = new DataGroup();
		DataGroup dg2 = new DataGroup();
		dg1.setLegend("_search");
		dg2.setLegend("_new");
		
		List<DataPoint<?, ?>> dps1 = new ArrayList<>();
		DataPoint<String, Integer> dp1 = new DataPoint<>();
		dp1.setX("pet");
		dp1.setY(1);
		
		DataPoint<String, Integer> dp2 = new DataPoint<>();
		dp2.setX("owner");
		dp2.setY(2);
		
		dps1.add(dp1);
		dps1.add(dp2);
		dg1.setDataPoints(dps1);
		
		List<DataPoint<?, ?>> dps2 = new ArrayList<>();
		DataPoint<String, Integer> dp3 = new DataPoint<>();
		dp3.setX("visit");
		dp3.setY(1);
		
		DataPoint<String, Integer> dp4 = new DataPoint<>();
		dp4.setX("audit");
		dp4.setY(1);
		
		dps2.add(dp3);
		dps2.add(dp4);
		dg2.setDataPoints(dps2);
		
		mongo.insert(dg1, "samplechartaggregateview");
		mongo.insert(dg2, "samplechartaggregateview");
		mongo.updateMulti(new Query().addCriteria(Criteria.where("legend").in("_search","_new")), new Update().set("_class", "com.antheminc.oss.nimbus.entity.aggregate.chart.DataGroup"), "samplechartaggregateview");
		
		String request = "/samplechartaggregateview/_search?fn=query";
		
		HttpServletRequest httpReq = MockHttpRequestBuilder.withUri(PLATFORM_ROOT+request)
				.getMock();
			
		Object controllerResp = controller.handleGet(httpReq, null);
		assertThat(controllerResp).isNotNull();
		
		List<DataGroup> dataGrps = (List<DataGroup>) ExtractResponseOutputUtils.extractOutput(controllerResp);
		assertThat(dataGrps).isNotEmpty();
		
		assertThat(dataGrps).extracting("dataPoints").isNotEmpty();
		
		assertThat(dataGrps).flatExtracting(grp ->  {
						List<String> dataPoints = grp.getDataPoints().stream().map(dp -> grp.getLegend()+dp.getX()+dp.getY()).collect(Collectors.toList());
						return dataPoints;
					}
				)
				.contains("_newvisit1", "_newaudit1", "_searchpet1", "_searchowner2");
	}
	
}
