/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.model.config.ValidatorProvider;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateGateway;
import com.anthem.oss.nimbus.core.spec.contract.event.BulkEventListener;
import com.anthem.oss.nimbus.core.spec.contract.event.EventListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter @AllArgsConstructor
public class EntityStateAspectHandlers {

	private EventListener eventListener;
	
	private BulkEventListener bulkEventListener;
	
	private ValidatorProvider validatorProvider;
	
	private ParamStateGateway paramStateGateway;
}
 