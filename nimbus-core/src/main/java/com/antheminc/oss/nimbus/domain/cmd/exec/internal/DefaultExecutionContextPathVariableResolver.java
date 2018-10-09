package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.CommandMessageConverter;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ParamPathExpressionParser;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn.FilterMode;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.domain.model.config.ParamConfig.MappedParamConfig;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.PageFilter;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author Rakesh Patel
 *
 */
@Getter(value=AccessLevel.PROTECTED)
public class DefaultExecutionContextPathVariableResolver implements ExecutionContextPathVariableResolver {

	protected final JustLogit logit = new JustLogit(DefaultExecutionContextPathVariableResolver.class);
	private static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" };
	private static final String STRING_NULL = "null";
	private static final String DEFAULT_STRICT_FILTER_MODE = "eq";
	
	private final CommandMessageConverter converter;
	
	public DefaultExecutionContextPathVariableResolver(BeanResolverStrategy beanResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	@Override
	public String resolve(ExecutionContext eCtx, Param<?> param, String pathToResolve) {
		if(StringUtils.trimToNull(pathToResolve)==null)
			return pathToResolve;
		
		return resolveInternal(eCtx, param, pathToResolve);
	}

	
	protected String resolveInternal(ExecutionContext eCtx, Param<?> param, String pathToResolve) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(pathToResolve);
		if(MapUtils.isEmpty(entries))
			return pathToResolve;
		
		String out = pathToResolve;
		for(Integer i : entries.keySet()) {
			String key = entries.get(i);
			
			// look for relative path to execution context
			String val = mapSearchMarkers(eCtx, param, key);
			
			if(StringUtils.isNotBlank(val)) {
				out = StringUtils.replace(out, key, val, 1);
			}
			else {
				String before = StringUtils.substringBefore(out, key);
				String after = StringUtils.substringAfter(out, key);
				
				out = StringUtils.endsWith(before, Constants.PARAM_ASSIGNMENT_MARKER.code)
						? StringUtils.join(before, STRING_NULL, after)
						: StringUtils.join(before, after);
			}
		}
		
		return out;
	}
	
	
	private String mapSearchMarkers(ExecutionContext eCtx, Param<?> param, String key) {
		String entryToResolve = ParamPathExpressionParser.stripPrefixSuffix(key);
		if(StringUtils.equalsIgnoreCase(entryToResolve, Constants.SERVER_PAGE_EXP_MARKER.code)) {
			return mapPageCriteria(eCtx, param);
		}
		if(StringUtils.equalsIgnoreCase(entryToResolve, Constants.SERVER_FILTER_EXPR_MARKER.code)) {
			return mapFilterCriteria(eCtx, param);
		}
		
		return key;
	}

	private String mapPageCriteria(ExecutionContext eCtx, Param<?> param) {
		//String pageCriteria = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.SERVER_PAGE_CRITERIA_EXPR_MARKER.code);
		String pageSize = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.SEARCH_REQ_PAGINATION_SIZE.code);
		String page = eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.SEARCH_REQ_PAGINATION_PAGE_NUM.code);
		String[] sortBy = eCtx.getCommandMessage().getCommand().getParameterValue(Constants.SEARCH_REQ_PAGINATION_SORT_PROPERTY.code);
		
		StringBuilder url = new StringBuilder();
		
		if(StringUtils.isNotBlank(pageSize) && StringUtils.isNotBlank(page) ) {
			url.append(Constants.SEARCH_REQ_PAGINATION_SIZE.code).append(Constants.PARAM_ASSIGNMENT_MARKER.code).append(pageSize)
					.append(Constants.REQUEST_PARAMETER_DELIMITER.code).append(Constants.SEARCH_REQ_PAGINATION_PAGE_NUM.code)
					.append(Constants.PARAM_ASSIGNMENT_MARKER.code).append(page);
		}
		
		if(sortBy != null && sortBy.length > 0) {
			Stream.of(sortBy)
				.forEach(sort -> url.append(Constants.REQUEST_PARAMETER_DELIMITER.code)
						.append(Constants.SEARCH_REQ_PAGINATION_SORT_PROPERTY.code)
						.append(Constants.PARAM_ASSIGNMENT_MARKER.code)
						.append(sort));
		}
		
		String pageCriteria = url.toString();
		
		if(StringUtils.isNotBlank(pageCriteria)) {
			String[] sortByCriteria = StringUtils.substringsBetween(pageCriteria, "sortBy=", ",");
			
			if(sortByCriteria != null && sortByCriteria.length > 0) {
				pageCriteria=Stream.of(sortByCriteria)
			             .map(toRem-> (Function<String,String>)s->s.replaceAll(toRem, findMappedParamPath(toRem,param)).replaceAll("/", "."))
			             .reduce(Function.identity(), Function::andThen)
			             .apply(pageCriteria);
			}
		}
		
		return pageCriteria;
	}
	
	private String findMappedParamPath(String currentParamPath, Param<?> param) {
		ParamConfig<?> p = param.getConfig().getType().findIfCollection().getElementConfig();
		MappedParamConfig<?,?> mappedParam = p.findIfMapped();
		
		String paramPath = currentParamPath;
		ParamConfig<?> currentParam = p.getType().findIfNested().getModelConfig()
				.findParamByPath("/" + currentParamPath);
		if(mappedParam != null) {
			MappedParamConfig<?, ?> mappedElemParam = (MappedParamConfig<?, ?>) currentParam;
			String mappedPath = mappedElemParam.getPath().value();
			if (StringUtils.isNotBlank(mappedPath)) {
				paramPath = StringUtils.stripStart(mappedPath, "/");
			}
		}
		
		return paramPath;
	}

	private String mapFilterCriteria(ExecutionContext eCtx, Param<?> param) {
		final PageFilter pageFilter;
		try {
			pageFilter = getConverter().toType(PageFilter.class, eCtx.getCommandMessage().getRawPayload());
			if(pageFilter == null || CollectionUtils.isEmpty(pageFilter.getFilters()))
				return null;
		}
		catch (FrameworkRuntimeException e) {
			logit.error(() -> "Could not convert the rawPayload " + eCtx.getCommandMessage().getRawPayload()
					+ " to filterCriteria for search config on param: " + param
					+ ". This can happen if 1. You are submitting a different payload and also have configured the search with <!filterCriteria!> in which case we cannot support the filterCriteria or 2. The submitted filterCriteria cannot be serialized which needs to be investigated.",
					e);
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		ParamConfig<?> p = param.getConfig().getType().findIfCollection().getElementConfig();
		MappedParamConfig<?,?> mappedParam = p.findIfMapped();
		
		final String alias = getAlias(p, mappedParam);
		
		pageFilter.getFilters().forEach(f -> {
			String paramPath = findMappedParamPath(f.getCode(), param);
			ParamConfig<?> currentParam = p.getType().findIfNested().getModelConfig().findParamByPath("/" + f.getCode());
			FilterMode filterMode = (FilterMode) currentParam.getUiStyles().getAttributes().get("filterMode");
			
			//TODO - once additional filter patterns are implemented, need to revisit this
			if(StringUtils.containsIgnoreCase(currentParam.getType().getName(), "date")) {
				buildDateCriteria(builder, alias, paramPath, f.getValue());
			}
			else if(ClassUtils.isAssignable(currentParam.getReferredClass(), Number.class, true)) {
				String mode = FilterMode.getStrictMatchModeFor(filterMode);
				if(!FilterMode.isValidNumericFilter(filterMode)) {
					logit.error(() -> "Invalid FilterMode '"+filterMode+"' configured for param "+currentParam+ " ,Using the default match - '"+DEFAULT_STRICT_FILTER_MODE+"'");
					mode = DEFAULT_STRICT_FILTER_MODE;
				}
				buildCriteria(builder, alias, paramPath, f.getValue(), mode);
			}
			else if(ClassUtils.isAssignable(currentParam.getReferredClass(), Boolean.class, true)) {
				String mode = FilterMode.getStrictMatchModeFor(filterMode);
				if(!FilterMode.isValidBooleanFilter(filterMode)) {
					logit.error(() -> "Invalid FilterMode "+filterMode+" configured for param "+currentParam+ " ,Using the default match - '"+DEFAULT_STRICT_FILTER_MODE+"'");
					mode = DEFAULT_STRICT_FILTER_MODE;
				}
				buildCriteria(builder, alias, paramPath, f.getValue(), mode);
			}
			else {
				buildCriteria(builder, alias, paramPath, "'"+f.getValue()+"'", filterMode.getCode());
			}

		});
		
		return builder.toString();
	}

	private void buildDateCriteria(StringBuilder builder, final String alias, String paramPath, String value ) {
		LocalDate dateValue = getLocalDate(value);
		if(dateValue != null) {
			LocalDate nextDateValue = dateValue.plusDays(1);
			int year = dateValue.getYear();
		    int month = dateValue.getMonthValue();
		    int day = dateValue.getDayOfMonth();
			
		    int nextDateYear = nextDateValue.getYear();
		    int nextDateMonth = nextDateValue.getMonthValue();
		    int nextDateDay = nextDateValue.getDayOfMonth();
			
			buildCriteria(builder, alias, paramPath, "java.time.LocalDate.of("+year+", "+month+", "+day+")", "goe");
			buildCriteria(builder, alias, paramPath, "java.time.LocalDate.of("+nextDateYear+", "+nextDateMonth+", "+nextDateDay+")", "lt");
		}
	}

	private String getAlias(ParamConfig<?> p, MappedParamConfig<?, ?> mappedParam) {
		final ModelConfig<?> modelConfig = mappedParam != null ? mappedParam.getMapsToConfig().getType().findIfNested().getModelConfig() 
						: p.getType().findIfNested().getModelConfig();
		return modelConfig.getRepo() != null && StringUtils.isNotBlank(modelConfig.getRepo().alias())
				? modelConfig.getRepo().alias()
				: modelConfig.getAlias();
	}

	private void buildCriteria(StringBuilder builder, String alias, String paramPath, String value, String operator) {
		builder.append(".and(").append(alias + ".").append(paramPath.replaceAll("/", ".")).append(".").append(operator)
				.append("(").append(value).append("))");
	}

	private LocalDate getLocalDate(String value) {
		for (String DATE_FORMAT : DATE_FORMATS) {
			try {
				return LocalDate.parse(value, DateTimeFormatter.ofPattern(DATE_FORMAT));
			} catch (Exception e) {
				
			}
		}
		return null;
	}
	
}
