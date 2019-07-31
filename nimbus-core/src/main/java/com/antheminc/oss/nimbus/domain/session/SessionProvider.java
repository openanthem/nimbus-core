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

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

/**
 * @author Rakesh Patel
 *
 */
public interface SessionProvider {
	public String getSessionId();
	
	public <R> R getAttribute(String key);
	
	public void setAttribute(String key, Object value);
	public void setAttribute(Command cmd, Object value);
	public boolean removeAttribute(String key);
	
	default public void clear() {
		throw new FrameworkRuntimeException("Implementation class "+this.getClass().getName()+" needs to override the clear method");
	}
	
	public ClientUser getLoggedInUser();	
	public void setLoggedInUser(ClientUser clientUser);
}
