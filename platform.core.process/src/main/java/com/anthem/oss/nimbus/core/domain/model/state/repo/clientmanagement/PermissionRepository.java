/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.anthem.oss.nimbus.core.entity.access.Permission;
/**
 * @author AC63348
 *
 */
public interface PermissionRepository extends GraphRepository<Permission> {

}
