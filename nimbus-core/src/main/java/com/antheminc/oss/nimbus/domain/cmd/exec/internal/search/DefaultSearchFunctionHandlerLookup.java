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
package com.antheminc.oss.nimbus.domain.cmd.exec.internal.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.LookupSearchCriteria;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings("unchecked")
@EnableLoggingInterceptor
public class DefaultSearchFunctionHandlerLookup<T, R> extends DefaultSearchFunctionHandler<T, R> {

	protected static final String toReplace = "//.";
	protected static final String replaceWith = "//?.";
	private ExpressionEvaluator expressionEvaluator;
	
	public DefaultSearchFunctionHandlerLookup(BeanResolverStrategy beanResolver) {
		this.expressionEvaluator = beanResolver.find(ExpressionEvaluator.class);
	}
	
	@Override
	@Cacheable(value="staticcodevalues", 
		key = "#executionContext.getCommandMessage().getCommand().getFirstParameterValue(\"where\")", 
		condition= "T(org.apache.commons.lang3.StringUtils).equalsIgnoreCase(#executionContext.getCommandMessage().getCommand().getElementSafely(T(com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type).DomainAlias).getAlias(), \"staticCodeValue\")")
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		
		ModelConfig<?> mConfig = getRootDomainConfig(executionContext);
		
		List<?> searchResult = (List<?>)super.execute(executionContext, actionParameter);
				
		Command cmd = executionContext.getCommandMessage().getCommand();
		if(StringUtils.equalsIgnoreCase(cmd.getElementSafely(Type.DomainAlias).getAlias(), "staticCodeValue")) {
			return getStaticParamValues((List<StaticCodeValue>)searchResult, cmd);
		}
		LookupSearchCriteria lookupSearchCriteria = createSearchCriteria(executionContext, mConfig, actionParameter);
		return getDynamicParamValues(lookupSearchCriteria, mConfig.getReferredClass(), searchResult);
	}

	
	@Override
	protected LookupSearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig, Param<T> actionParameter) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		LookupSearchCriteria lookupSearchCriteria = new LookupSearchCriteria();
		lookupSearchCriteria.validate(executionContext);
			
		//String where = resolveNamedQueryIfApplicable(executionContext, mConfig, actionParameter);
		String where = executionContext.getCommandMessage().getCommand().getFirstParameterValue(Constants.SEARCH_REQ_WHERE_MARKER.code);
		lookupSearchCriteria.setWhere(where);
		
		lookupSearchCriteria.setOrderby(cmd.getFirstParameterValue(Constants.SEARCH_REQ_ORDERBY_MARKER.code));
		lookupSearchCriteria.setFetch(cmd.getFirstParameterValue(Constants.SEARCH_REQ_FETCH_MARKER.code));
		lookupSearchCriteria.setAggregateCriteria(cmd.getFirstParameterValue(Constants.SEARCH_REQ_AGGREGATE_MARKER.code));
		lookupSearchCriteria.setProjectCriteria(buildProjectCriteria(cmd));
		
		lookupSearchCriteria.setCmd(executionContext.getCommandMessage().getCommand());
		return lookupSearchCriteria;
	}
	
	private R getStaticParamValues(List<StaticCodeValue> searchResult, Command cmd) {	
		if(CollectionUtils.isEmpty(searchResult))
			return null;
		
		if(CollectionUtils.size(searchResult) > 1)
			throw new IllegalStateException("StaticCodeValue search for a command "+cmd+" returned more than one records for paramCode");
		
		return (R) searchResult.get(0).getParamValues();
	}
	
	private R getDynamicParamValues(LookupSearchCriteria lookupSearchCriteria, Class<?> criteriaClass, List<?> searchResult) {
		List<String> list = new ArrayList<String>(lookupSearchCriteria.getProjectCriteria().getMapsTo().values());

		if(list.size() > 2)
		throw new IllegalStateException("ParamValues lookup failed due to more than 2 fields provided to create the param values. the criteria class is "+criteriaClass);
		
		String cd = list.get(0).replaceAll(toReplace, replaceWith);
		String lb = list.get(1).replaceAll(toReplace, replaceWith);
		try {
			List<ParamValue> paramValues = new ArrayList<>();
			for(Object model: searchResult) {
				Object code = this.expressionEvaluator.getValue(cd, model);
				Object label = this.expressionEvaluator.getValue(lb, model);
				if(code!= null && label !=null) {
					paramValues.add(new ParamValue(code.toString(), label.toString()));
				}
			}
			return (R)paramValues;
		}
		catch(Exception ex) {
			throw new FrameworkRuntimeException("Failed to parse property - code: "+list.get(0)+" and label: "+list.get(1), ex);
		}
	}
	
}
