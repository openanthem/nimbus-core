/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.test.scenarios.crossdomainresolver.view;

import java.time.LocalDate;
import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ConfigConditionals;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.crossdomainresolver.view.VRSampleCrossDomain.SampleNested;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleCoreEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
@Domain(value="sample_multi_domain") 
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter @ToString
public class VRSampleMultiDomain {
	
	@Label("This label is shown in {<!/p/sample_crossdomain_pathresolver/language!>}")
	private String attr1;
	
	@ConfigConditionals({
		@ConfigConditional(when="findStateByPath('/../attr1') == 'English'", config = { 
				@Config(url = "/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/language!> Breakfast\"")}),
		@ConfigConditional(when="findStateByPath('/../attr1') == 'French'", config = { 
				@Config(url = "/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/language!> Pastries\"")}),
	})
	private String attr2_conditional_action;
	
	@Config(url = "/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/language!> Breakfast\"")
	private String attr2_action;
	
	private String attr3;
	
	@Config(url="/nested_attr4/_replace?rawPayload=<!json(/p/sample_crossdomain_pathresolver/nested)!>")
	private String nested_attr4_action_complex;
	
	@Config(url="/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/nested/attr_1!>\"")
	private String nested_attr4_action;
	
	@Config(url="/nested_attr4_test/_replace?rawPayload=<!json(/../nested_attr4)!>")
	private String test;
	
	private SampleNested nested_attr4;
	
	private SampleNested nested_attr4_test;
	
	@Config(url="/coll_attr5/_replace?rawPayload=<!json(/p/sample_crossdomain_pathresolver/simple_coll)!>")
	private String coll_attr5_action_complex;
	
	@Config(url="/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/simple_coll/1!>\"")
	private String coll_attr5_action;
	
	private List<String> coll_attr5;
	
	@Config(url="/nested_coll_attr6/_replace?rawPayload=<!json(/p/sample_crossdomain_pathresolver/nested_coll)!>")
	private String nested_coll_attr6_action_complex;
	
	@Config(url="/attr3/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/nested_coll/1/attr_2!>\"")
	private String nested_coll_attr6_action;
	
	private List<SampleNested> nested_coll_attr6;
	
	@Config(url="/attr7/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/count!>\"")
	private String attr7_long_action;
	
	private Long attr7;
	
	@Config(url="/attr8/_replace?rawPayload=\"<!/p/sample_crossdomain_pathresolver/date_attr!>\"")
	private String attr8_date_action;
	
	private LocalDate attr8;
	
	@Config(url="/attr9_string/_replace?rawPayload=\"<!/p/sample_core:<!/../attr9_id!>/attr_String!>\"")
	private String attr9_crossdomain_refId_action;
	
	@Config(url="/attr9/_replace?rawPayload=<!json(/p/sample_core:<!/../attr9_id!>)!>")
	private String attr9_crossdomain_with_refId_nested_action;
	
	private SampleCoreEntity attr9;
	
	private String attr9_string;
	
	private Long attr9_id;
	
}
