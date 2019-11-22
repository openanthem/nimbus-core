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
import com.antheminc.oss.nimbus.domain.defn.Lock;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Page;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ViewRoot;
import com.antheminc.oss.nimbus.test.scenarios.s0.core.SampleLockableDomain;

import lombok.Getter;
import lombok.Setter;
/**
 * @author Sandeep Mantha
 *
 */
@Domain(value = "rootdomain", includeListeners = { ListenerType.websocket })
@Repo(value = Repo.Database.rep_none, cache = Repo.Cache.rep_device)
@Getter @Setter
@ViewRoot(layout = "")
@MapsTo.Type(SampleLockableDomain.class)
public class RootTestModel {

	@Path
	private String attr1;

	@Page(defaultPage = true)
	private VPPage1 vpPage1;

	@Page
	private VPPage2 vpPage2;

	@Model
	@Getter @Setter
	public static class VPPage1  {} 

	@Model
	@Getter @Setter
	public static class VPPage2  {} 
}