/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.entity.access.Role;
import com.anthem.oss.nimbus.core.entity.client.Client;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole.Entry;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

/**
 * @author Soham Chakravarti
 *
 */
@Repository
public interface ClientUserRepository extends MongoRepository<ClientUser, String> {
	
	ClientUser findByLoginName(String loginName);
	
	//@Query("MATCH(u)-[r:CLIENT]->(c) WHERE id(c)={id} RETURN u")
	List<ClientUser> findByClient(@Param("id")Client c);
	
	//@Query()
	//List<Role<Entry, ClientAccessEntity>> getGrantedRolesForClientUser(ClientUser cu);
	
	List<ClientUser> findByClientId(String id); //Empty Array returned
	
	//@Query("MATCH(u)-[r:CLIENT_USER_I_D_S]->(c:ClientUserIDS {source:{0},idsGuid:{1}}) RETURN u")
	//ClientUser findByIDSSourceAndGuid(String source,String guid);
	
	
}
