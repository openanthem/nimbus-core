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
package com.antheminc.oss.nimbus.channel.web;

import org.apache.commons.collections.CollectionUtils;

import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.MultiExecuteOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Rakesh Patel
 *
 */
@RequiredArgsConstructor @Getter
public class HttpRawResponseBodyHeaderInterceptor implements ResponseInterceptor<MultiExecuteOutput> {

	@Override
	public boolean intercept(MultiExecuteOutput responseBody) {
		MultiOutput output = responseBody.getSingleResult();
		
		if(output != null) {
			if(CollectionUtils.isEmpty(output.getOutputs()))
				return false;
			
			if(output.getOutputs().size() > 1) // assumes the remote model call cannot have more than one outputs per request since it is meant for CRUD
				return false;
				
			Object obj = output.getSingleResult();
			if(obj instanceof Param) {
				Param<?> param = (Param<?>) obj;
				Object entityState = param.getLeafState();
				Output<Object> o = (Output<Object>)output.getOutputs().get(0); 
				o.setValue(entityState);
			}
		}
		return true;	
	}

}
