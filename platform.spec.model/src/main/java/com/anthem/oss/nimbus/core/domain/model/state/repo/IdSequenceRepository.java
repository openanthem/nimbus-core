package com.antheminc.oss.nimbus.core.domain.model.state.repo;

import com.antheminc.oss.nimbus.core.SequenceException;

/**
 * Repository for auto sequence generator (for implementation check {@link MongoIdSequenceRepository})
 * Can be implemented for other data sources
 * 
 * @author Rakesh Patel
 *
 */
public interface IdSequenceRepository {

	public long getNextSequenceId(String key) throws SequenceException;
}
