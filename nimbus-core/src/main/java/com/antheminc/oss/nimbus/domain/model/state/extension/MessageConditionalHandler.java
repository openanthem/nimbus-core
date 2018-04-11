package com.antheminc.oss.nimbus.domain.model.state.extension;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.extension.AbstractConditionalStateEventHandler;

/**
 * @author Akancha Kashyap
 *
 */
public class MessageConditionalHandler extends AbstractConditionalStateEventHandler.EvalExprWithCrudActions<MessageConditional> {

	public MessageConditionalHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	@Override
	protected void handleInternal(Param<?> onChangeParam, MessageConditional configuredAnnotation) {
		boolean isValid = this.evalWhen(onChangeParam, configuredAnnotation.when());
		
		
		if(isValid) {
			Message msg = new Message(configuredAnnotation.message(), configuredAnnotation.messageType(), configuredAnnotation.context());
			onChangeParam.setMessage(msg);
		}
		// Be  default the message is retained on the param
		else if(!configuredAnnotation.whenElseRetainMsg()) {
			Message msg = new Message(null, configuredAnnotation.messageType(), configuredAnnotation.context());
			onChangeParam.setMessage(msg);
		}
		
		
	}

}
