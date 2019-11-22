/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.s0.view;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Tile;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ViewRoot;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.SampleLandingView.VSTestSearch;
import com.antheminc.oss.nimbus.test.scenarios.s0.view.SampleLandingView.VSTestSearchForm;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Sandeep Mantha
 *
 */
@Domain(value = "rootlandingview", includeListeners = { ListenerType.websocket })
@Repo(value = Repo.Database.rep_none, cache = Repo.Cache.rep_device)
@Getter @Setter
@ViewRoot(layout = "")
public class RootLandingView {

	@Page(defaultPage=true)
	@Config(url = "/p/rootlandingview/_process?fn=_unlock")
    private VPTest vpTest;

    @Model @Getter @Setter
    public static class VPTest  {

        @Tile
        private VTTest vtTest;
        
    }
    
    @Model @Getter @Setter
    public static class VTTest  {
    	
    	private String test;
    }
}


