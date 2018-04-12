package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
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
import com.antheminc.oss.nimbus.domain.model.state.repo.db.SearchCriteria.FilterCriteria;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultExecutionContextPathVariableResolver implements ExecutionContextPathVariableResolver {

	protected final JustLogit logit = new JustLogit(this.getClass());
	private static final String[] DATE_FORMATS = new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" };
	private static final String STRING_NULL = "null";
	
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
			String entryToResolve = ParamPathExpressionParser.stripPrefixSuffix(key);
			
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
			return mapPageCriteria(eCtx);
		}
		if(StringUtils.equalsIgnoreCase(entryToResolve, Constants.SERVER_FILTER_EXPR_MARKER.code)) {
			return mapFilterCriteria(eCtx, param);
		}
		
		return key;
	}

	private String mapPageCriteria(ExecutionContext eCtx) {
		return eCtx.getCommandMessage().getCommand().getFirstParameterValue(Constants.SERVER_PAGE_CRITERIA_EXPR_MARKER.code);
	}

	@SuppressWarnings("unchecked")
	private String mapFilterCriteria(ExecutionContext eCtx, Param<?> param) {
		final List<FilterCriteria> filters;
		try {
			filters = converter.readArray(FilterCriteria.class, List.class, eCtx.getCommandMessage().getRawPayload());
		}
		catch (FrameworkRuntimeException e) {
			logit.error(() -> "Could not convert the rawPayload " + eCtx.getCommandMessage().getRawPayload()
					+ " to filterCriteria for search config on param: " + param
					+ ". This can happen if 1. You are submitting a different payload and also have configured the search with <!filterCriteria!> in which case we cannot support the filterCriteria or 2. The submitted filterCriteria cannot be serialized which needs to be investigated.",
					e);
			return null;
		}
		
		if(CollectionUtils.isEmpty(filters))
			return null;
		
		StringBuilder builder = new StringBuilder();
		ParamConfig<?> p = param.getConfig().getType().findIfCollection().getElementConfig();
		MappedParamConfig<?,?> mappedParam = p.findIfMapped();
		
		final String alias;
		if(mappedParam != null) {
			ModelConfig<?> mappedModelConfig = mappedParam.getMapsToConfig().getType().findIfNested().getModelConfig();
			alias = mappedModelConfig.getRepo() != null && StringUtils.isNotBlank(mappedModelConfig.getRepo().alias())
					? mappedModelConfig.getRepo().alias()
					: mappedModelConfig.getAlias();
		}
		else {
			ModelConfig<?> modelConfig = p.getType().findIfNested().getModelConfig();
			alias = modelConfig.getRepo() != null && StringUtils.isNotBlank(modelConfig.getRepo().alias())
					? modelConfig.getRepo().alias()
					: modelConfig.getAlias();
		}
		
		filters.forEach(f -> {

			String paramPath = f.getCode();
			ParamConfig<?> currentParam = p.getType().findIfNested().getModelConfig()
					.findParamByPath("/" + f.getCode());
			if(mappedParam != null) {
				MappedParamConfig<?, ?> mappedElemParam = (MappedParamConfig<?, ?>) currentParam;
				String mappedPath = mappedElemParam.getPath().value();
				if (StringUtils.isNotBlank(mappedPath)) {
					paramPath = StringUtils.stripStart(mappedPath, "/");
				}
			}
			FilterMode filterMode = (FilterMode) currentParam.getUiStyles().getAttributes().get("filterMode");
			String operator = filterMode.getCode();
			
			if(StringUtils.containsIgnoreCase(currentParam.getType().getName(), "date")) {
				LocalDate dateValue = getLocalDate(f.getValue());
				if(dateValue != null) {
					int year = dateValue.getYear();
				    int month = dateValue.getMonthValue();
				    int day = dateValue.getDayOfMonth();
					int nextDay = day + 1;
					
					buildCriteria(builder, alias, paramPath, "java.time.LocalDate.of("+year+", "+month+", "+day+")", "goe");
					buildCriteria(builder, alias, paramPath, "java.time.LocalDate.of("+year+", "+month+", "+nextDay+")", "lt");
				}
			}
			else {
				buildCriteria(builder, alias, paramPath, "'"+f.getValue()+"'", operator);
			}

		});
		
		return builder.toString();
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
