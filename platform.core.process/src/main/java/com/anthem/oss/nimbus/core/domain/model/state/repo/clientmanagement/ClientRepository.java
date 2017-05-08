/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.anthem.oss.nimbus.core.entity.client.Client;

/**
 * @author Soham Chakravarti
 *
 */
public interface ClientRepository extends PagingAndSortingRepository<Client, String>{

	public Client findByCode(String code);
	
	public Page<Client> findAll();
	
}
