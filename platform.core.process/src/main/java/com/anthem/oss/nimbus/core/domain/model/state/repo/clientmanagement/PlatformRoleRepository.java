package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.access.DefaultRole;

public interface PlatformRoleRepository extends MongoRepository<DefaultRole, String> {

	public DefaultRole findByCode(String code);
	
}
