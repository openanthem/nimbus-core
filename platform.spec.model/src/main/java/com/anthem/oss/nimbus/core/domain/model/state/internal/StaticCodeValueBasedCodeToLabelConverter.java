/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Rakesh Patel
 *
 */
public class StaticCodeValueBasedCodeToLabelConverter extends RepoBasedConverter {

	
	@SuppressWarnings("unchecked")
	@Override
	public String serialize(String input) {
		//TODO - need to make this generic
		Command cmd = CommandBuilder.withUri("Anthem/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramValues.any().code.eq('"+input+"')").getCommand();
		cmd.setAction(Action._search);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = gateway.execute(cmdMsg);
		
		List<ParamValue> paramValues = (List<ParamValue>)multiOp.getSingleResult();
		
		if(CollectionUtils.isEmpty(paramValues))
			return input;
		
		return paramValues.stream()
					.filter((pv) -> StringUtils.equalsIgnoreCase(input, (String)pv.getCode()))
					.map((pv)-> pv.getLabel())
					.findFirst()
					.orElse(input);
					
		
	}
	
	@Override
	public String deserialize(String input) {
		throw new UnsupportedOperationException("RepoBasedCodeToDescConverter.deserialize is not implemented since it is not needed.");
	}
	
}
