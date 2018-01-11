/**
 * 
 */
package com.antheminc.oss.nimbus.platform.spec.model.dsl.binder;

import lombok.Data;


/**
 * @author Rakesh Patel
 *
 */
@Data
public class Holder<B> {

	private B state;
	
	
	public Holder() {}
	
	public Holder(B state) {
		this.state = state;
	}
	
	
	@Override
	public String toString() {
		return state != null ? state.toString() : this.getClass().getName();
	}
	
}
