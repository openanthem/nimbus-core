/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.entity.user.DefaultUser;

/**
 * @author Soham Chakravarti
 *
 */
@Repository
public interface PlatformUserRepository extends MongoRepository<DefaultUser, String> {

	public DefaultUser findByEmail(String email);
}
