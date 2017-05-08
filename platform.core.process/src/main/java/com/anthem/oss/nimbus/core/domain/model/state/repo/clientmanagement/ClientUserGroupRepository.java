/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;

/**
 * @author Rakesh Patel
 *
 */
@Repository
public interface ClientUserGroupRepository extends MongoRepository<ClientUserGroup, String> {
	
	ClientUserGroup findByName(String name);
	
	//@Query("MATCH(ug:ClientUserGroup)-[]->(ce:ClientEntity) Where ce.code = {0} return ug")
	//ClientUserGroup findByClientEntity(String code);
	
	//@Query("MATCH(ug:ClientUserGroup)-[]->(ce:ClientEntity) Where ce.code = {0} return ug")
	List<ClientUserGroup> findAllByClientEntity(String code);
	
}
