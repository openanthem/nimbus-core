/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.domain.command.CommandElement.Type;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.utils.ParamPathExpressionParser;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultCommandPathVariableResolver implements CommandPathVariableResolver {

	@Override
	public String resolve(ExecutionContext eCtx, Param<?> commandParam, String urlToResolve) {
		Map<Integer, String> entries = ParamPathExpressionParser.parse(urlToResolve);
		if(MapUtils.isEmpty(entries))
			return urlToResolve;
		
		String out = urlToResolve;
		for(Integer i : entries.keySet()) {
			String key = entries.get(i);
			
			// look for relative path to passed in param's parent model
			String pathToResolve = ParamPathExpressionParser.stripPrefixSuffix(key);
			
			String val = map(eCtx, commandParam, pathToResolve);
			
			out = StringUtils.replace(out, key, val, 1);
		}
		
		return out;
	}
	
	protected String map(ExecutionContext eCtx, Param<?> commandParam, String pathToResolve) {
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_SESSION_SELF.code))
			return mapSelf(eCtx, commandParam, pathToResolve);
		
		if(StringUtils.startsWithIgnoreCase(pathToResolve, Constants.MARKER_COMMAND_PARAM_CURRENT_SELF.code))
			return commandParam.getPath();
		
		return mapQuad(eCtx, commandParam, pathToResolve);
	}
	
	protected String mapSelf(ExecutionContext eCtx, Param<?> commandParam, String pathToResolve) {
		//TODO bean path evaluation to get value
		return eCtx.getCommandMessage().getCommand().getElement(Type.ClientAlias).get().getAlias();
	}
	
	protected String mapQuad(ExecutionContext eCtx, Param<?> commandParam, String pathToResolve) {
		Param<?> p = commandParam.getParentModel().findParamByPath(pathToResolve);
		return String.valueOf(p.getState());
	}
}
