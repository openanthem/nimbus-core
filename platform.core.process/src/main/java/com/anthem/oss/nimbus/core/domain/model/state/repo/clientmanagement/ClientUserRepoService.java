/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.DefaultUser;
import com.anthem.oss.nimbus.core.web.client.ClientUserRepoAPI;

/**
 * @author AC63348
 *
 */
public class ClientUserRepoService implements ClientUserRepoAPI<ClientUser> {

	ClientUserRepository cuRepo;
	
	ClientRepository cRepo;
	
	PlatformUserRepository puRep;
	
	public ClientUserRepoService(ClientUserRepository cuRepo, ClientRepository cRepo, PlatformUserRepository puRep) {
		this.cuRepo = cuRepo;
		this.cRepo = cRepo;
		this.puRep = puRep;
	}


	private static final int FETCH_DEPTH = 5;
	
	@Value("${pageIndex:0}")
	int index;
	
	@Value("${pageSize:15}")
	int size;
	
	private static final Logger log = LoggerFactory.getLogger(ClientUserRepoService.class);
	
	@Override
	public ClientUser getUser(String loginName) throws EntityNotFoundException {
		ClientUser clientUser;
		try{
			clientUser = cuRepo.findByLoginName(loginName);
			if(clientUser != null){
				clientUser = cuRepo.findOne(clientUser.getId(),FETCH_DEPTH);
			}
		}catch(Exception e){
			throw new EntityNotFoundException("No user found with client user name"+loginName, e, ClientUser.class);
		}
		return clientUser;
	}
	
	@Override
	public ClientUser getUsersByIDSSourceAndGuid(String source, String idsGuid) throws EntityNotFoundException {
		ClientUser clientUser;
		try{
			clientUser = cuRepo.findByIDSSourceAndGuid(source, idsGuid);
			
		}catch(Exception e){
			throw new EntityNotFoundException("No user found with source and guid :"+source+","+idsGuid, e, ClientUser.class);
		}
		return clientUser;
	}

	@Override
	public Long addClientUser(String clientCode,ClientUser clientUser) throws FrameworkRuntimeException {
		ClientUser savedUser = null;
		try{
			Client ce = cRepo.findByCode(clientCode);
			Assert.notNull(ce);
			clientUser.setClient(ce);
			DefaultUser pu = addPlatformUser(clientUser);
			Assert.notNull(pu);
			clientUser.setPlatformUser(pu);
			savedUser = cuRepo.save(clientUser);
		} catch(Exception e) {
			throw new FrameworkRuntimeException("Service Exeception while saving Client User : "+clientUser.getName(), e);
		}
		return savedUser.getId();
	}
	
	public DefaultUser addPlatformUser(ClientUser cu) {
		DefaultUser platformUser = null;
		try{
			DefaultUser pu = new DefaultUser();
			pu.setEmail(cu.getEmail());
			pu.setName(cu.getName());
			platformUser = puRep.save(pu);
		} catch(Exception e){
			throw new FrameworkRuntimeException("Service Exeception while saving Platform User : "+cu.getName(), e);
		}
		return platformUser;
	}

	@Override
	public Page<ClientUser> getUsersByName(String code, ClientUser clientUser) throws EntityNotFoundException {
		try{
			List<ClientUser> cuResultList = new ArrayList<ClientUser>();
			PageRequest pageReq = new PageRequest(index,size);
			Client client = cRepo.findByCode(code);
			List<ClientUser> cuList = cuRepo.findByClient(client);
			
			// If login name exists then search with the login name else return all the users for a client
			if (clientUser != null && !StringUtils.isEmpty(clientUser.getLoginName())) {
				Assert.notNull(cuRepo.findByLoginName(clientUser.getLoginName()));
				ClientUser cuu = cuRepo.findByLoginName(clientUser.getLoginName());
				if(cuu != null) {
					cuResultList.add(cuu);
				}else{
					throw new EntityNotFoundException("Client does not exists with the login name - "+clientUser.getLoginName(),ClientUser.class);
				}
				
			}else{
				// Looping through the list, since this list is retrieved using a cypher query, hence does not take into account the depth of ClientUser
				List<ClientUser> cuList2 = new ArrayList<ClientUser>();
				for(ClientUser tempcu : cuList){
					ClientUser user = cuRepo.findOne(tempcu.getId(), 5);
					cuList2.add(user);
				}
				cuResultList.addAll(cuList2);
			}
			return new PageImpl<ClientUser>(cuResultList,pageReq,cuResultList.size());
		}catch(Exception e){
			throw new EntityNotFoundException("Service Exception while retrieving client - "+clientUser.getLoginName(),e,ClientUser.class);
		}
	}

	@Override
	public void deleteClientUser(String clientCode, ClientUser clientUser) throws FrameworkRuntimeException {
		try{
			ClientUser cuDelete = cuRepo.findOne(clientUser.getId());
			Assert.notNull(cuDelete, "Client User to be deleted cannot be null");
			if(log.isTraceEnabled()){
				log.trace("Deleting role : "+clientUser.getLoginName()+ " for client: " +clientCode);
			}
			cuRepo.delete(clientUser);
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while deleting role : "+clientUser.getLoginName()+ " for client : " +clientCode ,e);			
		}
		
	}

	@Override
	public ClientUser getUserById(Long id) throws EntityNotFoundException {
		return cuRepo.findOne(id,FETCH_DEPTH);
	}
	
	@Override
	public Long editClientUser(String clientCode, ClientUser clientUser) throws FrameworkRuntimeException {
		ClientUser editUser = null;
		try{
			editUser = cuRepo.save(clientUser);
		} catch(Exception e) {
			throw new FrameworkRuntimeException("Service Exeception while editing Client User : "+clientUser.getName(), e);
		}
		return editUser.getId();
	}


	@Override
	public Page<ClientUser> getClientUsersByNameOrAll(String code, ClientUser user) throws FrameworkRuntimeException {
		List<ClientUser> cuList = new ArrayList<ClientUser>();
		List<ClientUser> cuListFinal = new ArrayList<ClientUser>();
		PageRequest pageReq = new PageRequest(index, size);
		try {
			Client client = cRepo.findByCode(code);
			if (null != user && (null!=user.getLoginName() || null != user.getId())) {
				if (StringUtils.isNotBlank(user.getLoginName())) {
					cuList.add(cuRepo.findByLoginName(user.getLoginName()));
					return new PageImpl<ClientUser>(cuList, pageReq, cuList.size());
				} else if (user.getId() != null) {
					cuList.add(cuRepo.findOne(user.getId()));
					return new PageImpl<ClientUser>(cuList, pageReq, cuList.size());
				}
			} else if (null != client) {
				cuList = cuRepo.findByClient(client);
				// Related entities are coming as null thus making an extra call to get complete data
				cuList.forEach(cuser->{
					if(null!=cuser){
						cuListFinal.add(this.getUserById(cuser.getId()));
					}
				});
				return new PageImpl<ClientUser>(cuListFinal, pageReq, cuList.size());
			} else {
				throw new EntityNotFoundException("Client User not found ", ClientUser.class);
			}

		} catch (Exception e) {
			throw new FrameworkRuntimeException("Exception occured while getting the ClientUser : ", e);
		}
		return new PageImpl<ClientUser>(cuList, pageReq, cuList.size());
	}

}
