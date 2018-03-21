package com.antheminc.oss.nimbus.domain.cmd.exec.internal;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContextPathVariableResolver;
import com.antheminc.oss.nimbus.domain.cmd.exec.ParamPathExpressionParser;
import com.antheminc.oss.nimbus.domain.defn.Constants;

/**
 * @author Rakesh Patel
 *
 */
public class DefaultExecutionContextPathVariableResolver implements ExecutionContextPathVariableResolver{

	@Override
	public String resolve(ExecutionContext eCtx, String pathToResolve) {
		if(StringUtils.trimToNull(pathToResolve)==null)
			return pathToResolve;
		
		return resolveInternal(eCtx, pathToResolve);
	}

	
	protected String resolveInternal(ExecutionContext eCtx, String pathToResolve) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(pathToResolve);
		if(MapUtils.isEmpty(entries))
			return pathToResolve;
		
		String out = pathToResolve;
		for(Integer i : entries.keySet()) {
			String key = entries.get(i);
			
			// look for relative path to execution context
			String entryToResolve = ParamPathExpressionParser.stripPrefixSuffix(key);
			
			String val = mapMarkers(eCtx, entryToResolve);
			
			out = StringUtils.replace(out, key, val, 1);
		}
		
		return out;
	}
	
	
	private String mapMarkers(ExecutionContext eCtx, String pathToResolve) {
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_URI_PAGE_EXPR.code)) 
			return eCtx.getCommandMessage().getCommand().getFirstParameterValue("pageCriteria");
		
		return null;
	}
	
}
