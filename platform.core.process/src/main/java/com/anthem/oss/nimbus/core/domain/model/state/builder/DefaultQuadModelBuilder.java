/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.spec.contract.event.BulkEventListener;
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
	
	private EntityStateBuilder stateBuilder;
	
	private ValidatorProvider validatorProvider;
	
	private ParamStateGateway paramStateGateway;
	
	private List<StateAndConfigEventListener> paramEventListeners;
	
	private BulkEventListener bulkEventListener;

	private final BeanResolverStrategy beanResolver;
	
	private JustLogit logit = new JustLogit(getClass());
	
	public RestTemplate restTemplate = new RestTemplate();
	
	public DefaultQuadModelBuilder(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
	}
			
	@PostConstruct		
	public void init() {
		this.domainConfigApi = beanResolver.get(DomainConfigBuilder.class);
		this.stateBuilder = beanResolver.get(EntityStateBuilder.class);
		this.validatorProvider = beanResolver.get(ValidatorProvider.class);
		this.paramStateGateway = beanResolver.get(ParamStateGateway.class);
	
		setParamEventListeners(new LinkedList<>());
		
		Collection<StateAndConfigEventListener> publishers = beanResolver.getMultiple(StateAndConfigEventListener.class);
		publishers.forEach(getParamEventListeners()::add);
		
		//bulkEventListener = beanResolver.get(BulkEventListener.class);
		
	}
	
	@Override
	public <V, C> QuadModel<V, C> build(Command cmd, V viewState, Param<C> coreParam) {
		ExecutionEntity.ExConfig<V, C> exConfig = buildExecConfig(cmd);

		ExecutionEntity<V, C>.ExModel execModel = stateBuilder.buildExec(cmd.createRootDomainCommand(), createAspectHandlers(), exConfig, viewState, coreParam);
		
		return build(execModel);
	}
	
	@Override
	public <V, C> QuadModel<V, C> build(Command cmd, ExecutionEntity<V, C> eState) {
		ExecutionEntity.ExConfig<V, C> exConfig = buildExecConfig(cmd);

		ExecutionEntity<V, C>.ExModel execModel = stateBuilder.buildExec(cmd.createRootDomainCommand(), createAspectHandlers(), eState, exConfig);
		
		return build(execModel);
	}
	
	private <V, C> QuadModel<V, C> build(ExecutionEntity<V, C>.ExModel execModel) {
		QuadModel<V, C> quadModel = new QuadModel<>(execModel);
		return quadModel;
	}
	
	private <V, C> ExecutionEntity.ExConfig<V, C> buildExecConfig(Command cmd) {
		ModelConfig<?> modelConfig = Optional.ofNullable(domainConfigApi.getRootDomain(cmd.getRootDomainAlias()))
										.orElseThrow(()->new InvalidConfigException("Root Domain ModelConfig not found for : "+cmd.getRootDomainAlias()));
		
		
		// if mapped set domain config to view, other use it as core
		ModelConfig<V> viewConfig = modelConfig.isMapped() ? (ModelConfig<V>)modelConfig : null;
		ModelConfig<C> coreConfig = modelConfig.isMapped() ? (ModelConfig<C>)modelConfig.findIfMapped().getMapsTo() : (ModelConfig<C>)modelConfig;
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>(coreConfig, viewConfig, null);
		return exConfig;
	}
	
	private EntityStateAspectHandlers createAspectHandlers() {
		QuadScopedEventListener qEventListener = new QuadScopedEventListener(getParamEventListeners());
		
		return new EntityStateAspectHandlers(qEventListener, bulkEventListener, validatorProvider, paramStateGateway);
	}
}
