/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.notification;

import java.util.EnumSet;
import java.util.Optional;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class ActivateConditionalNotificationHandler implements OnStateLoadHandler<ActivateConditional>, OnStateChangeHandler<ActivateConditional> {

	private ExpressionEvaluator expressionEvaluator;
	
	public ActivateConditionalNotificationHandler(BeanResolverStrategy beanResolver) {
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}
	
	@Override
	public void handle(ActivateConditional configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation);
	}
	
	@Override
	public void handle(ActivateConditional configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
		EnumSet<Action> validSet = EnumSet.of(Action._new, Action._update, Action._replace, Action._delete);
		
		if(!validSet.contains(event.getAction()))
			return;
		
		handleInternal(event.getParam(), configuredAnnotation);
	}
	
	protected void handleInternal(Param<?> onChangeParam, ActivateConditional configuredAnnotation) {
		String whenExpr = configuredAnnotation.when();
		
		Object entityState = onChangeParam.getLeafState();
		boolean isTrue = expressionEvaluator.getValue(whenExpr, new Holder<>(entityState), Boolean.class);
		
		// validate target param to activate
		String targetPath = configuredAnnotation.targetPath();

		Param<?> targetParam = Optional.ofNullable(onChangeParam.findParamByPath(targetPath))
								.orElseThrow(()->new InvalidConfigException("Target parm lookup returned null for targetPath: "+targetPath+" on param: "+onChangeParam));
		
		if(isTrue)
			targetParam.activate();
		else
			targetParam.deactivate();
	}
	
}
