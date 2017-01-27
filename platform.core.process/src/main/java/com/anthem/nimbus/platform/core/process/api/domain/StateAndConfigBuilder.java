/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.core.process.api.ProcessBeanResolver;
import com.anthem.nimbus.platform.spec.contract.process.ProcessGateway;
import com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository;
import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;
import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandBuilder;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.command.MultiExecuteOutput;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Behavior;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;
import com.anthem.nimbus.platform.spec.model.dsl.Model.Param.Values.Source;
import com.anthem.nimbus.platform.spec.model.dsl.binder.AbstractStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.nimbus.platform.spec.model.dsl.binder.MappedParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ModelStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ParamStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.dsl.binder.TypeStateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.CoreParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamType;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamValue;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;

/**
 * @author Soham Chakravarti
 *
 */
@Component
public class StateAndConfigBuilder {

	@Autowired ValidatorProvider validatorProvider;
	
	@Qualifier("default.param.state.repository")
	@Autowired ParamStateRepository pRep;

	@Autowired
	@Qualifier("default.processGateway")
	ProcessGateway processGateway;
	
	private JustLogit logit = new JustLogit(getClass());
	
	public <T, C extends ModelConfig<T>> ModelStateAndConfig<T, C> build(StateAndConfigSupportProvider provider, ParamStateAndConfig<?> parentState, C mConfig, ModelStateAndConfig<?, ?> mapsToSAC) {
		Holder<T> mStateHolder = new Holder<>(ModelsTemplate.newInstance(mConfig.getReferredClass()));
		return build(provider, parentState, mConfig, ()->mStateHolder.getState(), (s)->mStateHolder.setState(s), mapsToSAC);
	}
	
	public <T, C extends ModelConfig<T>, P> ModelStateAndConfig<T, C> build(StateAndConfigSupportProvider provider, ParamStateAndConfig<?> parentState, C mConfig, Supplier<T> mGet, Consumer<T> mSet, ModelStateAndConfig<?, ?> mapsToSAC) {
		return buildInternal(provider, parentState, mConfig, mGet, mSet, mapsToSAC);
	}
	
	/*
	 * TODO make the ModelStateAndConfig creation extensible based on mConfig.referredClass, default to ModelAndStateConfig.class
	 * TODO same for ParamSateAndConfig
	 */
	protected <T, C extends ModelConfig<T>, P> ModelStateAndConfig<T, C> buildInternal(
			StateAndConfigSupportProvider provider, ParamStateAndConfig<?> parentState, 
			C mConfig, Supplier<T> mGet, Consumer<T> mSet, StateAndConfig.Model<?, ?> mapsToSAC) {
		
		if(mConfig==null) return null;

		/* if model & param are mapped, then  mapsToSAC must not be null */
		if(mConfig.isMapped() && mapsToSAC==null) 
			throw new InvalidConfigException("Model class: "+mConfig.getReferredClass()+" is mapped: "+mConfig.getMapsTo()
						+" but mapsToSAC is not supplied. Was this model's config loaded first as part of core?");
		
		ModelStateAndConfig<T, C> mState = new ModelStateAndConfig<>(parentState, mConfig, mGet, mSet, mapsToSAC, provider);
		mState.initConfigState();
		
		if(mConfig.getParams()==null) return mState;
		
		final String parentPath = StringUtils.trimToEmpty(parentState==null ? "" : parentState.getPath());
		
		/* iterate through config params and create state instances in the same order */
		for(ParamConfig<?> mpConfigRawType : mConfig.getParams()) {
			@SuppressWarnings("unchecked")
			final ParamConfig<P> mpConfig = (ParamConfig<P>)mpConfigRawType;
			
			final ParamStateAndConfig<P> mpState = createParam(provider, mConfig, mState, mapsToSAC, mpConfig);
			logit.debug(()->"[buildInternal] mpStatePath: "+ mpState.getPath());
			
			//TODO 10 Implement using: Observer, Observable at State & AbstractState level
			//TODO DONE 11 Create MappedParamStateAndConfig class which can have mapsToParam as composite
			//TODO 12 Register mappedParam as Observer in mapsToParam
			//TODO 13 Call setChanged() when state changes in parameter and call notifyObservers(state)

			 
			/* add param state to model state in same order */
			mState.templateParams().add(mpState);
			
			//handle param type
			TypeStateAndConfig type = handleParamType(provider, mpState.getPath(), mpConfig, mState, mpState, mapsToSAC);
			mpState.setType(type);
		}
		
		return mState;
	}
	
	
	/**
	 * Create Param SAC based on whether the referred ParamConfig is mapped or not.
	 */
	protected <P> ParamStateAndConfig<P> createParam(StateAndConfigSupportProvider provider, ModelConfig<?> mConfig, ModelStateAndConfig<?, ?> mState, StateAndConfig.Model<?, ?> mapsToSAC, ParamConfig<P> mpConfig) {
		/* if param is mapped then model must be mapped */
		if(mpConfig.isMapped() && !mConfig.isMapped())
				throw new InvalidConfigException("Model class: "+mConfig.getReferredClass()+" is must mapped to load mapped param: "+mpConfig.getMapsTo());
		
		
		/* find mapped param's state */
		final Param<P> mpMapsToParam = findMapsToParam(mpConfig, mapsToSAC);
		
		/* create state params with callbacks for getter & setter */
		final ParamStateAndConfig<P> mpState;
		if(mpMapsToParam != null) {
			mpState = new MappedParamStateAndConfig<P>(
					mpMapsToParam,
					mState,
					mpConfig, 
					pRep,
					provider);	
			
		} else {
			mpState = new ParamStateAndConfig<P>(
					mState,
					mpConfig, 
					pRep,
					provider);	
		}
		mpState.initConfigState();
		
		
		/* set param path */
		final String parentPath = StringUtils.trimToEmpty(mState.getParent()==null ? "" : mState.getParent().getPath());
		String mpStatePath = parentPath + Constants.SEPARATOR_URI.code + mpConfig.getCode();
		mpState.setPath(mpStatePath);
		
		/* setting param values if applicable */
		createParamValues(mpConfig, mpStatePath);
		
		return mpState;
	}
	
	protected <P> TypeStateAndConfig handleParamType(StateAndConfigSupportProvider provider, 
			String mpStatePath, ParamConfig<P> mpConfig, ModelStateAndConfig<?, ?> mState, ParamStateAndConfig<P> mpState, StateAndConfig.Model<?, ?> mapsToSAC) {
		//check if param is not nested (i.e., not a recognized platform model)
		
		//TODO handle collection. for now skipping collections
		/*if(pConfig.getType().getCollection()!=null) {
			//--return new TypeStateAndConfig(pConfig.getType());
			@SuppressWarnings("unchecked")
			ParamType.Nested<P> nmType = ((ParamType.Nested<P>)pConfig.getType());
			ModelConfig<P> nmConfig = nmType.getModel();
			
			//buildInternal(eventPublisher, pState, nmConfig, pState.getGetter(), pState.getSetter(), null);
			ModelStateAndConfig<P, ?> nmState = new ModelStateAndConfig<>(pState, nmConfig, null, null, null, eventPublisher, validatorProvider);
			//ModelStateAndConfig<P, ?> nmState = buildInternal(eventPublisher, pState, nmConfig, pState.getGetter(), pState.getSetter(), null);
			TypeStateAndConfig.Nested<P> ntState = new TypeStateAndConfig.Nested<>(nmType, nmState);
			return ntState;
		}*/
		
		if(mpConfig.getType() instanceof ParamType.Nested) {
			//handle nested model
			@SuppressWarnings("unchecked")
			ParamType.Nested<P> mpNmType = ((ParamType.Nested<P>)mpConfig.getType());
			
			ModelConfig<P> mpNmConfig = mpNmType.getModel();
			
			/* determine mapsTo model SAC for param's nested model */
			final StateAndConfig.Model<?, ?> mpNmMapsToSAC = resolveNmMapsToSAC(provider, mState, mpState, mpConfig, mpNmConfig, mapsToSAC);
			
			/* create nested model SAC */
			ModelStateAndConfig<P, ?> nmState = buildInternal(provider, mpState, mpNmConfig, mpState.getGetter(), mpState.getSetter(), mpNmMapsToSAC);
			
			/* set model path */
			nmState.setPath(mpStatePath);
			
			TypeStateAndConfig.Nested<P> ntState = new TypeStateAndConfig.Nested<>(mpNmType, nmState);
			return ntState;
			
		} else {
			
			return new TypeStateAndConfig(mpConfig.getType());
		}
	}
	
	private <B, P> StateAndConfig.Model<?, ?> resolveNmMapsToSAC(StateAndConfigSupportProvider provider, ModelStateAndConfig<?, ?> mState, 
			ParamStateAndConfig<P> mpState, ParamConfig<P> mpConfig, ModelConfig<P> mpNmConfig, StateAndConfig.Model<?, ?> mapsToSAC) {
		final StateAndConfig.Model<?, ?> mpNmMapsToSAC;
		
		/* handle explicitly mapped model via @Path on param, otherwise if param's nested model is mapped - then, it would be treated as de-linked backing modelSAC  */
		if(mpConfig.isImplicitlyMapped()) {
			
			/* use path value, otherwise is considered same as parent's */
			String mpMapsToNmPath = (mpConfig.isMapped()) ? mpConfig.getMapsTo().value() : null;
			
			if(StringUtils.trimToNull(mpMapsToNmPath) != null) {
				mpNmMapsToSAC = mapsToSAC.findParamByPath(mpMapsToNmPath).getType().findIfNested();
			} else {
				mpNmMapsToSAC = mapsToSAC;
			}
			
			/* check config that the mapsTo model matches to determined mapsTo */
			if(mpNmConfig.isMapped() 
					&& mpNmConfig.getMapsTo().value() != mpNmMapsToSAC.getConfig().getReferredClass()) {
				
				throw new InvalidConfigException("Determined mapsToSAC doesn't match with @MapsTo defined on nested model class. "
						+ "Possibly the @MapsTo on Param is incorrect or @MapsTo on NestedModel is incorrect."
						+ " Determined mpNmConfig: "+ mpNmMapsToSAC.getConfig().getReferredClass()+" -> Found configured: "+ mpNmConfig.getMapsTo().value());
			}
			
		} else if(mpNmConfig.isMapped()) {/* backing model scenario */
			//create new SAC for backing core model
			ModelConfig<B> mpNmBackingConfig = mpNmConfig.resolveBackingConfig();
			
			Holder<B> mpNmBackingStateHolder = new Holder<B>();
			
			Supplier<B> mpNmBackingStateGet = ()->mpNmBackingStateHolder.getState();
			Consumer<B> mpNmBackingStateSet = new Consumer<B>() {
				@SuppressWarnings("unchecked")
				@Override
				public void accept(B source) {
					StateAndConfig.Model<P, ? extends ModelConfig<P>> nmSAC = mpState.getType().findIfNested();
					P targetMapppedTo = nmSAC.getCreator().get();
					
					
					nmSAC.getParams().forEach((nmSACParam)->{
						if(nmSACParam.isMapped()) {
							//TODO implement MapsTo.Path logic with nested traversal eg:"/name/firstName"
							String sourcePath = nmSACParam.getConfig().getCode();
							PropertyDescriptor mpPropDescriptor = BeanUtils.getPropertyDescriptor(source.getClass(), sourcePath);
							
							Object sourceParamVal = AbstractStateAndConfig.read(mpPropDescriptor, ()->source);
							
							nmSACParam.getProvider().getEventPublisher().apply(SuppressMode.ECHO);
							((Param<Object>)nmSACParam).setState(sourceParamVal);
							nmSACParam.getProvider().getEventPublisher().apply(null);
						}
					});
					
					
					mpNmBackingStateHolder.setState(source);
				}
			};
			
			mpNmMapsToSAC = buildInternal(provider, mpState, mpNmBackingConfig, mpNmBackingStateGet, mpNmBackingStateSet, null);
			
		} else {/* no backing model */
			
			mpNmMapsToSAC = null;
		}
		
		return mpNmMapsToSAC;
	}
	
	private <T> Param<T> findMapsToParam(ParamConfig<T> pConfig, StateAndConfig.Model<?, ?> mapsToStateAndConfig) {
		/* return null if param is not mapped */
		if(!pConfig.isMapped()) return null;
		
		String configuredPath = (pConfig.getMapsTo()==null) ? null : StringUtils.trimToNull(pConfig.getMapsTo().value());
		String resolvedPath = (configuredPath==null) ? pConfig.getCode() : configuredPath;
		
		Param<T> mapsToParam = mapsToStateAndConfig.findParamByPath(resolvedPath);
			
		if(mapsToParam==null) throw new InvalidConfigException("Param is mapped but no param found on mapped model. "
				+ "Mapped Param: "+pConfig.getCode()+" with mapsTo: "+pConfig.getMapsTo()+" mapped model: "+mapsToStateAndConfig.getPath());
			
		return mapsToParam;	
	}
	
private <P> void createParamValues(ParamConfig<P> mpConfig, String paramPath) {
		
		if(StringUtils.isNotBlank(mpConfig.getValuesUrl())) {
			//Source src = ProcessBeanResolver.getBean(mpConfig.getValuesUrl(),Source.class);
			//List<ParamValue> values = src.getValues(mpState.getPath());
			
			CoreParamConfig<P> coreConfig = (CoreParamConfig<P>) mpConfig;
			
//			String[] uriWithPayload = mpConfig.getValuesUrl().split(Constants.CODE_VALUE_CONFIG_DELIMITER.code);
//			Command cmd = CommandBuilder.withUri(Constants.PARAM_VALUES_URI_PREFIX.code+uriWithPayload[0]+Constants.PARAM_VALUES_URI_SUFFIX.code).getCommand();
//			cmd.setAction(Action._search);
//			cmd.templateBehaviors().add(Behavior.$execute);
//			CommandMessage cmdMsg = new CommandMessage();
//			cmdMsg.setCommand(cmd);
//			if(uriWithPayload.length > 1) {
//				cmdMsg.setRawPayload(uriWithPayload[1]); // domain model lookup
//			}
//			else{
//				cmdMsg.setRawPayload(paramPath); // static code value lookup
//			}
//			
//			MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
//			List<ParamValue> values = output.getSingleResult();
			
			//if(CollectionUtils.isNotEmpty(values)) {
				//mpConfig.setValues(values);
			if(coreConfig.getValuesGetter() == null) {
				coreConfig.setValuesGetter(() -> {
					String[] uriWithPayload = coreConfig.getValuesUrl().split(Constants.CODE_VALUE_CONFIG_DELIMITER.code);
					Command cmd = CommandBuilder.withUri(Constants.PARAM_VALUES_URI_PREFIX.code+uriWithPayload[0]+Constants.PARAM_VALUES_URI_SUFFIX.code).getCommand();
					cmd.setAction(Action._search);
					cmd.templateBehaviors().add(Behavior.$execute);
					CommandMessage cmdMsg = new CommandMessage();
					cmdMsg.setCommand(cmd);
					if(uriWithPayload.length > 1) {
						cmdMsg.setRawPayload(uriWithPayload[1]); // domain model lookup
					}
					else{
						cmdMsg.setRawPayload(paramPath); // static code value lookup
					}
					
					MultiExecuteOutput output = (MultiExecuteOutput) processGateway.startProcess(cmdMsg);
					return output.getSingleResult();
				});
			}
		}
	}

}
