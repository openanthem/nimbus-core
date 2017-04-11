/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;

/**
 * @author Sriman Cherukuri
 *
 */
@RefreshScope
public class ClientUserGroupRepoService {

	public ClientUserGroupRepoService(ClientEntityRepository ceRepo, ClientUserGroupRepository cugRepo) {
		this.ceRepo = ceRepo;
		this.cugRepo = cugRepo;
	}

	ClientEntityRepository ceRepo;
	
	ClientUserGroupRepository cugRepo;
	
	private static final int FETCH_DEPTH = 5;
	
	@Value("${pageIndex:0}")
	int index;
	
	@Value("${pageSize:15}")
	int size;
	
	public ClientUserGroup setAssociatedTo(Long clientEntityId, ClientUserGroup clientUserGroup) throws ValidationException, FrameworkRuntimeException {
		try{
			Assert.notNull(clientEntityId, "Parent Client entity Id cannot be null");
			Assert.notNull(clientUserGroup, "ClientUserGroup to be added cannot be null");
			
			ClientEntity parentClientEntity = ceRepo.findOne(clientEntityId);
			Assert.notNull(parentClientEntity, "Parent Client Entity cannot be null");
			
			//associate clientusergroup to cliententity(org)
			if(clientUserGroup.getId()!=null) {
				cugRepo.save(clientUserGroup);
			} else {
				clientUserGroup.addassociatedTo(parentClientEntity);
				cugRepo.save(clientUserGroup);
			}
			
			ClientUserGroup cug = cugRepo.findByName(clientUserGroup.getName());
			
			return cug;
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Exception while adding a nested client entity - "+clientUserGroup+" : "+e.getMessage());
		}
	}
	
	public Page<ClientUserGroup> getAllUserGroupsForOrg(String code) throws ValidationException, FrameworkRuntimeException {
		try{
			PageRequest pageReq = new PageRequest(index,size);
			
			List<ClientUserGroup> cugResultList = new ArrayList<ClientUserGroup>();
			List<ClientUserGroup> cugList = cugRepo.findAllByClientEntity(code);
			
			if(cugList != null){
				
				// associated to the usergroup entities are coming as null thus making an extra call to get complete data
				cugList.forEach(usergroup->{
					if(null!=usergroup){
						cugResultList.add(this.getUserGroupById(usergroup.getId()));
					}
				});
			}
			
			return new PageImpl<ClientUserGroup>(cugResultList, pageReq, cugResultList.size());
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while retrieving client entity - "+code.toString());
		}
	}
	
	public ClientUserGroup getUserGroupById(Long id) throws EntityNotFoundException {
		return cugRepo.findOne(id,FETCH_DEPTH);
	}
}
