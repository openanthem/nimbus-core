/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.anthem.oss.nimbus.core.entity.client.Client;

/**
 * @author Soham Chakravarti
 *
 */
public interface ClientRepository extends GraphRepository<Client> ,PagingAndSortingRepository<Client, Long>{

	public Client findByCode(String code);
	
	public Page<Client> findAll();
	
}
