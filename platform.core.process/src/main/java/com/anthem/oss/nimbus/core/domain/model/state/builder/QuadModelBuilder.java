/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventListener;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.config.builder.DomainConfigBuilder;
import com.anthem.oss.nimbus.core.domain.model.config.ActionExecuteConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.config.internal.MappedDefaultModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.domain.model.state.StateBuilderSupport;
import com.anthem.oss.nimbus.core.domain.model.state.StateMeta;
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
@Component("default.quadModelBuilder")
@Getter @Setter
@RefreshScope
public class QuadModelBuilder {

	@Autowired DomainConfigBuilder domainConfigApi;
	@Autowired StateBuilder stateAndConfigBuilder;
	
	@Autowired ApplicationContext appCtx;
	
	@Autowired PageNavigationInitializer navigationStateHelper;
	
	@Autowired ValidatorProvider validatorProvider;
	
	@Qualifier("default.param.state.repository")
	@Autowired ParamStateGateway paramStateGateway;
	
	private List<StateAndConfigEventListener> paramEventListeners;

	private JustLogit logit = new JustLogit(getClass());
	
	public RestTemplate restTemplate = new RestTemplate();
	
	
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
		

		//create event publisher
		QuadScopedEventListener qEventListener = new QuadScopedEventListener(getParamEventListeners());
		
		StateBuilderSupport provider = new StateBuilderSupport(qEventListener, /*getClientUserFromSession(cmd),*/ validatorProvider, paramStateGateway);
		String rootDomainUri = cmd.getRootDomainUri();
		
		final ExecutionEntity<V, C>.ExModel execModelStateAndConfig = stateAndConfigBuilder.buildExec(cmd, provider, eState, exConfig);
		
		QuadModel<V, C> quadModel = new QuadModel<>(cmd.getRootDomainElement(), execModelStateAndConfig);
		//quadModel.setEventPublisher(qEventPublisher);
		//==initializeFlowState(cmd, quadModel);
		
		return quadModel;
	}
	
	public <V, C> QuadModel<V, C> build2(Command cmd, ExecutionEntity<V, C> eState) {
		final MappedDefaultModelConfig<V, C> viewConfig = findViewConfig(cmd);
		ModelConfig<C> coreConfig = (ModelConfig<C>)domainConfigApi.getVisitedModels().get(viewConfig.findIfMapped().getMapsTo().getReferredClass());
		ModelConfig<ProcessFlow> flowConfig = (ModelConfig<ProcessFlow>)domainConfigApi.getVisitedModels().get(ProcessFlow.class);
		
		
		StateMeta<C> coreMeta = new StateMeta<>(coreConfig, ()->eState.getCore(), c->eState.setCore(c));
		StateMeta.FlowMeta<C> flowMeta = new StateMeta.FlowMeta<>(coreMeta, flowConfig, ()->eState.getFlow(), f->eState.setFlow(f));
		StateMeta.View<V, C> viewMeta = new StateMeta.View<>(flowMeta, viewConfig, ()->eState.getView(), v->eState.setView(v));
		
		
		//create event publisher
		//QuadScopedEventPublisher qEventPublisher = new QuadScopedEventPublisher(getParamEventPublishers());
		
		StateBuilderSupport provider = new StateBuilderSupport(null, /*getClientUserFromSession(cmd),*/ validatorProvider, paramStateGateway);
		String rootDomainUri = cmd.getRootDomainUri();
		
		final ExecutionEntity<V, C>.ExModel execModelStateAndConfig = stateAndConfigBuilder.buildExec(cmd, provider, eState, viewMeta);
		
		QuadModel<V, C> quadModel = new QuadModel<>(cmd.getRootDomainElement(), execModelStateAndConfig);
		//quadModel.setEventPublisher(qEventPublisher);
		//==initializeFlowState(cmd, quadModel);
		
		return quadModel;
	}
	
//	public <V, C> QuadModel<V, C> build(Command cmd, StateAndConfigMeta.View<V, C> viewMeta) {
//		//create event publisher
//		QuadScopedEventPublisher qEventPublisher = new QuadScopedEventPublisher(getParamEventPublishers());
//		
//		StateAndConfigSupportProvider provider = new StateAndConfigSupportProvider(qEventPublisher, getClientUserFromSession(cmd), validatorProvider, paramStateGateway);
//		String rootDomainUri = cmd.getRootDomainUri();
//		
//		StateAndConfigMeta<C> coreMeta = viewMeta.getCore();
//		final ModelStateAndConfig<C> coreStateAndConfig = stateAndConfigBuilder.buildModel(provider, null, coreMeta.getConfig(), null);
//		coreStateAndConfig.setRootDomainUri(rootDomainUri+"/c");
//		
//		//TODO: Build View State...by referencing Core's Param for mapped params in ViewConfig
//		final ModelStateAndConfig<V> viewStateAndConfig = stateAndConfigBuilder.buildModel(provider, null, viewMeta.getConfig(), coreStateAndConfig);
//		viewStateAndConfig.setRootDomainUri(rootDomainUri+"/v");
//		
//		final ModelStateAndConfig<FlowState> flowStateAndConfig = stateAndConfigBuilder.buildModel(provider, null, viewMeta.getFlow().getConfig(), null);
//		flowStateAndConfig.setRootDomainUri(rootDomainUri+"/f");
//		
//		QuadModel<V, C> quadModel = new QuadModel<>(cmd.getRootDomainElement(), coreStateAndConfig, viewStateAndConfig,flowStateAndConfig);
//		quadModel.setEventPublisher(qEventPublisher);
//		initializeFlowState(cmd, quadModel);
//		
//		return quadModel;
//	}
	
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
	
	
	/**
	 * 
	 * @param command
	 * @param quadModel
	 */
	public void initializeFlowState(Command command,QuadModel<?,?> quadModel){
		//quadModel.loadProcessState(processConfigurationBuilder.getProcessConfiguration(command,quadModel));
		navigationStateHelper.init(quadModel);
	}
	
}
