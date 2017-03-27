/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;

/**
 * @author Soham Chakravarti
 *
 */
public interface ClientUserRoleRepository extends GraphRepository<ClientUserRole> {
	
	public ClientUserRole findByCode(String code);
	
	public Page<ClientUserRole> findAll();
	
}
