/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.chart.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.entity.aggregate.chart.DataGroup;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Domain(value="samplechartview", includeListeners= {ListenerType.websocket})
@Repo(value= Database.rep_none, cache=Cache.rep_device)
@Getter @Setter
public class VRSampleChart {
	
	/*
	 * Below configuration is only for test case purpose. When @Chart is used for view configuration in live applications, 
	 * please look at PetClinic implementaion for configuration options
	 */
	private List<DataGroup> actionPerDomainRoot;
	
	@Config(url="<!#this!>/../actionPerDomainRoot/_process?fn=_set&url=/p/samplechart/_search?fn=query&where={\n" + 
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
			"}")
	private String chartSearchParam;
	
}
