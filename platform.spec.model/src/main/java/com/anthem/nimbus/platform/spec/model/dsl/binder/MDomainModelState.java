/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.util.Objects;

import com.anthem.nimbus.platform.spec.model.dsl.binder.DomainState.MappedModel;
import com.anthem.nimbus.platform.spec.model.dsl.config.ModelConfig;
import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MDomainModelState<T, M> extends DomainModelState<T> implements MappedModel<T, M>{

    private static final long serialVersionUID = 1L;

    private final Model<M> mapsTo;

	public MDomainModelState(Model<M> mapsTo, Param<T> associatedParam, ModelConfig<T> config, StateAndConfigSupportProvider provider) {
    	super(associatedParam, config, provider);
    	
    	Objects.requireNonNull(mapsTo, "MapsTo model must not be null.");
    	this.mapsTo = mapsTo;
 	}

}
