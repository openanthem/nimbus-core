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
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamValue;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.LookupSearchCriteria;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.ProjectCriteria;
import com.antheminc.oss.nimbus.entity.StaticCodeValue;
import com.antheminc.oss.nimbus.support.EnableLoggingInterceptor;
import com.antheminc.oss.nimbus.support.JustLogit;
import com.antheminc.oss.nimbus.support.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.support.pojo.CollectionsTemplate;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings("unchecked")
@EnableLoggingInterceptor
public class DefaultSearchFunctionHandlerLookup<T, R> extends DefaultSearchFunctionHandler<T, R> {

	private JustLogit logit = new JustLogit(this.getClass());
	
	protected static final String PROPERTY_DELIMITER = ".";
	protected static final String TO_REPLACE = "//"+PROPERTY_DELIMITER;
	protected static final String REPLACE_WITH = "//?"+PROPERTY_DELIMITER;
	
	private ExpressionEvaluator expressionEvaluator;
	
	@Value(value="${search.lookup.inMemory.sortThreshold:500}")
	private int inMemorySortThreshold;
	
	@Value(value="${search.lookup.inMemory.exceptionIfOverSortThreshold:false}")
	private boolean exceptionIfOverSortThreshold;
	
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
			return getStaticParamValues((List<StaticCodeValue>)searchResult, mConfig, cmd);
		}
		return getDynamicParamValues(mConfig, searchResult, cmd);
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
	
	private R getStaticParamValues(List<StaticCodeValue> searchResult, ModelConfig<?> mConfig, Command cmd) {	
		if(CollectionUtils.isEmpty(searchResult))
			return null;
		
		if(CollectionUtils.size(searchResult) > 1)
			throw new FrameworkRuntimeException("StaticCodeValue search for a command "+cmd+" returned more than one records, it must return only one record for the paramCode provided in the where clause");
		
		List<ParamValue> paramValues = searchResult.get(0).getParamValues();
		
		return sortIfApplicable(paramValues, mConfig, cmd);
				
	}

	private R getDynamicParamValues(ModelConfig<?> mConfig, List<?> searchResult, Command cmd) {
		ProjectCriteria projectCriteria = buildProjectCriteria(cmd);
		if(projectCriteria == null)
			throw new InvalidConfigException("DynamicParamValues lookup needs projection.mapTo to create the param values for command: "+cmd+". found none.");
		
		List<String> list = new ArrayList<String>(projectCriteria.getMapsTo().values());

		if(list.size() > 2)
			throw new InvalidConfigException("ParamValues lookup failed due to more than 2 fields provided in projection to create the param values for command: "+cmd);
		
		String cd = list.get(0).replaceAll(TO_REPLACE, REPLACE_WITH);
		String lb = list.get(1).replaceAll(TO_REPLACE, REPLACE_WITH);
		try {
			List<ParamValue> paramValues = new ArrayList<>();
			for(Object model: searchResult) {
				Object code = this.expressionEvaluator.getValue(cd, model);
				Object label = this.expressionEvaluator.getValue(lb, model);
				if(code!= null && label !=null) {
					paramValues.add(new ParamValue(code.toString(), label.toString()));
				}
			}
			return sortIfApplicable(paramValues, mConfig, cmd);
		}
		catch(Exception ex) {
			throw new FrameworkRuntimeException("Failed to parse property - code: "+list.get(0)+" and label: "+list.get(1)+" for command: "+cmd, ex);
		}
	}
	
	/**
	 * In memory sorting of the param values
	 */
	private R sortIfApplicable(List<ParamValue> paramValues, ModelConfig<?> mConfig, Command cmd) {
		String orderBy = cmd.getFirstParameterValue(Constants.SEARCH_REQ_ORDERBY_MARKER.code);
		
		if(StringUtils.isBlank(orderBy) || StringUtils.startsWith(orderBy, findRepoAlias(mConfig)+"."))
			return (R) paramValues;
		
		int index = StringUtils.lastIndexOf(orderBy, PROPERTY_DELIMITER);
		
		if(index == -1)
			throw new IllegalStateException("Invalid orderby clause for StaticCodeValue search for a command "+cmd+" . It must follow the pattern \"property.direction\" e.g. code.asc() or name.firstName.desc()");
		
		if(inMemorySortThreshold <= 0 || paramValues.size() > inMemorySortThreshold) {
			if(exceptionIfOverSortThreshold) {
				throw new FrameworkRuntimeException("Size of paramValues "+paramValues.size()+" is greater than configured limit for in memory sort: "+inMemorySortThreshold+", for command: "+cmd+". "
						+ "Please configure the limit as needed keeping in mind that above the threshold limit, you may encounter memory leak.");
			}
			else {
				logit.error(() -> "Size of paramValues "+paramValues.size()+" is greater than configured limit for in memory sort: "+inMemorySortThreshold+", for command: "+cmd+" so returning the list without sorting. "
						+ "Please configure the limit as needed keeping in mind that above the threshold limit, you may encounter memory leak." );
				return (R) paramValues;
			}
		}
		
		String property = StringUtils.substringBeforeLast(orderBy, PROPERTY_DELIMITER);
		String direction = StringUtils.substringAfterLast(orderBy, PROPERTY_DELIMITER);
		
		try {
			if(StringUtils.equalsAnyIgnoreCase(direction, Constants.SEARCH_REQ_ORDERBY_DESC_MARKER.code))
				CollectionsTemplate.sortSelfReverse(paramValues, Comparator.comparing(pv -> expressionEvaluator.getValue(property, pv, String.class)));
			else if(StringUtils.equalsAnyIgnoreCase(direction, Constants.SEARCH_REQ_ORDERBY_ASC_MARKER.code))
				CollectionsTemplate.sortSelf(paramValues, Comparator.comparing(pv -> expressionEvaluator.getValue(property, pv, String.class)));
			else
				throw new FrameworkRuntimeException("Valid sort direction(s) are "+Constants.SEARCH_REQ_ORDERBY_DESC_MARKER.code+" OR "+Constants.SEARCH_REQ_ORDERBY_ASC_MARKER.code+" but found: "+direction+" , in command: "+cmd); 
		}
		catch(Exception e) {
			throw new FrameworkRuntimeException("Could not sort the param values with provided orderBy clause: "+orderBy+" , in command: "+cmd, e);
		}
		
		return (R) paramValues;
	}
	
	
}
