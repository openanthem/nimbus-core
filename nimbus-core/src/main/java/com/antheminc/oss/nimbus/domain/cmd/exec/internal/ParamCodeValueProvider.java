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
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessage;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Input;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
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
@ConfigurationProperties(prefix="static.codevalue")
public class ParamCodeValueProvider implements HierarchyMatch, CommandExecutor<List<ParamValue>> {
	
	private static final String DEFAULT_KEY_ATTRIBUTE = "id";
	private static final String KEY_VALUE_SEPERATOR = "&";
	public static final String STATIC_CODE_VALUE = "staticCodeValue";
	
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
	 *		2.2 DB 
	 *
	 */
	@Override
	public Output<List<ParamValue>> execute(Input input) {
		CommandMessage cmdMsg = input.getContext().getCommandMessage();
		final List<ParamValue> codeValues;
		if(StringUtils.equalsIgnoreCase(cmdMsg.getCommand().getElementSafely(Type.DomainAlias).getAlias(), STATIC_CODE_VALUE)) {
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
		
		// 2.2 DB lookup
		String[] keyValuePayload = cmdMsg.getRawPayload().split(KEY_VALUE_SEPERATOR);
		cmdMsg.setRawPayload(null); // Clearing rawpayload as the payload for this command message is specific for key/value lookup and not supported by search executor
		List<AbstractEntity<?>> modelList = (List<AbstractEntity<?>>)searchExecutor.execute(input);
		
		if(CollectionUtils.isNotEmpty(modelList)) {
			String nestedDomainAlias = StringUtils.stripStart(cmdMsg.getCommand().getAbsoluteDomainAlias(), "/");
			
			String[] nestedDomainModels = nestedDomainAlias.split("/");
			Object currentObject = modelList;
			PropertyDescriptor currentPd = null;
			if(nestedDomainModels.length > 1) {
				for(int i=1; i<nestedDomainModels.length; i++) {
					try {
						if(i == 1) {
							currentPd = BeanUtils.getPropertyDescriptor(modelList.get(0).getClass(), nestedDomainModels[i]);
							currentObject = currentPd.getReadMethod().invoke(modelList.get(0));
						}
						else{
							currentPd = BeanUtils.getPropertyDescriptor(currentObject.getClass(), nestedDomainModels[i]);
							currentObject = currentPd.getReadMethod().invoke(currentObject);
						}
					}
					catch(Exception ex) {
						throw new FrameworkRuntimeException("Failed to execute read on property: "+currentPd, ex);
					}
				}
			}
			
			Collection<AbstractEntity<?>> coll = null;
			if(currentObject instanceof Collection<?>) {
				coll = (Collection<AbstractEntity<?>>) currentObject;
			}
			else{
				coll = new ArrayList<>();
				coll.add((AbstractEntity<?>)currentObject);
			}
			if (CollectionUtils.isEmpty(coll)) {
				List<ParamValue> paramValues = new ArrayList<>();
				return (R)paramValues;
			}
			Class<? extends AbstractEntity> codeValueClass = coll.iterator().next().getClass();
			
			
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(codeValueClass, DEFAULT_KEY_ATTRIBUTE);
			PropertyDescriptor pd1 = BeanUtils.getPropertyDescriptor(codeValueClass, keyValuePayload.length > 0?keyValuePayload[0]:DEFAULT_KEY_ATTRIBUTE);
			PropertyDescriptor pd2 = BeanUtils.getPropertyDescriptor(codeValueClass, keyValuePayload.length > 1?keyValuePayload[1]:keyValuePayload[0]);
			
			try {
				List<ParamValue> paramValues = new ArrayList<>();
				for(Object model: coll) {
					paramValues.add(new ParamValue(pd.getReadMethod().invoke(model), (String)pd1.getReadMethod().invoke(model), (String)pd2.getReadMethod().invoke(model)));
				}
				return (R)paramValues;
			}
			catch(Exception ex) {
				throw new FrameworkRuntimeException("Failed to execute read on property: "+pd, ex);
			}
		}
		return null;
	}

}
