package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.anthem.oss.nimbus.core.entity.access.DefaultRole;

public interface PlatformRoleRepository extends GraphRepository<DefaultRole> {

	public DefaultRole findByCode(String code);
	
}
