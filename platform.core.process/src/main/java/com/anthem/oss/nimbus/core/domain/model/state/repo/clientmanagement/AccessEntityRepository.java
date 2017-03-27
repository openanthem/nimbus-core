/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;


/**
 * @author Soham Chakravarti
 *
 */
public interface AccessEntityRepository extends GraphRepository<DefaultAccessEntity> {

	public DefaultAccessEntity findByCode(String code);
}
