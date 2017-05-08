/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;


/**
 * @author Soham Chakravarti
 *
 */
public interface AccessEntityRepository extends MongoRepository<DefaultAccessEntity, String> {

	public DefaultAccessEntity findByCode(String code);
}
