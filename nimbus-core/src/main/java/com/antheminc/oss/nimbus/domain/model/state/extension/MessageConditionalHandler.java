package com.antheminc.oss.nimbus.domain.model.state.extension;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

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
		
		if(isValid) {
			// The unevaluated msg is considered unique as we dont want to show the same msg twice in different contexts or Types
			// TODO: The same msg should not be added accross two Message Conditionals -Add a static validation for it in future
			Message msg = new Message(configuredAnnotation.message(), evaluatedMessage, configuredAnnotation.messageType(), configuredAnnotation.context(),configuredAnnotation.cssClass());

			Set<Message> newMsgs = new HashSet<>();
			if(!CollectionUtils.isEmpty(onChangeParam.getMessages())) 
				newMsgs.addAll(onChangeParam.getMessages());
			
			//  - if an existing msg with the same Key(when condition is present - then the new msg doesnt get added in a HashSet
			// -  We want to replace the old value with the new value for the same key
			if(!newMsgs.add(msg)) {
				newMsgs.remove(msg);
				
			 }
			newMsgs.add(msg);
			onChangeParam.setMessages(newMsgs);
			
		}
		else if(!configuredAnnotation.whenElseRetainMessage()) {
			if(CollectionUtils.isEmpty(onChangeParam.getMessages())) 
				return;
			
			Set<Message> oldMsgs = new HashSet<>(onChangeParam.getMessages());
			
			// Remove this msg
			
			oldMsgs.removeIf(msg -> msg.getUniqueId().equals(configuredAnnotation.message()));
			if(CollectionUtils.isEmpty(oldMsgs))
				onChangeParam.setMessages(null);
			else
				onChangeParam.setMessages(oldMsgs);
		}
		
		
	}
	
	

}
