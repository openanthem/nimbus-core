/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.nimbus.platform.spec.model.dsl.Action;

/**
 * @author Soham Chakravarti
 *
 */
public class SimpleState<T> extends AbstractState<T> {

	
	public SimpleState(ParamStateAndConfig<?> parent, Supplier<T> getter, Consumer<T> setter,
			StateAndConfigSupportProvider provider) {
		
		super(parent, provider);
		setGetter(getter);
		setSetter(setter);
	}
	
	
    /**
     * 
     */
	@Override
	public void setState(T newState) {
		Action a = setStateInternal(newState);
		
		if(a != null) {
			setStateAndNotifyObservers(newState);
			
			emitEvent((ParamStateAndConfig<?>)getParent());
		}
	}
	
	@Override
	public void validateAndSetState(T state)  {
		//throw new OperationNotSupportedException("Cannot validate and set state for SimplateState type: "+state);
	}
	
}
