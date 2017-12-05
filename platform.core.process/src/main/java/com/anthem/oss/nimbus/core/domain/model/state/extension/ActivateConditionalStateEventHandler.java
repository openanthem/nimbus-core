/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Soham Chakravarti
 *
 */
public class ActivateConditionalStateEventHandler extends AbstractConditionalStateEventHandler.EvalExprWithCrudActions<ActivateConditional> {

	public ActivateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	protected void handleInternal(Param<?> onChangeParam, ActivateConditional configuredAnnotation) {
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		// validate target param to activate
		String[] targetPaths = configuredAnnotation.targetPath();

		Arrays.asList(targetPaths).stream()
			.forEach(targetPath -> handleInternal(onChangeParam, targetPath, (targetParam->{
				if(isTrue)
					targetParam.activate();
				else
					targetParam.deactivate();
			})));
		
	}
	
}
