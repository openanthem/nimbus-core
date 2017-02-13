/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import java.util.Objects;

import com.anthem.nimbus.platform.spec.model.util.StateAndConfigSupportProvider;
import com.anthem.oss.nimbus.core.domain.model.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.MappedModel;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
public class MappedDefaultModelState<T, M> extends DefaultModelState<T> implements MappedModel<T, M>{

    private static final long serialVersionUID = 1L;

    @JsonIgnore private final Model<M> mapsTo;

	public MappedDefaultModelState(Model<M> mapsTo, Param<T> associatedParam, ModelConfig<T> config, StateAndConfigSupportProvider provider) {
    	super(associatedParam, config, provider);
    	
    	Objects.requireNonNull(mapsTo, "MapsTo model must not be null.");
    	this.mapsTo = mapsTo;
 	}

}
