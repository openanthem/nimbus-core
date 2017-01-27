/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Observable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString(exclude={"parent", "eventPublisher"})
public abstract class AbstractState<T> extends Observable implements State<T> {

	@JsonIgnore final private AbstractStateAndConfig<?, ?> parent;
	
	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Supplier<T> getter;
	
	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Consumer<T> setter;

	@JsonIgnore private transient final StateAndConfigSupportProvider provider;

	@Override
	public T getState() {
		return getGetter().get();
	}
	
	/**
	 * 
	 * @param newState
	 * @return
	 */
    protected Action setStateInternal(T newState) {
		T oldState = getState();
		
		//00
		if(oldState == null && newState == null) return null;
		
		//01
		else if(oldState == null && newState != null) {
			return Action._new;
		}
		
		//10
		else if(oldState != null && newState == null) {
			return Action._delete;
		}
				
		//11
		else if(oldState != null && newState != null) {
			//if(oldState.equals(newState)) return null;
			return Action._replace;
		}
		
		throw new IllegalStateException("Illogical section of code reached while setting newState: "+ newState);
	}
	
	/**
     * 
     * @param newState
     */
    protected void setStateAndNotifyObservers(T newState) {
    	getSetter().accept(newState);
    	
    	setChanged();
    	notifyObservers();
    }
	
    
    /**
     * 
     * @param p
     */
	protected void emitEvent(StateAndConfig<?,?> p) {
		if(provider.getEventPublisher() == null) return;
		if(p instanceof ParamStateAndConfig<?>) {
			ParamStateAndConfig<?> param = (ParamStateAndConfig<?>)p;
			ModelEvent<ParamStateAndConfig<?>> e = new ModelEvent<ParamStateAndConfig<?>>(Action._replace, param.getPath(), param);
			provider.getEventPublisher().publish(e);
		}
		else if(p instanceof ModelStateAndConfig<?,?>){
			ModelStateAndConfig<?,?> param = (ModelStateAndConfig<?,?>)p;
			ModelEvent<ModelStateAndConfig<?,?>> e = new ModelEvent<ModelStateAndConfig<?,?>>(Action._replace, param.getPath(), param);
			provider.getEventPublisher().publish(e);
		}
		//ModelEvent<ParamStateAndConfig<?>> e = new ModelEvent<ParamStateAndConfig<?>>(Action._replace, p.getPath(), p);
		
	}
}
