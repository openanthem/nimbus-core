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

import java.util.List;

import org.springframework.data.domain.Page;

import com.antheminc.oss.nimbus.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.domain.cmd.exec.ValidationException;
import com.antheminc.oss.nimbus.entity.client.Client;
import com.antheminc.oss.nimbus.entity.client.ClientEntity;
import com.antheminc.oss.nimbus.entity.client.access.ClientUserRole;

/**
 * @author Swetha Vemuri
 *
 */
public interface ClientEntityRepoAPI<T extends ClientEntity> {
	
	/**
	 * 
	 */
	public static final int CE_FETCH_DEPTH = 5;
	
	
	/**
	 * 
	 * @param c
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public String addClient(Client c) throws ValidationException, FrameworkRuntimeException;
	
	/**
	 * 
	 * @param c
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public String addClientEntity(ClientEntity c) throws ValidationException, FrameworkRuntimeException;
	
	//public Page<Client> getAllClients() throws FrameworkRuntimeException;
	
	/**
	 * 
	 * @param clientId
	 * @param ce
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public String addNestedEntity(String clientId, ClientEntity ce) throws ValidationException, FrameworkRuntimeException;
	
	/**
	 * 
	 * @param client
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public Page<Client> getClientByNameOrAll(Client client) throws ValidationException, FrameworkRuntimeException; 
	
	/**
	 * 
	 * @param clientId
	 * @param clientEntity
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public Page<ClientEntity> getClientEntityByNameOrAll(String clientId , ClientEntity clientEntity) throws ValidationException, FrameworkRuntimeException; 
	
	/**
	 * 
	 * @param clientCode
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public List<ClientUserRole> getAllRolesForClient(String clientCode) throws FrameworkRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param cuRole
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public List<ClientUserRole> searchRolesForClient(String clientCode, ClientUserRole cuRole) throws ValidationException, FrameworkRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param role
	 * @return
	 */
	public String addRoleForClient(String clientCode, ClientUserRole role);
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUserRole
	 */
	public void deleteRoleForClient(String clientCode, ClientUserRole clientUserRole);
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUserRole
	 */
	public void updateRoleForClient(String clientCode, ClientUserRole clientUserRole);
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public ClientUserRole getClientUserRoleByCode(String code) throws FrameworkRuntimeException;
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws FrameworkRuntimeException
	 */
	public Client getClientByCode(String code) throws FrameworkRuntimeException;

}
