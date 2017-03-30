/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;

/**
 * @author Sriman Cherukuri
 *
 */
@Service("clientUserGroupRepo")
@RefreshScope
public class ClientUserGroupRepoService {

	@Autowired ClientEntityRepository ceRepo;
	
	@Autowired ClientUserGroupRepository cugRepo;
	
	public ClientUserGroup addAssociatedTo(Long clientEntityId, ClientUserGroup cu) throws ValidationException, FrameworkRuntimeException {
		try{
			Assert.notNull(clientEntityId, "Parent Client entity Id cannot be null");
			Assert.notNull(cu, "ClientUserGroup to be added cannot be null");
			
			ClientEntity parentClientEntity = ceRepo.findOne(clientEntityId);
			Assert.notNull(parentClientEntity, "Parent Client Entity cannot be null");
			
			//associate clientusergroup to cliententity(org)
			cu.setAssociatedTo(parentClientEntity);
			cugRepo.save(cu);
			
			ClientUserGroup cug = cugRepo.findByName(cu.getName());
			
			return cug;
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Exception while adding a nested client entity - "+cu+" : "+e.getMessage());
		}
		
		
	}
}
