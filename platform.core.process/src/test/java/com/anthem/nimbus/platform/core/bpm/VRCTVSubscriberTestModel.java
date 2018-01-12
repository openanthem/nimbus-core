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
package com.anthem.nimbus.platform.core.bpm;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo;
import com.anthem.oss.nimbus.core.domain.definition.Repo;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Cache;
import com.anthem.oss.nimbus.core.domain.definition.Repo.Database;
import com.anthem.oss.nimbus.core.domain.definition.ViewConfig.ViewRoot;
import com.anthem.oss.nimbus.core.domain.definition.Domain.ListenerType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Domain(value = "ctvsubscriberviewmodel", includeListeners={ListenerType.websocket}, lifecycle="ctvsubscriberviewmodel")
@MapsTo.Type(CTVSubscriberTestModel.class)
@Repo(value=Database.rep_none, cache=Cache.rep_device)
@ViewRoot(layout = "caseoverviewlayout")
@Getter @Setter
public class VRCTVSubscriberTestModel {
	private String viewParameter;
	private String viewResultParameter;
}
