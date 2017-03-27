/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;

/**
 * @author Rakesh Patel
 *
 */
@Repository
public interface ClientUserGroupRepository extends GraphRepository<ClientUserGroup> {
	
	ClientUserGroup findByName(String name);
	
	@Query("MATCH(ug:ClientUserGroup)-[]->(ce:ClientEntity) Where ce.code = {0} return ug")
	ClientUserGroup findByClientEntity(String code);
	

}
