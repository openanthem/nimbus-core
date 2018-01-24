/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core.web.client;

import org.springframework.data.domain.Page;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationException;
import com.antheminc.oss.nimbus.domain.model.state.EntityNotFoundException;
import com.antheminc.oss.nimbus.entity.client.user.ClientUser;

public interface ClientUserRepoAPI<T extends ClientUser> {
	
	/**
	 * 
	 */
	public static final int CE_FETCH_DEPTH = 5;
	
	
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException
	 */
	public T getUserById(String id) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param source
	 * @param idsGuid
	 * @return
	 * @throws EntityNotFoundException
	 */
	public T getUsersByIDSSourceAndGuid(String source, String idsGuid) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUser
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public String addClientUser(String clientCode, ClientUser clientUser) throws ValidationException, FrameworkRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUser
	 * @return
	 * @throws EntityNotFoundException
	 */
	public Page<ClientUser> getUsersByName(String clientCode, ClientUser clientUser) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param clientCode
	 * @param cu
	 * @throws PlatformRuntimeException
	 */
	public void deleteClientUser(String clientCode, ClientUser cu) throws FrameworkRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUser
	 * @return
	 * @throws PlatformRuntimeException
	 */
	public String editClientUser(String clientCode, ClientUser clientUser) throws FrameworkRuntimeException;
	
	/**
	 * Gets the client users by name or all.
	 *
	 * @param code the code
	 * @param user the user
	 * @return the client users by name or all
	 * @throws FrameworkRuntimeException the framework runtime exception
	 */
	public Page<ClientUser> getClientUsersByNameOrAll(String code, ClientUser user) throws FrameworkRuntimeException;

}
