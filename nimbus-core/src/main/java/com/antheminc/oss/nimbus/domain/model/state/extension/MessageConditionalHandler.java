package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;

/**
 * @author Akancha Kashyap
 *
 */

public class MessageConditionalHandler extends EvalExprWithCrudActions<MessageConditional> {

	public MessageConditionalHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	@Override
	protected void handleInternal(Param<?> onChangeParam, MessageConditional configuredAnnotation) {
		boolean isValid = this.evalWhen(onChangeParam, configuredAnnotation.when());
		// Evaluate the msg as a spel expression
		String evaluatedMessage = expressionEvaluator.getValue(configuredAnnotation.message(), new ParamStateHolder<>(onChangeParam), String.class);
		
		String[] targetPaths = configuredAnnotation.targetPath();
		
		if(isValid) {
			if(StringUtils.isAllEmpty(targetPaths)) {
				addMessageToParam(onChangeParam, evaluatedMessage, configuredAnnotation);
			} else {
				Arrays.asList(targetPaths).stream()
				.forEach(targetPath -> {
					Param<?> targetParam = retrieveParamByPath(onChangeParam, targetPath);
					addMessageToParam(targetParam, evaluatedMessage, configuredAnnotation);
				});
			}
		}
		else if(!configuredAnnotation.whenElseRetainMessage()) {
			if(StringUtils.isAllEmpty(targetPaths)) {
				if(CollectionUtils.isEmpty(onChangeParam.getMessages())) 
					return;
				
				removeMessageFromParam(onChangeParam, configuredAnnotation);
			} else {
				Arrays.asList(targetPaths).stream()
				.forEach(targetPath -> {
					Param<?> targetParam = retrieveParamByPath(onChangeParam, targetPath);
					if(CollectionUtils.isNotEmpty(targetParam.getMessages())) {
						removeMessageFromParam(targetParam, configuredAnnotation);
					}
				});
			}
			
			
		}
		
		
	}
	
	private void addMessageToParam(Param<?> param, String message, MessageConditional configuredAnnotation) {
		// The unevaluated msg is considered unique as we dont want to show the same msg twice in different contexts or Types
		// TODO: The same msg should not be added accross two Message Conditionals -Add a static validation for it in future
		Message msg = new Message(configuredAnnotation.message(), message, configuredAnnotation.messageType(), configuredAnnotation.context(),configuredAnnotation.cssClass());

		Set<Message> newMsgs = new HashSet<>();
		if(!CollectionUtils.isEmpty(param.getMessages())) 
			newMsgs.addAll(param.getMessages());
		
		//  - if an existing msg with the same Key(when condition is present - then the new msg doesnt get added in a HashSet
		// -  We want to replace the old value with the new value for the same key
		if(!newMsgs.add(msg)) {
			newMsgs.remove(msg);
			
		 }
		newMsgs.add(msg);
		param.setMessages(newMsgs);
	}
	
	private void removeMessageFromParam(Param<?> param, MessageConditional configuredAnnotation) {
		Set<Message> oldMsgs = new HashSet<>(param.getMessages());
		
		// Remove this msg
		oldMsgs.removeIf(msg -> msg.getUniqueId().equals(configuredAnnotation.message()));
		if(CollectionUtils.isEmpty(oldMsgs))
			param.setMessages(null);
		else
			param.setMessages(oldMsgs);
	}
	

}
