package com.anthem.nimbus.platform.spec.contract.client;

import org.springframework.data.domain.Page;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.command.ValidationException;
import com.anthem.nimbus.platform.spec.model.exception.EntityNotFoundException;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;

public interface ClientUserRepoAPI<T extends ClientUser> {
	
	/**
	 * 
	 */
	public static final int CE_FETCH_DEPTH = 5;
	
	/**
	 * 
	 * @param loginName
	 * @return
	 * @throws EntityNotFoundException
	 */
	public T getUser(String loginName) throws EntityNotFoundException;
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws EntityNotFoundException
	 */
	public T getUserById(Long id) throws EntityNotFoundException;
	
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
	public Long addClientUser(String clientCode, ClientUser clientUser) throws ValidationException, PlatformRuntimeException;
	
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
	public void deleteClientUser(String clientCode, ClientUser cu) throws PlatformRuntimeException;
	
	/**
	 * 
	 * @param clientCode
	 * @param clientUser
	 * @return
	 * @throws PlatformRuntimeException
	 */
	public Long editClientUser(String clientCode, ClientUser clientUser) throws PlatformRuntimeException;

}
