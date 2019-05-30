/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.model.state.builder.internal;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import javax.annotation.PostConstruct;

import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.bpm.BPMGateway;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.config.builder.DomainConfigBuilder;
import com.antheminc.oss.nimbus.domain.model.config.ModelConfig;
import com.antheminc.oss.nimbus.domain.model.config.ValidatorProvider;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.domain.model.state.EntityStateAspectHandlers;
import com.antheminc.oss.nimbus.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.domain.model.state.builder.EntityStateBuilder;
import com.antheminc.oss.nimbus.domain.model.state.builder.QuadModelBuilder;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.QuadScopedEventListener;
import com.antheminc.oss.nimbus.domain.model.state.event.listener.StateAndConfigEventListener;
import com.antheminc.oss.nimbus.domain.model.state.internal.ExecutionEntity;
import com.antheminc.oss.nimbus.domain.model.state.repo.ParamStateGateway;
import com.antheminc.oss.nimbus.support.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter
public class DefaultQuadModelBuilder implements QuadModelBuilder {

	private DomainConfigBuilder domainConfigApi;
	
	private EntityStateBuilder stateBuilder;
	
	private ValidatorProvider validatorProvider;
	
	private ParamStateGateway paramStateGateway;
	
	private List<StateAndConfigEventListener> paramEventListeners;

	private BPMGateway bpmGateway;
	
	private final BeanResolverStrategy beanResolver;
	
	private JustLogit logit = new JustLogit(DefaultQuadModelBuilder.class);
	
	private RestTemplate restTemplate = new RestTemplate();
	
	public DefaultQuadModelBuilder(BeanResolverStrategy beanResolver) {
		this.beanResolver = beanResolver;
	}
			
	@PostConstruct		
	public void init() {
		this.domainConfigApi = beanResolver.get(DomainConfigBuilder.class);
		this.stateBuilder = beanResolver.get(EntityStateBuilder.class);
		this.validatorProvider = beanResolver.get(ValidatorProvider.class);
		this.paramStateGateway = beanResolver.get(ParamStateGateway.class);
		this.bpmGateway = beanResolver.get(BPMGateway.class);
		
		setParamEventListeners(new LinkedList<>());
		
		Collection<StateAndConfigEventListener> publishers = getBeanResolver().getMultiple(StateAndConfigEventListener.class);
		publishers.forEach(getParamEventListeners()::add);
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

		ExecutionEntity<V, C>.ExModel execModel = getStateBuilder().buildExec(cmd.createRootDomainCommand(), createAspectHandlers(), eState, exConfig);
		
		return build(execModel);
	}
	
	private <V, C> QuadModel<V, C> build(ExecutionEntity<V, C>.ExModel execModel) {
		QuadModel<V, C> quadModel = new QuadModel<>(execModel);
		return quadModel;
	}
	
	private <V, C> ExecutionEntity.ExConfig<V, C> buildExecConfig(Command cmd) {
		ModelConfig<?> modelConfig = Optional.ofNullable(getDomainConfigApi().getRootDomain(cmd.getRootDomainAlias()))
										.orElseThrow(()->new InvalidConfigException("Root Domain ModelConfig not found for : "+cmd.getRootDomainAlias()));
		
		
		// if mapped set domain config to view, other use it as core
		ModelConfig<V> viewConfig = modelConfig.isMapped() ? (ModelConfig<V>)modelConfig : null;
		ModelConfig<C> coreConfig = modelConfig.isMapped() ? (ModelConfig<C>)modelConfig.findIfMapped().getMapsToConfig() : (ModelConfig<C>)modelConfig;
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>(coreConfig, viewConfig, null);
		return exConfig;
	}
	
	private EntityStateAspectHandlers createAspectHandlers() {
		QuadScopedEventListener qEventListener = new QuadScopedEventListener(getParamEventListeners());
		
		BiFunction<Param<?>, String, Object> bpmEvaluator = (p, pid) -> getBpmGateway().continueBusinessProcessExecution(p, pid);
		return new EntityStateAspectHandlers(qEventListener, bpmEvaluator, getValidatorProvider(), getParamStateGateway(), beanResolver);
	}
}
