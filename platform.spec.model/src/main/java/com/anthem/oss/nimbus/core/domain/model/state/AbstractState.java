/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Observable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.anthem.oss.nimbus.core.domain.command.Action;
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

//	@JsonIgnore final private StateAndConfig<?, ?> parent;
	
//	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Supplier<T> getter;
//	
//	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Consumer<T> setter;

	@JsonIgnore private transient final StateBuilderSupport provider;

//	@Override
//	public T getState() {
//		return getGetter().get();
//	}
	
//	/**
//	 * 
//	 * @param newState
//	 * @return
//	 */
//    protected Action setStateInternal(T newState) {
//		T oldState = getState();
//		
//		//00
//		if(oldState == null && newState == null) return null;
//		
//		//01
//		else if(oldState == null && newState != null) {
//			return Action._new;
//		}
//		
//		//10
//		else if(oldState != null && newState == null) {
//			return Action._delete;
//		}
//				
//		//11
//		else if(oldState != null && newState != null) {
//			if(oldState.equals(newState)) return null;
//			return Action._replace;
//		}
//		
//		throw new IllegalStateException("Illogical section of code reached while setting newState: "+ newState);
//	}
	
	/**
     * 
     * @param newState
     */
    protected void setStateAndNotifyObservers(T newState) {
    	//getSetter().accept(newState);
    	setState(newState);
    	
    	setChanged();
    	notifyObservers(newState);
    }
	

}
