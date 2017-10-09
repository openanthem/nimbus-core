/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractConditionalStateEventHandler {

	protected ExpressionEvaluator expressionEvaluator;
	
	public AbstractConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		this.expressionEvaluator = beanResolver.get(ExpressionEvaluator.class);
	}
	
	protected boolean evalWhen(Param<?> onChangeParam, String whenExpr) {
		Object entityState = onChangeParam.getLeafState();
		return expressionEvaluator.getValue(whenExpr, new Holder<>(entityState), Boolean.class);
	}
}
