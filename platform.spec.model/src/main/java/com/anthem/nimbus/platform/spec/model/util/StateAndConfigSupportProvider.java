/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.anthem.nimbus.platform.spec.contract.event.EventPublisher;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.validation.ValidatorProvider;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@Getter @Setter @RequiredArgsConstructor
public class StateAndConfigSupportProvider {
	
	private final EventPublisher eventPublisher;
	
	private final ClientUser clientUser;
	
	//private final Supplier<ClientUser> getter;
	
	//private final Consumer<ClientUser> setter;
	
	private final ValidatorProvider validatorProvider;

}
