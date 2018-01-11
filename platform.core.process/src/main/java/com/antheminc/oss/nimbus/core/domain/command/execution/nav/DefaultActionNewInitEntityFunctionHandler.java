/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command.execution.nav;

import java.util.Arrays;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.antheminc.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.antheminc.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.antheminc.oss.nimbus.core.domain.definition.Constants;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamConfig;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultActionNewInitEntityFunctionHandler<T> implements FunctionHandler<T, Param<T>> {

	private final CommandMessageConverter converter;
	
	public DefaultActionNewInitEntityFunctionHandler(BeanResolverStrategy beanResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	@Override
	public Param<T> execute(ExecutionContext eCtx, Param<T> actionParameter) {
		Command cmd = eCtx.getCommandMessage().getCommand();
		
		String[] targetParams = Optional.ofNullable(cmd.getParameterValue(Constants.KEY_FN_INITSTATE_ARG_TARGET_PATH.code))
									.orElseThrow(()->new InvalidConfigException("'"+Constants.KEY_FN_INITSTATE_ARG_TARGET_PATH.code+"' must be configured but found null for cmd:"+cmd));
		// To set multiple values values in target since the new command does not have handle of the old object
		// Ex : @Config(url="/p/queue/_new?fn=_initEntity&target=/entityId&json=\"<!/.m/id!>\"&target=/name&json=\"<!/.m/name!>\"")
		for(int index =0; index < targetParams.length; index++) {
			
			String targetParamPath = targetParams[index];
			Param<Object> targetParam = Optional.ofNullable(actionParameter.findParamByPath(targetParamPath))
					.orElseThrow(()->new InvalidConfigException("No param for configured target path: "+targetParamPath+" for cmd: "+cmd));

			Object converted = resolveTargetState(eCtx.getCommandMessage(), index, targetParam);
			if(converted != null)
				targetParam.setState(converted);
		}		
		return actionParameter;
	}
	
	protected Object resolveTargetState(CommandMessage cmdMessage, int index, Param<Object> targetParam) {

		String json = null;
		
		if(cmdMessage.getCommand().getParameterValue(Constants.KEY_FN_INITSTATE_ARG_JSON.code) != null)
			json = Arrays.asList(cmdMessage.getCommand().getParameterValue(Constants.KEY_FN_INITSTATE_ARG_JSON.code)).get(index);
		else{
			json = cmdMessage.getRawPayload();
		}
			
		if(json == null)
			return null;
		return resolveAsJson(cmdMessage.getCommand(), targetParam.getConfig(), json);
		
	}

	private Object resolveAsJson(Command cmd, ParamConfig<Object> pConfig, String json) {
		json =	Optional.ofNullable(json)
				.map(StringUtils::trimToNull)
				.orElseThrow(()->new InvalidConfigException("'json' OR must be configured but found null for cmd:"+cmd));
		
		Object converted = converter.convert(pConfig, json);
		return converted;
	}
}
