package com.anthem.oss.nimbus.core.domain.command.execution.search;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ParamValue;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.db.ModelRepository;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.LookupSearchCriteria;
import com.anthem.oss.nimbus.core.entity.SearchCriteria.ProjectCriteria;
import com.anthem.oss.nimbus.core.entity.StaticCodeValue;

/**
 * @author Rakesh Patel
 *
 */
@SuppressWarnings("unchecked")
public class DefaultSearchFunctionHandlerLookup<T, R> extends DefaultSearchFunctionHandler<T, R> {

	@Override
	public R execute(ExecutionContext executionContext, Param<T> actionParameter) {
		
		ModelConfig<?> mConfig = getRootDomainConfig(executionContext);
		
		LookupSearchCriteria lookupSearchCriteria = createSearchCriteria(executionContext, mConfig);
		Class<?> criteriaClass = mConfig.getReferredClass();
		String alias = findRepoAlias(criteriaClass);
		
		ModelRepository rep = getRepFactory().get(mConfig.getRepo()); //TODO what if it is a non db search like WS call for search ? 
		
		List<?> searchResult = (List<?>)rep._search(criteriaClass, alias, lookupSearchCriteria);

		Command cmd = executionContext.getCommandMessage().getCommand();
		if(StringUtils.equalsIgnoreCase(cmd.getElement(Type.DomainAlias).get().getAlias(), "staticCodeValue")) {
			return getStaticParamValues((List<StaticCodeValue>)searchResult, cmd);
		}
		return getDynamicParamValues(lookupSearchCriteria, criteriaClass, searchResult);
	}

	@Override
	protected LookupSearchCriteria createSearchCriteria(ExecutionContext executionContext, ModelConfig<?> mConfig) {
		Command cmd = executionContext.getCommandMessage().getCommand();
		
		LookupSearchCriteria lookupSearchCriteria = new LookupSearchCriteria();
		lookupSearchCriteria.validate(executionContext);
			
		lookupSearchCriteria.setWhere(executionContext.getCommandMessage().getCommand().getFirstParameterValue("where"));
		
		ProjectCriteria projectCriteria = new ProjectCriteria();
		
		if(cmd.getRequestParams().get("projection.alias") != null) {
			projectCriteria.setAlias(cmd.getFirstParameterValue("projection.alias"));
		}
		else if(cmd.getRequestParams().get("projection.mapsTo") != null) {
			String projectMapping = cmd.getFirstParameterValue("projection.mapsTo");
			String[] keyValues = StringUtils.split(projectMapping,",");
			
			Stream.of(keyValues).forEach((kvString) -> {
				if(MapUtils.isEmpty(projectCriteria.getMapsTo())){
					projectCriteria.setMapsTo(new HashMap<String, String>());
				}
				String[] kv = StringUtils.split(kvString,":");
				projectCriteria.getMapsTo().put(kv[0], kv[1]);
			});
		}
		
		lookupSearchCriteria.setProjectCriteria(projectCriteria);
		
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
		
		PropertyDescriptor codePd = BeanUtils.getPropertyDescriptor(criteriaClass, list.get(0));
		PropertyDescriptor labelPd = BeanUtils.getPropertyDescriptor(criteriaClass, list.get(1));
		
		try {
			List<ParamValue> paramValues = new ArrayList<>();
			for(Object model: searchResult) {
				paramValues.add(new ParamValue((String)codePd.getReadMethod().invoke(model), (String)labelPd.getReadMethod().invoke(model)));
			}
			return (R)paramValues;
		}
		catch(Exception ex) {
			throw new FrameworkRuntimeException("Failed to execute read on property: "+codePd+" and "+labelPd, ex);
		}
	}
	
}
