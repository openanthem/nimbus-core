/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain.session;

import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.defn.Constants;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

/**
 * 
 * @author Jayant Chaudhuri
 *
 */
public abstract class AbstractSessionProvider implements SessionProvider {

	@Override
	public final ClientUser getLoggedInUser() {
		return getAttribute(Constants.CLIENT_USER_KEY.code);		
	}

	@Override
	public final void setAttribute(Command cmd, Object value) {
		setAttribute(cmd.getRootDomainUri(), value);
	}

	@Override
	public final void setLoggedInUser(ClientUser clientUser) {
		setAttribute(Constants.CLIENT_USER_KEY.code, clientUser);
	}

}
