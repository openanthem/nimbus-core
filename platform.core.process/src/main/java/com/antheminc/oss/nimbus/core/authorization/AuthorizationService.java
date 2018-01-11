package com.antheminc.oss.nimbus.core.authorization;

import com.antheminc.oss.nimbus.core.domain.command.Command;


/**
 * @author Rakesh Patel
 *
 */
public interface AuthorizationService {

	public boolean hasAccess(Command command) throws AuthorizationException;
}
