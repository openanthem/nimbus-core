/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution.process;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.command.execution.FunctionHandler;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ModelRepository;

/**
 * @author Swetha Vemuri
 *
 */
public class AddCollectionsFunctionalHandler <T,S> implements FunctionHandler<T,S> {
	
	private final ModelRepository repo;

	private final CommandMessageConverter converter;
	
	public AddCollectionsFunctionalHandler(BeanResolverStrategy beanResolver) {
		this.converter = beanResolver.get(CommandMessageConverter.class);
		this.repo = beanResolver.get(ModelRepository.class,"rep_mongodb");
	}

	
	@Override
	public S execute(ExecutionContext eCtx, Param<T> actionParameter) {
		
		CommandMessage cmdMsg = eCtx.getCommandMessage();
		Class<?> convertToClass = actionParameter.getConfig().getType().findIfCollection().getElementConfig().getReferredClass();
		ModelConfig<Object> mConfig = actionParameter.getConfig().getType().findIfCollection().getElementConfig().getType().findIfNested().getModel();
		//TODO - converter cannot convert mapsTo object. The payload has to match the object representation. Need to fix this to use mapsTo Path for convert
		Object convertedObject = converter.convert(convertToClass, cmdMsg.getRawPayload());	
		String targetState = cmdMsg.getCommand().getFirstParameterValue("set");
		repo._new(mConfig, convertedObject);
		Param<T> param = eCtx.getQuadModel().getCore().findParamByPath(targetState);
		
		param.findIfCollection().add(convertedObject);
		
		return null;
	}
}
