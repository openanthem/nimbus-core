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
package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutorGateway;
import com.antheminc.oss.nimbus.domain.defn.Converters.ParamConverter;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@EnableLoggingInterceptor
@Getter(AccessLevel.PROTECTED)
public class StaticCodeValueBasedCodeToLabelConverter implements ParamConverter<String, String> {

	private CommandExecutorGateway gateway;
	
	public StaticCodeValueBasedCodeToLabelConverter(BeanResolverStrategy beanResolver) {
		this.gateway = beanResolver.find(CommandExecutorGateway.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String serialize(String input) {
		//TODO - need to make this generic
		Command cmd = CommandBuilder.withUri("Anthem/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramValues.any().code.eq('"+input+"')").getCommand();
		cmd.setAction(Action._search);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = getGateway().execute(cmdMsg);
		
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
