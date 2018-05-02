package com.antheminc.oss.nimbus.domain.model.state.extension;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.MessageConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;

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
		
		else if(!configuredAnnotation.whenElseRetainMessage()) {
			onChangeParam.setMessage(null);
		}
		
		
	}

}
