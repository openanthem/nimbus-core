/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUserAccessBehavior;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.command.ValidationResult;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.exception.UserNotAuthorizedException;
import com.anthem.nimbus.platform.spec.model.util.CollectionsTemplate;
import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @Setter @ToString(callSuper=true, of={"params"})
public class ModelStateAndConfig<T, C extends ModelConfig<T>> extends AbstractStateAndConfig<T, C>
		implements StateAndConfig.Model<T, C>, Observer, Serializable {
	
    private static final long serialVersionUID = 1L;

	@JsonIgnore transient protected Supplier<? extends T> creator;
	
	private List<StateAndConfig.Param<? extends Object>> params;
	
    @JsonIgnore transient protected final StateAndConfig.Model<?, ?> backingCoreModel;
	
	private transient ValidationResult validationResult;
	
	private static final Logger log = LoggerFactory.getLogger(ModelStateAndConfig.class);
	
	@JsonIgnore 
	private ExecutionStateTree executionStateTree;
	
	
	public ModelStateAndConfig(ParamStateAndConfig<?> parentMState, C config, Supplier<T> getter, Consumer<T> setter,
			StateAndConfig.Model<?, ?> backingCoreModel, StateAndConfigSupportProvider provider) {
		
		
		super(parentMState, config, provider);
		setGetter(getter);
		setSetter(setter);
		
		//this.creator = ()-> (getter.get()==null) ? (T)ModelsTemplate.newInstance(config.getReferredClass()) : getter.get();
		this.creator = new Supplier<T>() {
			@Override
			public T get() {
				T existingState = getter.get();
				if(existingState != null) {
					return existingState;
				}
				
				T newState = (T) ModelsTemplate.newInstance(config.getReferredClass());
				setter.accept(newState);
				
				//notify registered observers
				setChanged();
				notifyObservers();
				
				return newState;
			}
		};
		
		//notify (view) mappedFrom model with backingCore is created
		this.backingCoreModel = backingCoreModel;
		if(backingCoreModel != null) {
			ModelStateAndConfig<?, ?> bcm = (ModelStateAndConfig<?, ?>) backingCoreModel;
			bcm.addObserver(this);
		}
	}
	
	
	@Override
	public void update(Observable o, Object arg) {
		this.creator.get();
		
		emitEvent(getParent());
	}
	
	@Transient @JsonIgnore @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private final transient CollectionsTemplate<List<StateAndConfig.Param<? extends Object>>, StateAndConfig.Param<? extends Object>> templateParams = new CollectionsTemplate<>(
			() -> getParams(), (p) -> setParams(p), () -> new LinkedList<>());

	@JsonIgnore @Override
	public CollectionsTemplate<List<StateAndConfig.Param<?>>, StateAndConfig.Param<?>> templateParams() {
		return templateParams;
	}
	
	
	@Override
	public void initConfigState() {
		//visit cloned config available for this QuadState
		getConfig().visit((ParamStateAndConfig<?>)getParent(), getProvider());
	}
	
	@JsonIgnore @Override
	public ParamStateAndConfig<?> getParent() {
		return (ParamStateAndConfig<?>)super.getParent();
	}
	
	@JsonIgnore @Override
	public StateAndConfig.Model<?, ?> getRootParent() {
		if(getParent() == null) return this;
		
		return getParent().getRootParent();
	}
	
	/**
	 * 
	 */
	@Override
	public <P> Param<P> findParamByPath(String[] pathArr) {
		if(ArrayUtils.isEmpty(pathArr)) {//return null; 
			return (getParent() != null) ? getParent().findParamByPath(getPath()) : null;
		}
		
		String nPath = pathArr[0];
		@SuppressWarnings("unchecked")
		Param<P> p = (Param<P>)templateParams().find(nPath);
		if(p == null) return null;
		
		if(pathArr.length == 1) { //last one
			return p;
		}
		
		return p.findParamByPath(ArrayUtils.remove(pathArr, 0));
	}
	
	@JsonIgnore @Override
	public T getState() {
		return super.getState();
	}
	
	
	@JsonIgnore @Override
	public void setState(T selectedValue) {
		boolean hasAccess = false;
		ClientUser cu = this.getProvider().getClientUser();
		Action a = setStateInternal(selectedValue);
		if(a != null){
			ClientUserAccessBehavior clientUserAccessBehavior = cu.newBehaviorInstance(ClientUserAccessBehavior.class);
			hasAccess = clientUserAccessBehavior.canUserPerformAction(getPath(),a.name());
		}
		
		if(a != null) {
			rippleToMapsTo();
		}
		
		//safe to cast as the constructor restricts to only Param type for parent
		if(hasAccess){
			setStateAndNotifyObservers(selectedValue);
			if(this.getParent() == null) {
				emitEvent(this);
			}
			else{
				emitEvent(getParent());
			}
		}else{
			if(a != null){
				log.debug("User does not have access to set state on ["+getPath()+"] for action : ["+a+"]");
				throw new UserNotAuthorizedException("User not authorized to set state on ["+getPath()+"] for action : ["+a+"]" +this);
			}
		}
			
	}
	
	/**
	 * 
	 */
	public void rippleToMapsTo() {
		getParams().stream().filter(p -> p.isMapped())                         // if p is mapped
				.forEach(p -> {                                                // set mapped state to mapsTo state
					Param<Object> mapsTo = (Param<Object>) p.getMapsTo();
					Object state = p.getState();
					mapsTo.setState(state);
				});
	}
	
	/**
	 * 
	 */
	@Override
	public void validateAndSetState(T state) {
		validate(state);
		
		if(CollectionUtils.isNotEmpty(this.validationResult.getErrors())) {
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
	    this.validationResult.addValidationErrors(getValidatorProvider().getValidator().validate(state));
	}
	
	
	
}