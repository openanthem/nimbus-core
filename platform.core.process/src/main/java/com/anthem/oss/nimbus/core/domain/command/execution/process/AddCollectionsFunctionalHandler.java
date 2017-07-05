/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Swetha Vemuri
 *
 */
public class AddCollectionsFunctionalHandler <T,S> implements FunctionHandler<T,S> {

	private final CommandMessageConverter converter;
	
	public AddCollectionsFunctionalHandler(BeanResolverStrategy beanResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	
	@Override
	public S execute(ExecutionContext eCtx, Param<T> actionParameter) {
		
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		Class<?> convertToClass = actionParameter.getConfig().getType().findIfCollection().getElementConfig().getReferredClass();
		
		Object convertedObject = converter.convert(convertToClass, cmdMsg.getRawPayload());
		String targetState = cmdMsg.getCommand().getFirstParameterValue("set");
		Param<T> param = eCtx.getQuadModel().getCore().findParamByPath(targetState);
		
		param.findIfCollection().add(convertedObject);
		
		return null;
	}
}
