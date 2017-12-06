/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import java.lang.annotation.Annotation;
import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Consumer;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateHolder;
import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.antheminc.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.antheminc.oss.nimbus.core.domain.model.state.ParamEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.antheminc.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractConditionalStateEventHandler {

	protected BeanResolverStrategy beanResolver;
	
	protected ExpressionEvaluator expressionEvaluator;
	
	public AbstractConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}
	
	protected boolean evalWhen(Param<?> onChangeParam, String whenExpr) {
		return expressionEvaluator.getValue(whenExpr, new ParamStateHolder(onChangeParam), Boolean.class);
	}
	
	protected Param<?> retrieveParamByPath(Param<?> baseParam, String targetPath) {
		return Optional.ofNullable(baseParam.findParamByPath(targetPath))
				.orElseThrow(() -> new InvalidConfigException("Target param lookup returned null for targetPath: " + targetPath + " on param: " + baseParam));
	}
	
	protected static abstract class EvalExprWithCrudActions<A extends Annotation> extends AbstractConditionalStateEventHandler 
		implements OnStateLoadHandler<A>, OnStateChangeHandler<A> {
		
		public EvalExprWithCrudActions(BeanResolverStrategy beanResolver) {
			super(beanResolver);
		}
		
		@Override
		public void handle(A configuredAnnotation, Param<?> param) {
			handleInternal(param, configuredAnnotation);
		}
		
		@Override
		public void handle(A configuredAnnotation, ExecutionTxnContext txnCtx, ParamEvent event) {
			EnumSet<Action> validSet = EnumSet.of(Action._new, Action._update, Action._replace, Action._delete);
			
			if(!validSet.contains(event.getAction()))
				return;
			
			handleInternal(event.getParam(), configuredAnnotation);
		}
		
		protected abstract void handleInternal(Param<?> onChangeParam, A configuredAnnotation);
		
		protected void handleInternal(Param<?> onChangeParam, String targetPath, Consumer<Param<?>> executeCb) {
			Param<?> targetParam = retrieveParamByPath(onChangeParam, targetPath);

			executeCb.accept(targetParam);
		}
	}
}
