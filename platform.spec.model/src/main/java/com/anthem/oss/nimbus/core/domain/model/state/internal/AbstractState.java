/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import java.util.Observable;

import com.anthem.oss.nimbus.core.domain.model.state.EntityStateAspectHandlers;
import com.anthem.oss.nimbus.core.domain.model.state.State;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor @ToString
public abstract class AbstractState<T> extends Observable implements State<T> {

//	@JsonIgnore final private StateAndConfig<?, ?> parent;
	
//	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Supplier<T> getter;
//	
//	@JsonIgnore @Setter(AccessLevel.PROTECTED) private transient Consumer<T> setter;

	@JsonIgnore private transient final EntityStateAspectHandlers provider;

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
