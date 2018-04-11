package com.antheminc.oss.nimbus.domain.model.state.extension;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.defn.extension.WarningConditional;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Context;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param.Message.Type;
import com.antheminc.oss.nimbus.domain.model.state.StateHolder.ParamStateHolder;
import com.antheminc.oss.nimbus.domain.model.state.extension.AbstractConditionalStateEventHandler;

public class WarningConditionalHandler extends AbstractConditionalStateEventHandler.EvalExprWithCrudActions<WarningConditional> {

	public WarningConditionalHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}



	protected boolean evalWhen(Param<?> onChangeParam, String whenExpr) {
		return expressionEvaluator.getValue(whenExpr, new ParamStateHolder<>(onChangeParam), Boolean.class);
	}

	

	@Override
	protected void handleInternal(Param<?> onChangeParam, WarningConditional configuredAnnotation) {
		boolean isValid = this.evalWhen(onChangeParam, configuredAnnotation.when());
		Message msg = new Message(configuredAnnotation.message(), Type.WARNING, Context.INLINE);
		
		if(isValid)onChangeParam.setMessage(msg);
	}

}
