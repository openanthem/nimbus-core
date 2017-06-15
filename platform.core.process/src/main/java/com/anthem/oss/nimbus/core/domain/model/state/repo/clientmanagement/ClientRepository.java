/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.client.Client;

/**
 * @author Soham Chakravarti
 *
 */
public interface ClientRepository extends MongoRepository<Client, String>{

	public Client findByCode(String code);
	
	//public Page<Client> findAll();
	
}
