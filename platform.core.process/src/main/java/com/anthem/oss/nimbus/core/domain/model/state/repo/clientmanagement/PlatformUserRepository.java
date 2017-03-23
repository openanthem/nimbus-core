/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

import com.anthem.oss.nimbus.core.entity.user.DefaultUser;

/**
 * @author Soham Chakravarti
 *
 */
@Repository
public interface PlatformUserRepository extends GraphRepository<DefaultUser> {

	public DefaultUser findByEmail(String email);
}
