/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository;
import com.anthem.nimbus.platform.spec.model.Findable;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUserAccessBehavior;
import com.anthem.nimbus.platform.spec.model.client.user.TestClientUserFactory;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.command.ValidationResult;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.exception.InvalidConfigException;
import com.anthem.nimbus.platform.spec.model.exception.UserNotAuthorizedException;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"type"})
public class ParamStateAndConfig<T> extends AbstractStateAndConfig<T, ParamConfig<T>>
		implements StateAndConfig.Param<T>, Findable<String>, Serializable {
	

	private static final long serialVersionUID = 1L;

	//@JsonIgnore final private ModelStateAndConfig<M, ?> parent;
	
	private TypeStateAndConfig type;
	
	private transient ValidationResult validationResult;
	
	@JsonIgnore private transient final ValidatorProvider validatorProvider;
	
	private static final Logger log = LoggerFactory.getLogger(ModelStateAndConfig.class);
	
	
	public ParamStateAndConfig(ModelStateAndConfig<?, ?> parent, ParamConfig<T> config, ParamStateRepository pRep, StateAndConfigSupportProvider provider) {
		
		super(parent, config, provider);
		this.validatorProvider = provider.getValidatorProvider();
		
		setGetter(() -> pRep._get(this)); 
		setSetter((s)-> pRep._set(this, s)); 
	}

	
	
	@Override
	public void initConfigState() {
		//visit cloned config available for this QuadState
		getConfig().visit(this, getProvider());
	}
	
	@JsonIgnore @Override
	public ModelStateAndConfig<?, ?> getParent() {
		return (ModelStateAndConfig<?, ?>)super.getParent();
	}
	
	/**
	 * 
	 */
	@JsonIgnore @Override
	public StateAndConfig.Model<?, ?> getRootParent() {
		if(getParent() != null) return getParent().getRootParent();
		
		/* if param is root, then it has to be of type nested */
		if(!getType().isNested())
			throw new InvalidConfigException("Found param as root which is not nested: "+ this);
		
		return getType().findIfNested();
	}
	
	
	@Override
	public boolean isMapped() {
		return false;
	}
	
	@JsonIgnore @Override
	public StateAndConfig.Param<T> getMapsTo() {
		return null;
	}
	
	/**
	 * 
	 */
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		if (ArrayUtils.isEmpty(pathArr))
			return (Param<P>) this;

		/* param is not leaf node */
		StateAndConfig.Model<?, ?> mp = getType().findIfNested();
		
		if (mp != null)
			return mp.findParamByPath(pathArr);

		/*
		 * if param is a leaf node and requested path has more children, then
		 * return null
		 */
		if (pathArr.length > 1)
			return null;

		/* param is leaf node */
		@SuppressWarnings("unchecked")
		Param<P> p = isFound(pathArr[0]) ? (Param<P>) this : null;
		return p;
	}
	
	
	@Override
	public boolean isFound(String by) {
		return getConfig().isFound(by);
	}
	
//	@Override
//	public void update(Observable o, Object arg) {
//		// TODO Auto-generated method stub
//		
//	}
	
	public <T, C extends ModelConfig<T>, P> ModelStateAndConfig<T, C> build(
			StateAndConfigSupportProvider provider, ParamStateAndConfig<?> parentState, C mConfig,
			Supplier<T> mGet, Consumer<T> mSet, ModelStateAndConfig<?, ?> mapsToStateAndConfig) {
		
		
		return null;
	}
	
	
	private transient T collectionProxyState;
	
	
	
	private static class _CollectionParamState<E> extends SimpleState<Collection<E>> {
		
		private Collection<ModelStateAndConfig<Object, ModelConfig<Object>>> collection;
		
		
		public _CollectionParamState(ParamStateAndConfig<?> parent, ParamConfig<Collection<E>> config,
				Supplier<Collection<E>> getter, Consumer<Collection<E>> setter,
				StateAndConfigSupportProvider provider) {
			
			
			super(parent, getter, setter, provider);
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	public void setState(T state) {
		ClientUser cu = this.getProvider().getClientUser();
		
		//delegate to model.setState if of type nested model
		if(getType().isNested()) {
			getType().findIfNested().setState(state);
			return;
		}
		
		// Store proxy in paramSAC, but let the internal AbstractState refer to
		// original state
		if (!isMapped() && getType().getCollection() != null) {
			Collection<Object> collectionState = (Collection<Object>) state;
			CollectionParamState cps = new CollectionParamState(collectionState);
			collectionProxyState = (T) cps.getProxy();

			ModelConfig<Object> mConfig = getConfig().getType().findIfNested().getModel();
			for (Object item : collectionState) {
				Supplier<Object> mGet = () -> item;
				Consumer<Object> mSet = (m) -> {
					collectionState.remove(item);
					collectionState.add(m);
				};

				ModelStateAndConfig<Object, ModelConfig<Object>> itemModelSAC = build(getProvider(), this,
						mConfig, mGet, mSet, null);
			}
		}
		Action a = setStateInternal(state);
		
		
		boolean hasAccess = true;
		if(a != null){
			ClientUserAccessBehavior clientUserAccessBehavior = cu.newBehaviorInstance(ClientUserAccessBehavior.class);			
			hasAccess = clientUserAccessBehavior.canUserPerformAction(getPath(),a.name());
		}
		
		if(hasAccess){		
			setStateAndNotifyObservers(state);
			emitEvent(this);
		}else{
			log.debug("User does not have access to set state on ["+getPath()+"] for action : ["+a.name()+"]");
			throw new UserNotAuthorizedException("User not authorized to set state on ["+getPath()+"] for action : ["+a.name()+"] " +this);
		}
			
	}
	
	
	
	/**
	 * 
	 */
	@Override
	public void validateAndSetState(T state) {
		validate(state);
		
		if(CollectionUtils.isNotEmpty(this.validationResult.getErrors())) {
			emitEvent(this);
	    	throw new ValidationException(this.validationResult);
	    } 
		else {
	    	setState(state);
	    }
	}
	
	/**
	 * 
	 * @param state
	 */
	private void validate(T state) {
		this.validationResult = new ValidationResult();
	    //TODO change the hard coded bean class and property with the respective getter methods once available
		if(isMapped()) { 
			//for view mapped param
			Class<?> mappedParamClass = getConfig().getReferredClass();
			String mappedParamCode = getConfig().getCode();
			
			//for core mapsTo param
			Class<?> mapsToParamClass = getMapsTo().getConfig().getReferredClass();
			String mapsToParamCode = getMapsTo().getConfig().getCode();
			
			this.validationResult.addValidationErrors(validatorProvider.getValidator().validateValue(mappedParamClass, mappedParamCode, state));
		    this.validationResult.addValidationErrors(validatorProvider.getValidator().validateValue(mapsToParamClass, mapsToParamCode, state)); 
			
		} 
		else {
			Class<?> paramClass = getConfig().getReferredClass();
			String paramCode = getConfig().getCode();
			
			this.validationResult.addValidationErrors(validatorProvider.getValidator().validateValue(paramClass, paramCode, state)); 
		} 
	}
	
}
