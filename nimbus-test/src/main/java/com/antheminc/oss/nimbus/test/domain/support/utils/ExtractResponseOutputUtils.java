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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import java.util.Set;

import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.support.Holder;

/**
 * @author Swetha Vemuri
 *
 */
public class ExtractResponseOutputUtils {

	public static Long extractDomainRootRefId(Object controllerResp) {
		return MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getRootDomainId();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp) {
		return (T)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getValue();
	}

	public static Set<ParamEvent> extractAggregatedEvents(Object controllerResp) {
		return MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getAggregatedEvents();
	}

	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp, int i) {
		return (T)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(i).getValue();
	}

}
