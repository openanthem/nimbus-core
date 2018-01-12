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
package test.com.anthem.nimbus.platform.utils;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;

/**
 * @author Swetha Vemuri
 *
 */
public class ExtractResponseOutputUtils {

	public static String extractDomainRootRefId(Object controllerResp) {
		return MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getRootDomainId();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp) {
		return (T)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getValue();
	}
	
	public static <T> T extractOutput(Object controllerResp, int j) {
		return extractOutput(controllerResp, 0, j);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp, int i, int j) {
		return (T)((MultiOutput)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(i)).getOutputs().get(j).getValue();
	}
}
