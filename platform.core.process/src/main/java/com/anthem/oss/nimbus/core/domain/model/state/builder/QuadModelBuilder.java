/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.internal.ExecutionEntity;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.entity.process.ProcessFlow;
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
public class QuadModelBuilder {

	DomainConfigBuilder domainConfigApi;
	
	EntityStateBuilder stateAndConfigBuilder;
	
	ApplicationContext appCtx;
	
	PageNavigationInitializer navigationStateHelper;
	
	ValidatorProvider validatorProvider;
	
	ParamStateGateway paramStateGateway;
	
	private List<StateAndConfigEventListener> paramEventListeners;

	private JustLogit logit = new JustLogit(getClass());
	
	public RestTemplate restTemplate = new RestTemplate();
	
	public QuadModelBuilder(DomainConfigBuilder domainConfigApi, EntityStateBuilder stateAndConfigBuilder,
			ApplicationContext appCtx, PageNavigationInitializer navigationStateHelper,
			ValidatorProvider validatorProvider, ParamStateGateway paramStateGateway) {
		this.domainConfigApi = domainConfigApi;
		this.stateAndConfigBuilder = stateAndConfigBuilder;
		this.appCtx = appCtx;
		this.navigationStateHelper = navigationStateHelper;
		this.validatorProvider = validatorProvider;
		this.paramStateGateway = paramStateGateway;
	}
	
	@PostConstruct
	@RefreshScope
	public void loadParamEventPublishers() {
		setParamEventListeners(new LinkedList<>());
		
		Collection<StateAndConfigEventListener> publishers = appCtx.getBeansOfType(StateAndConfigEventListener.class)
				.values();
		publishers.forEach(p->getParamEventListeners().add(p));
	}
	
	
	public <V, C> QuadModel<V, C> build(Command cmd) {
		ExecutionEntity<V, C> eState = new ExecutionEntity<>();
		return build(cmd, eState);
	}
	
	public <V, C> QuadModel<V, C> build(Command cmd, ExecutionEntity<V, C> eState) {
		final ModelConfig<?> modelConfig = findModelConfig(cmd);
		
		// if mapped set domain config to view, other use it as core
		ModelConfig<V> viewConfig = modelConfig.isMapped() ? (ModelConfig<V>)modelConfig : null;
		ModelConfig<C> coreConfig = modelConfig.isMapped() ? (ModelConfig<C>)modelConfig.findIfMapped().getMapsTo() : (ModelConfig<C>)modelConfig;
		ModelConfig<ProcessFlow> flowConfig = (ModelConfig<ProcessFlow>)domainConfigApi.getVisitedModels().get(ProcessFlow.class);
		
		ExecutionEntity.ExConfig<V, C> exConfig = new ExecutionEntity.ExConfig<>(coreConfig, viewConfig, flowConfig);
		

		//create event listener
		QuadScopedEventListener qEventListener = new QuadScopedEventListener(getParamEventListeners());
		
		EntityStateAspectHandlers provider = new EntityStateAspectHandlers(qEventListener, validatorProvider, paramStateGateway);
		
		final ExecutionEntity<V, C>.ExModel execModelStateAndConfig = stateAndConfigBuilder.buildExec(cmd.createRootDomainCommand(), provider, eState, exConfig);
		
		QuadModel<V, C> quadModel = new QuadModel<>(execModelStateAndConfig);
		//quadModel.setEventPublisher(qEventPublisher);
		//==initializeFlowState(cmd, quadModel);
		
		return quadModel;
	}
	
	
	private ModelConfig<?> findModelConfig(Command cmd) {
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		
		/* use input model if new, otherwise from output */
		final ModelConfig<?> mConfig = (Action._new == cmd.getAction() ? aec.getInput().getModel() : aec.getOutput().getModel());
		return mConfig;
	}
	
	public <V, C> MappedDefaultModelConfig<V, C> findViewConfig(Command cmd) {
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		
		/* use input model if new, otherwise from output */
		@SuppressWarnings("unchecked")
		final MappedDefaultModelConfig<V, C> viewConfig = (MappedDefaultModelConfig<V, C>)((Action._new == cmd.getAction()) ? aec.getInput().getModel() : aec.getOutput().getModel());
		return viewConfig;
	}
	
}
