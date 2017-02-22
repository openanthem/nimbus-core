/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;


/**
 * @author Soham Chakravarti
 *
 */
@Component("rep_neo4j")
public class DefaultNeo4jModelRepository implements ModelRepository {

	@Override
	public <T> T _new(Class<T> referredClass, String alias) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}

	@Override
	public <T> T _new(Class<T> referredClass, String alias, T input) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}

	@Override
	public <ID extends Serializable, T> T _get(ID id, Class<T> referredClass, String alias) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}

	@Override
	public <ID extends Serializable,T> T _update(String alias, ID id, String path, T state) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}
	
	@Override
	public void _replace(Param<?> param) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}

	@Override
	public void _replace(List<Param<?>> params) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}
	
	@Override
	public <T> T _replace(String alias, T state) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}

	@Override
	public <ID extends Serializable, T> T _delete(ID id, Class<T> referredClass, String alias) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}
	
	@Override
	public <T, C> List<T> _search(Class<T> referredClass, String alias, C criteria) {
		throw new UnsupportedOperationException("neo4j repo not yet implemented");
	}
	
}
