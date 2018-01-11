package com.antheminc.oss.nimbus.platform.spec.model.dsl.binder;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;

public class ParamStateHolder extends Holder<Object> {

	private final Param<?> param;
	
	public ParamStateHolder(Param<?> param) {
		super(param.getLeafState());
		this.param = param;
	}
	
	public Object findStateByPath(String path) {
		return this.param.findStateByPath(path);
	}
}
