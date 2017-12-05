/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import org.springframework.beans.factory.annotation.Autowired;

import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.antheminc.oss.nimbus.core.domain.definition.Converters.ParamConverter;

/**
 * @author Rakesh Patel
 *
 */
public abstract class RepoBasedConverter implements ParamConverter<String, String> {

	@Autowired 
	protected CommandExecutorGateway gateway;
	
	
}
