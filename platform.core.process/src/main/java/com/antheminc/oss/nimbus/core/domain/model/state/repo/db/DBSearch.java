package com.antheminc.oss.nimbus.core.domain.model.state.repo.db;

import com.antheminc.oss.nimbus.core.entity.SearchCriteria;

/**
 * @author Rakesh Patel
 *
 */
public interface DBSearch {
	
	public <T> Object search(Class<?> referredClass, String alias, SearchCriteria<T> criteria);
}
