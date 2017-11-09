/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Optional;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.definition.extension.ActivateConditional;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.ExecutionTxnContext;
import com.anthem.oss.nimbus.core.domain.model.state.ParamEvent;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateChangeHandler;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class ActivateConditionalStateEventHandler extends AbstractConditionalStateEventHandler implements OnStateLoadHandler<ActivateConditional>, OnStateChangeHandler<ActivateConditional> {

	public ActivateConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
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
		boolean isTrue = evalWhen(onChangeParam, configuredAnnotation.when());
		
		// validate target param to activate
		String[] targetPaths = configuredAnnotation.targetPath();

		Arrays.asList(targetPaths).stream().forEach((targetPath) ->
			handleInternal(onChangeParam,targetPath,isTrue));
		
	}
	
	protected void handleInternal(Param<?> onChangeParam, String targetPath, boolean evalWhen) {
		final Param<?> targetParam = this.retrieveParamByPath(onChangeParam, targetPath);

		if (evalWhen) {
			targetParam.activate();
		} else {
			targetParam.deactivate();
		}
	}
	
}
