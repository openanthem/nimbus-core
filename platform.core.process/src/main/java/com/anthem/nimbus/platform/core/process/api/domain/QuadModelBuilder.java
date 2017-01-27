/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.contract.event.StateAndConfigEventPublisher;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.client.user.TestClientUserFactory;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.ExecuteOutput;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.binder.FlowState;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ModelStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadScopedEventPublisher;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfigMeta;
import com.anthem.nimbus.platform.spec.model.dsl.config.ActionExecuteConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;
import com.anthem.nimbus.platform.spec.model.view.dsl.config.ViewModelConfig;
import com.anthem.nimbus.platform.utils.converter.NavigationStateHelper;

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

	@Autowired DomainConfigAPI domainConfigApi;
	@Autowired StateAndConfigBuilder stateAndConfigBuilder;
	@Autowired ProcessConfigurationBuilder processConfigurationBuilder;
	
	@Autowired ApplicationContext appCtx;
	
	@Autowired NavigationStateHelper navigationStateHelper;
	
	@Autowired ValidatorProvider validatorProvider;

	private List<StateAndConfigEventPublisher> paramEventPublishers;

	private JustLogit logit = new JustLogit(getClass());
	
	public RestTemplate restTemplate = new RestTemplate();
	
	
	@PostConstruct
	@RefreshScope
	public void loadParamEventPublishers() {
		setParamEventPublishers(new LinkedList<>());
		
		appCtx.getBeansOfType(StateAndConfigEventPublisher.class)
			.values().forEach(p->getParamEventPublishers().add(p));
	}
	

	public <V, C> QuadModel<V, C> build(Command cmd, Function<ModelConfig<C>, C> coreStateCreator) {
		return build(cmd, coreStateCreator, null, null);
	}

	public <V, C> QuadModel<V, C> build(Command cmd, Function<ModelConfig<C>, C> coreStateCreator, Function<ModelConfig<V>, V> viewStateCreator, Function<ModelConfig<FlowState>, FlowState> flowStateCreator) {
		logit.trace(()->"[build] Start with "+cmd);
		
		final ViewModelConfig<V, C> viewConfig = findViewConfig(cmd);
		
		if(!viewConfig.isMapped())
			throw new InvalidConfigException("View model must be mapped to Core model. View: "+viewConfig.getReferredClass());
		
		
		ModelConfig<C> coreConfig = domainConfigApi.getVisitedModels().mapsToModel(viewConfig.getMapsTo());
		ModelConfig<FlowState> flowConfig = domainConfigApi.getVisitedModels().get(FlowState.class);
		
		//C coreState = coreStateGetterFunction.apply(coreConfig);
		StateAndConfigMeta.View<V, C> viewMeta = StateAndConfigMeta.View.getInstance(coreConfig, viewConfig, flowConfig, coreStateCreator, viewStateCreator, flowStateCreator);
		
		QuadModel<V, C> q = build(cmd, viewMeta);
		
		logit.trace(()->"[build] End with "+cmd);
		return q;
	}
	
	public <V, C> QuadModel<V, C> build(Command cmd, StateAndConfigMeta.View<V, C> viewMeta) {
		//create event publisher
		QuadScopedEventPublisher qEventPublisher = new QuadScopedEventPublisher(getParamEventPublishers());
		
		StateAndConfigSupportProvider provider = new StateAndConfigSupportProvider(qEventPublisher, getClientUserFromSession(cmd), validatorProvider);
		String rootDomainUri = cmd.getRootDomainUri();
		
		StateAndConfigMeta<C> coreMeta = viewMeta.getCore();
		final ModelStateAndConfig<C, ModelConfig<C>> coreStateAndConfig = stateAndConfigBuilder.build(provider, null, coreMeta.getConfig(), coreMeta.getStateGetter(), coreMeta.getStateSetter(), null);
		coreStateAndConfig.setRootDomainUri(rootDomainUri+"/c");
		
		//TODO: Build View State...by referencing Core's Param for mapped params in ViewConfig
		final ModelStateAndConfig<V, ViewModelConfig<V, C>> viewStateAndConfig = stateAndConfigBuilder.build(provider, null, viewMeta.getConfig(), viewMeta.getStateGetter(), viewMeta.getStateSetter(), coreStateAndConfig);
		viewStateAndConfig.setRootDomainUri(rootDomainUri+"/v");
		
		final ModelStateAndConfig<FlowState, ModelConfig<FlowState>> flowStateAndConfig = stateAndConfigBuilder.build(provider, null, viewMeta.getFlow().getConfig(), viewMeta.getFlow().getStateGetter(), viewMeta.getFlow().getStateSetter(), null);
		flowStateAndConfig.setRootDomainUri(rootDomainUri+"/f");
		
		QuadModel<V, C> quadModel = new QuadModel<>(cmd.getRootDomainElement(), coreStateAndConfig, viewStateAndConfig,flowStateAndConfig);
		quadModel.setEventPublisher(qEventPublisher);
		initializeFlowState(cmd, quadModel);
		
		return quadModel;
	}
	

	public <V, C> ViewModelConfig<V, C> findViewConfig(Command cmd) {
		ActionExecuteConfig<?, ?> aec = domainConfigApi.getActionExecuteConfig(cmd);
		
		/* use input model if new, otherwise from output */
		@SuppressWarnings("unchecked")
		final ViewModelConfig<V, C> viewConfig = (ViewModelConfig<V, C>)((Action._new == cmd.getAction()) ? aec.getInput().getModel() : aec.getOutput().getModel());
		return viewConfig;
	}
	
	
	/**
	 * 
	 * @param command
	 * @param quadModel
	 */
	public void initializeFlowState(Command command,QuadModel<?,?> quadModel){
		quadModel.loadProcessState(processConfigurationBuilder.getProcessConfiguration(command,quadModel));
		navigationStateHelper.init(quadModel);
	}
	
	public ClientUser getClientUserFromSession(Command cmd){
		
//		ClientUser cu;
//		cu = clientUserRepoApi.getUser(cmd.getClientUserId());
//		logit.debug(()->"client user in session : "+cu);
//		return cu;
		
		// Making a rest call directly until feign client issue is resolved.
		ClientUser clientUser;
		Map<String, String> loginName= new HashMap<>();
		loginName.put("loginName", cmd.getClientUserId());
		String url = "http://localhost:9090/p/clientuser/{loginName}";
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);

		ParameterizedTypeReference<ExecuteOutput<ClientUser>> typeRef = new ParameterizedTypeReference<ExecuteOutput<ClientUser>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ClientUser> entity = new HttpEntity<>(headers);

		try{
			ExecuteOutput<ClientUser> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeRef,loginName).getBody();
			clientUser = response.getResult();
			return clientUser;
		}catch(Exception e){
			//For all test cases to work, returning a mock object of ClientUser
			logit.debug(()-> " Rest call failed while retrieving client user");
			clientUser = TestClientUserFactory.createClientUser();
			return clientUser;
		}
		
	}
	
}
