/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.config.DomainConfig;
import com.anthem.oss.nimbus.core.domain.definition.Execution;
import com.anthem.oss.nimbus.core.domain.definition.Executions;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig.Output;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class ExecutionOutputConfigHandler {

	public <T> List<ActionExecuteConfig<?, T>> loadClassConfigs(DomainConfig dc, ModelConfig<T> mConfig) {
		/* look for annotation with Execution.Input */
		Set<Execution.Output> outputs = new HashSet<>(AnnotationUtils.getRepeatableAnnotations(mConfig.getReferredClass(), Execution.Output.class, Executions.Outputs.class));
		Execution.Output.Default outputDefault = AnnotationUtils.findAnnotation(mConfig.getReferredClass(), Execution.Output.Default.class);
		
		if(outputDefault!=null && outputDefault.value()!=null) {
			outputs.add(outputDefault.value());
		} 	
		
		/* if none found, then leave it for defaults later */
		if(CollectionUtils.isEmpty(outputs)) return null;
		
		//Make distinct list of actions
		Set<Action> actions = new HashSet<>();
		outputs.forEach((out)->actions.addAll(Arrays.asList(out.value())));
		
		List<ActionExecuteConfig<?, T>> aecs = new ArrayList<>();
		for(Execution.Output o : outputs) {
			for(Action a : o.value()) {
				if(actions.contains(a)) {
					handleActionConfig(dc, mConfig, a, o);
					actions.remove(a);
				}
			}
		}

		return aecs;
	}
	
	public <T> ActionExecuteConfig<?, T> handleActionConfig(DomainConfig dc, ModelConfig<T> mConfig, Action a, Execution.Output o) {
		@SuppressWarnings("unchecked")
		ActionExecuteConfig<?, T> aec = (ActionExecuteConfig<?, T>) dc.templateActionConfigs().getOrAdd(a, ()->new ActionExecuteConfig<>(a));
		
		if(aec.hasOutput()) throw new InvalidConfigException("Found another config for: "+Execution.Output.class.getSimpleName()
				+" for Action: "+ a
				+" with referredClass: "+ aec.getOutput().getModel().getReferredClass()
				+" while loading config for current class: "+mConfig.getReferredClass());
		
		//TODO REMOVED CLONING WHILE STATIC CREATING>> GIVEN THAT STATE WOULD REQUIRE CLONING ANYWAYS: ModelConfig clonedModelConfig = mConfig.clone(o.ignore());
		aec.setOutput(new Output<>(mConfig, o.paginated(), o.fetch()));
		return aec;
	}
}
