package com.anthem.oss.nimbus.core.web.client;

import org.springframework.data.domain.Page;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

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
	public Long addClientUser(String clientCode, ClientUser clientUser) throws ValidationException, FrameworkRuntimeException;
	
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
	public Long editClientUser(String clientCode, ClientUser clientUser) throws FrameworkRuntimeException;

}
