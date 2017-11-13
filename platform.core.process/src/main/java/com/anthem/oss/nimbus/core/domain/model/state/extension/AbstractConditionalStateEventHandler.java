/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Optional;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.expr.ExpressionEvaluator;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

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
		Object entityState = onChangeParam.getLeafState();
		return expressionEvaluator.getValue(whenExpr, new Holder<>(entityState), Boolean.class);
	}
	
	protected Param<?> retrieveParamByPath(Param<?> baseParam, String targetPath) {
		return Optional.ofNullable(baseParam.findParamByPath(targetPath))
				.orElseThrow(() -> new InvalidConfigException("Target param lookup returned null for targetPath: " + targetPath + " on param: " + baseParam));
	}
}
