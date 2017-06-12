/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
@RefreshScope
public class DefaultQuadModelBuilder implements QuadModelBuilder {

	private DomainConfigBuilder domainConfigApi;
	
	private EntityStateBuilder stateAndConfigBuilder;
	
	private ValidatorProvider validatorProvider;
	
	private ParamStateGateway paramStateGateway;
	
	private List<StateAndConfigEventListener> paramEventListeners;

	private final BeanResolverStrategy beanResolver;
	
	private JustLogit logit = new JustLogit(getClass());
	
	public RestTemplate restTemplate = new RestTemplate();
	
	public DefaultQuadModelBuilder(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
	}
			
	@PostConstruct		
	public void init() {
		this.domainConfigApi = beanResolver.get(DomainConfigBuilder.class);
		this.stateAndConfigBuilder = beanResolver.get(EntityStateBuilder.class);
		this.validatorProvider = beanResolver.get(ValidatorProvider.class);
		this.paramStateGateway = beanResolver.get(ParamStateGateway.class);
	
		setParamEventListeners(new LinkedList<>());
		
		Collection<StateAndConfigEventListener> publishers = beanResolver.getMultiple(StateAndConfigEventListener.class);
		publishers.forEach(getParamEventListeners()::add);
	}
	
	
	public <V, C> QuadModel<V, C> build(Command cmd, ExecutionEntity<V, C> eState) {
		ModelConfig<?> modelConfig = domainConfigApi.getRootDomain(cmd.getRootDomainAlias());
		
		// if mapped set domain config to view, other use it as core
		ModelConfig<V> viewConfig = modelConfig.isMapped() ? (ModelConfig<V>)modelConfig : null;
		ModelConfig<C> coreConfig = modelConfig.isMapped() ? (ModelConfig<C>)modelConfig.findIfMapped().getMapsTo() : (ModelConfig<C>)modelConfig;
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>(coreConfig, viewConfig, null);
		

		//create event listener
		QuadScopedEventListener qEventListener = new QuadScopedEventListener(getParamEventListeners());
		
		EntityStateAspectHandlers provider = new EntityStateAspectHandlers(qEventListener, validatorProvider, paramStateGateway);
		
		final ExecutionEntity<V, C>.ExModel execModelStateAndConfig = stateAndConfigBuilder.buildExec(cmd.createRootDomainCommand(), provider, eState, exConfig);
		
		QuadModel<V, C> quadModel = new QuadModel<>(execModelStateAndConfig);
		//quadModel.setEventPublisher(qEventPublisher);
		//==initializeFlowState(cmd, quadModel);
		
		return quadModel;
	}
	
}
