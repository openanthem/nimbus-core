/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.definition.extension.EnableConditional;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class EnableConditionalStateEventHandler extends AbstractConditionalStateEventHandler.EvalExprWithCrudActions<EnableConditional> {

	public EnableConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override	
	protected void handleInternal(Param<?> onChangeParam, EnableConditional configuredAnnotation) {
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		// validate target param to enable
		String[] targetPaths = configuredAnnotation.targetPath();

		Arrays.asList(targetPaths).stream()
			.forEach(targetPath -> handleInternal(onChangeParam, targetPath, targetParam->targetParam.setEnabled(isTrue)));
		
	}
}
