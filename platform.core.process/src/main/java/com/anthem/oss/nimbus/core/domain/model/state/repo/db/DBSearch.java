package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import com.anthem.oss.nimbus.core.entity.SearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
public interface DBSearch {
	
	public <T> Object search(Class<?> referredClass, String alias, SearchCriteria<T> criteria);
}
