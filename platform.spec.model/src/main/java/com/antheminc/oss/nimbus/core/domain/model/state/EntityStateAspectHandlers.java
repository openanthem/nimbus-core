/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state;

import java.util.function.BiFunction;

import com.antheminc.oss.nimbus.core.BeanResolverStrategy;
import com.antheminc.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.antheminc.oss.nimbus.core.spec.contract.event.EventListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter @AllArgsConstructor
public class EntityStateAspectHandlers {

	private EventListener eventListener;
	
	private BiFunction<Param<?>, String, Object> bpmEvaluator;
	
	private ValidatorProvider validatorProvider;
	
	private ParamStateGateway paramStateGateway;
	
	private BeanResolverStrategy beanResolver;
}
 