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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.nav;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
//default._nav$execute?fn=default
public class PageIdEchoNavHandler<T> implements NavigationHandler<T> {

	@Override
	public String execute(ExecutionContext executionContext, Param<T> actionParameter) {
		return executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.KEY_NAV_ARG_PAGE_ID.code);
	}

	
}
