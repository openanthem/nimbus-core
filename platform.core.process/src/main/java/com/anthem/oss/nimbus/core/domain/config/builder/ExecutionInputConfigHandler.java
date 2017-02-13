/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.config.builder;

import java.lang.annotation.Annotation;
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
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig.Input;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class ExecutionInputConfigHandler {
	
	public void applyDefaults(DomainConfig dc) {
		
	}

	public <T> List<ActionExecuteConfig<T, ?>> loadClassConfigs(DomainConfig dc, ModelConfig<T> mConfig) {
		/* look for annotation with Execution.Input */
		Set<Execution.Input> inputs = new HashSet<>(AnnotationUtils.getRepeatableAnnotations(mConfig.getReferredClass(), Execution.Input.class, Executions.Inputs.class));
		Execution.Input.Default inputDefault = AnnotationUtils.findAnnotation(mConfig.getReferredClass(), Execution.Input.Default.class);
		
		if(inputDefault!=null && inputDefault.value()!=null) {
			inputs.add(inputDefault.value());
		} 	
		
		/* if none found, then leave it for defaults later */
		if(CollectionUtils.isEmpty(inputs)) return null;
		
		//Make distinct list of actions
		Set<Action> actions = new HashSet<>();
		inputs.forEach((in)->actions.addAll(Arrays.asList(in.value())));
		
		List<ActionExecuteConfig<T, ?>> aecs = new ArrayList<>();
		for(Execution.Input i : inputs) {
			for(Action a : i.value()) {
				if(actions.contains(a)) {
					handleActionConfig(dc, mConfig, a, i.ignore());
					actions.remove(a);
				}
			}
		}
		
		return aecs;
	}
	
	public <T> ActionExecuteConfig<T, ?> handleActionConfig(DomainConfig dc, ModelConfig<T> mConfig, Action a, Class<? extends Annotation> ignore[]) {
		@SuppressWarnings("unchecked")
		ActionExecuteConfig<T, ?> aec = (ActionExecuteConfig<T, ?>)dc.templateActionConfigs().getOrAdd(a, ()->new ActionExecuteConfig<>(a));
		
		if(aec.hasInput()) throw new InvalidConfigException("Found another config for: "+Execution.Input.class.getSimpleName()
				+" for Action: "+ a
				+" with referredClass: "+ aec.getInput().getModel().getReferredClass()
				+" while loading config for current class: "+mConfig.getReferredClass());
		
		//TODO REMOVED CLONING WHILE STATIC CREATING>> GIVEN THAT STATE WOULD REQUIRE CLONING ANYWAYS: ModelConfig clonedModelConfig = mConfig.clone(ignore);
		aec.setInput(new Input<>(mConfig));
		return aec;
	}
	
//	protected <T> ModelConfig<T> loadFieldConfigs(Class<?> clazz, DomainConfig dc) {
//		if(log.isTraceEnabled()) {
//			log.trace("Looking for field annotated with: "+Id.class);
//		}
//		Field idField = findFieldByAnnotation(clazz, Id.class);
//		
//		return null;
//	}
	
//	private Field findFieldByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationType) {
//		if(clazz==null) return null;
//		
//		Field all[] = FieldUtils.getAllFields(clazz);
//		if(ArrayUtils.isEmpty(all)) return null;
//		
//		for(Field f : all) {
//			if(AnnotatedElementUtils.isAnnotated(f, annotationType)) {
//				if(log.isDebugEnabled()) {
//					log.debug("Found field: "+f+" in class: "+clazz+" with annotationType: "+annotationType);
//				}
//				return f;
//			}
//		}
//		return null;
//	}
}
