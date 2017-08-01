/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.internal;

import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.definition.Converters.ParamConverter;

/**
 * @author Rakesh Patel
 *
 */
public abstract class RepoBasedCodeToDescriptionConverter implements ParamConverter<String, String> {

	@Autowired 
	protected CommandExecutorGateway gateway;
	
	
}
