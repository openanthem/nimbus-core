/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;


/**
 * @author Soham Chakravarti
 *
 */
public interface ClientAccessEntityRepository extends MongoRepository<ClientAccessEntity, String> {
	
	public ClientAccessEntity findByCode(String code);
	
//	@Query(	"MATCH (cae)-[sel:SELECTED_PERMISSIONS]->(p:Permission)"+
//			"WHERE (id)p ={id} AND (id)cae = {id}"+
//			"DETACH DELETE sel")
//	public void removeSelectedPermissions(@Param("id")Permission p,@Param("id") ClientAccessEntity cae);
	
	//@Query ("MATCH (cae)-[sel:SELECTED_PERMISSIONS]->(p:Permission) WHERE (id)cae={id} DETACH DELETE sel")
//	public void removeCAE(@Param("id") ClientAccessEntity cae);
	
}
