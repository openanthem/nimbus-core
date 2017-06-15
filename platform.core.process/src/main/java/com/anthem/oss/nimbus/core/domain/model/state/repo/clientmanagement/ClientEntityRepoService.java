/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.Assert;

import com.anthem.oss.nimbus.core.DataIntegrityViolationExecption;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.client.ClientEntity.Type;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.web.client.ClientEntityRepoAPI;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Swetha Vemuri
 *
 */
@Slf4j
@RefreshScope
public class ClientEntityRepoService implements ClientEntityRepoAPI<ClientEntity> {

	ClientRepository cRepo;
	
	ClientEntityRepository ceRepo;
	
	ClientUserRoleRepository crRepo;
	
	public ClientEntityRepoService(ClientRepository cRepo, ClientEntityRepository ceRepo, ClientUserRoleRepository crRepo) {
		this.cRepo = cRepo;
		this.ceRepo = ceRepo;
		this.crRepo = crRepo;
	}

	@Value("${pageIndex:0}")
	int index;
	
	@Value("${pageSize:15}")
	int size;

	@Override
	public String addClient(Client c) throws FrameworkRuntimeException {
		try{
			Assert.notNull(c, "Client to be added cannot be null");
			Assert.isNull(c.getId(),"Client to be added cannot have a pre-assigned id."+
								"Found value :"+c.getId()+" for client code :"+c.getCode());
			log.debug("Check for existing client:"+cRepo.findByCode(c.getCode()));
			//TODO Revisit this - UI does not have code.
			c.setCode(c.getName());
			
			if(cRepo.findByCode(c.getCode()) != null){
				throw new DataIntegrityViolationExecption("Client cannot be added without a unique client code - "+c.getCode(),Client.class);
			}
			Client dbClient = ceRepo.save(c);
			if(log.isInfoEnabled())
				log.info("Client saved::"+dbClient);
			return dbClient.getId();
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while adding client: "+c.getCode(),e);
		}
	}

	@Override
	public String addNestedEntity(String clientEntityId , ClientEntity nestedEntity) throws FrameworkRuntimeException{
		try{
			Assert.notNull(nestedEntity, "ClientEntity to be added cannot be null");
			Assert.notNull(clientEntityId,"Parent Client entity Id cannot be null");
			
			ClientEntity parentClientEntity = ceRepo.findOne(clientEntityId);
			Assert.notNull(parentClientEntity,"Parent Client Entity cannot be null");
			
			nestedEntity.setCode(nestedEntity.getName());
			nestedEntity.setType(Type.ORG);
			ceRepo.save(parentClientEntity);
			
			ClientEntity cenested = ceRepo.findByName(nestedEntity.getName());
			
			return cenested.getId();
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Exception while adding a nested client entity - "+nestedEntity+" : "+e.getMessage());
		}
		
	}
	

	
	@Override
	public Page<Client> getClientByNameOrAll(Client client) throws FrameworkRuntimeException{
		List<Client> ceList = new ArrayList<Client>();
		try{
			PageRequest pageReq = new PageRequest(index,size);	
			
			if(StringUtils.isNotBlank(client.getName()) && client.getId() == null){
				ClientEntity ce = ceRepo.findByName(client.getName());
				if(ce != null) {
					ceList.add(cRepo.findOne(ce.getId()));
					return new PageImpl<Client>(ceList,pageReq,ceList.size());
				} else {
					throw new EntityNotFoundException("Client Entity not found with name "+client.getName(),Client.class);
				}
			}
			else if(client.getId() != null) {
				ceList.add(cRepo.findOne(client.getId()));
				return new PageImpl<Client>(ceList,pageReq,ceList.size());
			}
			else {
				return cRepo.findAll(pageReq);
			}
			
		}catch(Exception e) {
			throw new FrameworkRuntimeException("Exception occured while getting the ClientEntity By Name : ",e);
		}
	}
	
	

	@Override
	public List<ClientUserRole> getAllRolesForClient(String clientCode) throws EntityNotFoundException{
		try{
			ClientEntity ce = ceRepo.findByCode(clientCode);
			Assert.notNull(ce, "Client Entity cannot be null");
			List<ClientUserRole> cr = new ArrayList<>();
			if(ce.getAssociatedRoles() != null)
				cr.addAll(ce.getAssociatedRoles());				
			return cr;
		}catch (Exception e){
			throw new EntityNotFoundException("Service Exception while retrieving client entity - "+clientCode,e,ClientEntity.class);
		}
	}
	
	@Override
	public List<ClientUserRole> searchRolesForClient(String clientCode, ClientUserRole cuRole) throws EntityNotFoundException {
		try{
			ClientEntity ce = ceRepo.findByCode(clientCode);

			Assert.notNull(ce, "Client Entity cannot be null");
			List<ClientUserRole> cr = new ArrayList<>();
			if (StringUtils.isEmpty(cuRole.getName())) {
				if(ce.getAssociatedRoles() != null)
					cr.addAll(ce.getAssociatedRoles());	
			} else {
				if(ce.getAssociatedRoles() != null) {
					for (ClientUserRole role: ce.getAssociatedRoles()) {
						if (role.getName().equalsIgnoreCase(cuRole.getName())) {
							cr.add(role);
						}
					}
				}
			}
			return cr;
		}catch (Exception e){
			throw new EntityNotFoundException("Service Exception while retrieving client entity - "+clientCode,e,ClientEntity.class);
		}
	}

	@Override
	public String addRoleForClient(String clientCode, ClientUserRole role) {
		try{
			ClientEntity ce = ceRepo.findByCode(clientCode);
			Assert.notNull(ce , "Client Entity cannot be null");
			Assert.notNull(role , "Client User Role to be added cannot be null");
			Assert.isNull(role.getId(), " Client User Role to be added cannot have a pre-assigned id." + 
					"Found value : "+role.getId()+" for role : "+role.getCode());
			
			if (StringUtils.isEmpty(role.getCode())) {
				role.setCode(role.getName());
			}
			
			for(ClientUserRole cr : ce.getAssociatedRoles()){
				if(StringUtils.equals(cr.getCode(), role.getCode())){
					throw new DataIntegrityViolationExecption("Role to be added : "+role.getCode() +" is a duplicate for client : "+ce.getCode(),ClientUserRole.class);					
				}
			}
			
			if(log.isTraceEnabled()){
				log.trace("Adding role : "+role.getCode()+ "to client: " +ce.getCode());
			}
			ce.addClientUserRole(role);
			ceRepo.save(ce);
			
			for(ClientUserRole cr : ce.getAssociatedRoles()){
				if(StringUtils.equals(cr.getCode(), role.getCode())){
					return cr.getId();
				}
			}
			return null;
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while adding role : "+role.getCode()+ " for client : " +clientCode ,e);
		}
		
	}

	@Override
	public void deleteRoleForClient(String clientCode, ClientUserRole clientUserRole) {
		try{
			ClientUserRole crDelete = crRepo.findOne(clientUserRole.getId());
			Assert.notNull(crDelete, "Client User Role to be deleted cannot be null");
			if(log.isTraceEnabled()){
				log.trace("Deleting role : "+clientUserRole.getCode()+ " for client: " +clientCode);
			}
			crRepo.delete(clientUserRole);
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while deleting role : "+clientUserRole.getCode()+ " for client : " +clientCode ,e);			
		}
		
	}

	@Override
	public void updateRoleForClient(String clientCode, ClientUserRole clientUserRole) {
		try{
			ClientUserRole crUpdate = crRepo.findOne(clientUserRole.getId());
			Assert.notNull("crUpdate", "Client User Role to be updated cannot be null");
			if(log.isTraceEnabled()){
				log.trace("Updating role : "+crUpdate.getCode()+ "to client ");
			}
			crRepo.save(clientUserRole);
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while updating role : "+clientUserRole.getCode()+ " for client : " +clientCode ,e);
		}
		
	}

	@Override
	public ClientUserRole getClientUserRoleByCode(String code) throws FrameworkRuntimeException {
		try{
			Assert.notNull(code, "Client Role ID cannot be null");
			ClientUserRole cur;
			if(NumberUtils.isDigits(code)) {
				cur = crRepo.findOne(code);
			}
			else {
				cur = crRepo.findByCode(code);
			}
			if(cur == null) {
				throw new EntityNotFoundException("Client User Role not found: "+code,ClientUserRole.class); 
			} 			
			return cur;
		}catch(Exception e) {
			throw new FrameworkRuntimeException("Exception occured while getting the ClientUserRole By code : "+code,e);
		}
	}
	
	@Override
	public Page<ClientEntity> getClientEntityByNameOrAll(String id, ClientEntity clientEntity)
			throws ValidationException, FrameworkRuntimeException {
		try{
			PageRequest pageReq = new PageRequest(index,size);	
			ClientEntity ce = ceRepo.findOne(id);
			List<ClientEntity> ceList = new ArrayList<ClientEntity>();
			if(clientEntity != null && clientEntity.getId() != null) { 
				//ClientEntity cee = ceRepo.findByName(clientEntity.getName());
				ClientEntity cee = ceRepo.findOne(clientEntity.getId());
				if(cee != null) {
//					List<String> clientEntityNames = clientEntitys.stream()
//							.map(s -> s.getName())
//							.collect(Collectors.toList());
//					if(clientEntityNames.contains(cee.getName())){
						ceList.add(cee);
						return new PageImpl<ClientEntity>(ceList,pageReq,ceList.size());
					//}
				} else {
					throw new EntityNotFoundException("Client Entity not found with name "+clientEntity.getName(),ClientEntity.class);
				}				

			}
			return new PageImpl<ClientEntity>(ceList,pageReq,ceList.size());
		}catch(Exception e){
			throw new EntityNotFoundException("Service Exception while retrieving client entity - "+id.toString(),e,ClientEntity.class);
		}

	}

	@Override
	public String addClientEntity(ClientEntity c) throws ValidationException, FrameworkRuntimeException {
		try{
			Assert.notNull(c, "ClientEntity to be added cannot be null");
			Assert.isNull(c.getId(),"ClienEntityt to be added cannot have a pre-assigned id."+
								"Found value :"+c.getId()+" for client code :"+c.getCode());
			//log.debug("Check for existing client:"+cRepo.findByCode(c.getCode()));
			//TODO Revisit this - UI does not have code.
			c.setCode(c.getName());
			
			if(cRepo.findByCode(c.getCode()) != null){
				throw new DataIntegrityViolationExecption("Client cannot be added without a unique client code - "+c.getCode(),Client.class);
			}
			ClientEntity dbClientEntity = ceRepo.save(c);
			if(log.isInfoEnabled())
				log.info("Client saved::"+dbClientEntity);
			return dbClientEntity.getId();
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while adding client: "+c.getCode(),e);
		}
	}

	@Override
	public Client getClientByCode(String code) throws FrameworkRuntimeException {
		try{
			Client c;
			if(NumberUtils.isDigits(code)) {
				c = cRepo.findOne(code);
			}
			else{
				c = cRepo.findByCode(code);
			}
			Assert.notNull(c, "Cannot find client with code "+code);
			if(c == null){
				throw new EntityNotFoundException("Client does not exist with code : "+code, Client.class);
			}else
				return cRepo.findOne(c.getId());
			
		}catch(Exception e){
			throw new FrameworkRuntimeException("Service Exception while retrieving Client with code : "+code,e);
		}
	}

	
}
