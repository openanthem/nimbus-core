package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.client.ClientEntity;

public interface ClientEntityRepository extends MongoRepository<ClientEntity, String>{
	
	ClientEntity findByCode(String code);
	
	ClientEntity findByName(String name);
	
	//Page<ClientEntity> findAll();
			
}
