/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository;
import com.anthem.nimbus.platform.spec.model.dsl.config.ParamConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Soham Chakravarti
 *
 */
public class MappedParamStateAndConfig<T> extends ParamStateAndConfig<T> {

	private static final long serialVersionUID = 1L;

	private final StateAndConfig.Param<T> mapsTo;

	public MappedParamStateAndConfig(StateAndConfig.Param<T> mapsTo, ModelStateAndConfig<?, ?> parent,
			ParamConfig<T> config, ParamStateRepository pRep, StateAndConfigSupportProvider provider) {
		
		super(parent, config, pRep, provider);
		this.mapsTo = mapsTo;
	}

	@Override
	public void initConfigState() {
		mapsTo.initConfigState();
		
		//visit cloned config available for this QuadState
		super.initConfigState();
	}
	
	@Override
	public boolean isMapped() {
		return true;
	}
	
	@JsonIgnore @Override
	public StateAndConfig.Param<T> getMapsTo() {
		return mapsTo;
	}
	
}
