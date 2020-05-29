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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecutor;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.HierarchyMatch;
import com.antheminc.oss.nimbus.entity.AbstractEntity;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@ConfigurationProperties(prefix="nimbus.static.codevalue")
public class ParamCodeValueProvider implements HierarchyMatch, CommandExecutor<List<ParamValue>> {
	
	private static final String DEFAULT_KEY_ATTRIBUTE = "id";
	private static final String KEY_VALUE_SEPERATOR = "&";
	
	DefaultActionExecutorSearch searchExecutor;
	
	public ParamCodeValueProvider(DefaultActionExecutorSearch searchExecutor) {
		this.searchExecutor = searchExecutor;
	}
	
	@Getter @Setter
	private Map<String,List<ParamValue>> values;

	
	/**
	 * Search will be in the order:
	 *	1. static code values (in below order):
	 *		1.1 config server, if not found
	 *		1.2 DB
	 *	2. Model as code values (in below order) 
 	 *		2.1 config server, if not found
	 *
	 */
	@Override
	public Output<List<ParamValue>> execute(Input input) {
		CommandMessage cmdMsg = input.getContext().getCommandMessage();
		final List<ParamValue> codeValues;
		if(StringUtils.equalsIgnoreCase(cmdMsg.getCommand().getElementSafely(Type.DomainAlias).getAlias(), Constants.PARAM_VALUES_DOMAIN_ALIAS.code)) {
			codeValues = getStaticCodeValue(input);
		}
		else{
			codeValues = getModelAsCodeValue(input);
		}
		
		return Output.instantiate(input, input.getContext(), codeValues);
	}
	
	
	@SuppressWarnings("unchecked")
	private List<ParamValue> getStaticCodeValue(Input input) {	
		CommandMessage cmdMsg = input.getContext().getCommandMessage();
		
		// 1.1 config server lookup
		if(MapUtils.isNotEmpty(values) && CollectionUtils.isNotEmpty(values.get(cmdMsg.getRawPayload()))){
			return values.get(cmdMsg.getRawPayload());
		}
		
		// 1.2 DB lookup
		cmdMsg.setRawPayload("{\"paramCode\":\""+cmdMsg.getRawPayload()+"\"}");
		List<StaticCodeValue> modelList = (List<StaticCodeValue>) searchExecutor.execute(input).getValue();
		if(CollectionUtils.isEmpty(modelList))
			return null;
		
		if(CollectionUtils.size(modelList) > 1)
			throw new IllegalStateException("StaticCodeValue look up for a command message"+cmdMsg+" returned more than one records for paramCode");
		
		return modelList.get(0).getParamValues();
	}
	
	@SuppressWarnings("unchecked")
	private <R> R getModelAsCodeValue(Input input) {	
		CommandMessage cmdMsg = input.getContext().getCommandMessage();
		
		// 2.1 config server lookup
		String domainAlias = cmdMsg.getCommand().getRootDomainAlias();
		if(MapUtils.isNotEmpty(values) && CollectionUtils.isNotEmpty(values.get(domainAlias))) {
			return (R)values.get(domainAlias);
		}
		
		throw new InvalidConfigException("Unsupported Operation.");
	}
}
