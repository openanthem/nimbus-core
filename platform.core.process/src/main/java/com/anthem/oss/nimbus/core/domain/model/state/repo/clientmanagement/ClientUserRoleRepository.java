/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;

/**
 * @author Soham Chakravarti
 *
 */
public interface ClientUserRoleRepository extends MongoRepository<ClientUserRole, String> {
	
	public ClientUserRole findByCode(String code);
	
	//public Page<ClientUserRole> findAll();
	
}
