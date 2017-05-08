/**
 * 
 */
package com.anthem.oss.nimbus.core.web.client;

import java.util.List;

import org.springframework.data.domain.Page;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;

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
	 * @param clientId
	 * @param ce
	 * @return
	 * @throws ValidationException
	 * @throws FrameworkRuntimeException
	 */
	public boolean editNestedEntity(String clientId, ClientEntity ce) throws ValidationException, FrameworkRuntimeException;
	
	
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
