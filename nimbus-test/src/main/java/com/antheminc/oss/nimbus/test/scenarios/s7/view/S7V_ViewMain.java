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
package com.antheminc.oss.nimbus.test.scenarios.s7.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Type;
import com.antheminc.oss.nimbus.domain.defn.Model.Param.Values;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Radio;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.defn.extension.Contents.Labels;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditional.Condition;
import com.antheminc.oss.nimbus.domain.defn.extension.ValuesConditionals;
import com.antheminc.oss.nimbus.test.scenarios.s7.core.S7C_CoreMain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Swetha Vemuri
 *
 */
@Domain("s7v_main") @Type(S7C_CoreMain.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@Getter @Setter @ToString
public class S7V_ViewMain {
	
	@Path
	@ValuesConditionals({ @ValuesConditional(targetPath = "../v_attr_values_2", condition = {
			@Condition(when = "state == 'test_0'", then = @Values(url = "/p/s7c_main/_search?fn=lookup&where=s7c_main.attr1_clone.eq('test_0')&projection.mapsTo=code:attr1,label:attr1")),
			@Condition(when = "state != null && state != 'test_0'", then = @Values(url = "/p/s7c_main/_search?fn=lookup&where=s7c_main.attr1_clone.eq('<!/.d/attr1!>')&projection.mapsTo=code:attr1,label:attr1")) }) })
	private String attr1;
	
	private String v_attr_values_2;
	
	@Radio
	@Values(url="/p/s7c_main/_search?fn=lookup&where=s7c_main.attr1_clone.eq('<!/.d/.m/attr1_clone!>')&projection.mapsTo=code:attr1_clone,label:attr1_clone")
	private String attr3;
	
	@Radio
	@Values(url="/p/s7c_corestatic/_search?fn=lookup&where=s7c_corestatic.staticAttr.eq('test_01')&projection.mapsTo=code:staticAttr,label:staticAttr")
	private String attr4;
	
}
