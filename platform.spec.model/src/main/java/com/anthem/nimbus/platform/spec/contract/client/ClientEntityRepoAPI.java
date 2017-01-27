/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.client;

import java.util.List;

import org.springframework.data.domain.Page;
import com.anthem.nimbus.platform.spec.model.client.Client;
import com.anthem.nimbus.platform.spec.model.client.ClientEntity;
import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

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
	 * @throws PlatformRuntimeException
	 */
	public Long addClient(Client c) throws ValidationException, PlatformRuntimeException;
	
	/**
	 * 
	 * @param c
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public Long addClientEntity(ClientEntity c) throws ValidationException, PlatformRuntimeException;
	
	//public Page<Client> getAllClients() throws PlatformRuntimeException;
	
	/**
	 * 
	 * @param clientId
	 * @param ce
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public Long addNestedEntity(Long clientId, ClientEntity ce) throws ValidationException, PlatformRuntimeException;
	
	/**
	 * 
	 * @param client
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public Page<Client> getClientByNameOrAll(Client client) throws ValidationException, PlatformRuntimeException; 
	
	/**
	 * 
	 * @param clientId
	 * @param clientEntity
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public Page<ClientEntity> getClientEntityByNameOrAll(Long clientId , ClientEntity clientEntity) throws ValidationException, PlatformRuntimeException; 
	
	/**
	 * 
	 * @param clientCode
	 * @return
	 * @throws PlatformRuntimeException
	 */
	public List<ClientUserRole> getAllRolesForClient(String clientCode) throws PlatformRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param cuRole
	 * @return
	 * @throws ValidationException
	 * @throws PlatformRuntimeException
	 */
	public List<ClientUserRole> searchRolesForClient(String clientCode, ClientUserRole cuRole) throws ValidationException, PlatformRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param role
	 * @return
	 */
	public Long addRoleForClient(String clientCode, ClientUserRole role);
	
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
	 * @throws PlatformRuntimeException
	 */
	public ClientUserRole getClientUserRoleByCode(String code) throws PlatformRuntimeException;
	
	/**
	 * 
	 * @param code
	 * @return
	 * @throws PlatformRuntimeException
	 */
	public Client getClientByCode(String code) throws PlatformRuntimeException;

}
