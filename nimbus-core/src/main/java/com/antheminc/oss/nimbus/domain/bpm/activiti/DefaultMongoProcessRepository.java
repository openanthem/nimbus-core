/**
 * 
 */
package com.antheminc.oss.nimbus.domain.bpm.activiti;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.antheminc.oss.nimbus.domain.bpm.ProcessRepository;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Rakesh Patel
 *
 */
@RequiredArgsConstructor @Getter
public class DefaultMongoProcessRepository implements ProcessRepository {

	private final MongoOperations mongoOps;
	
	
	@Override
	public <T> T _save(T state, String alias) {
		getMongoOps().save(state, alias);
		return state;
	}

	
	@Override
	public <T> T _update(String alias, Serializable id, String path, T state) {
		path = resolvePath(path);

		Query query = new Query(Criteria.where("_id").is(id));
		Update update = new Update();
		
		if (StringUtils.isBlank(path) || StringUtils.equalsIgnoreCase(path, "/c")) {
			getMongoOps().save(state, alias);
		} else {
			if (StringUtils.equals(path, "/id") || StringUtils.equals(path, "id")) {
				return state;
			}
			path = StringUtils.substringAfter(path, "/");
			path = path.replaceAll("/", "\\.");
			if (state == null)
				update.unset(path);
			else
				update.set(path, state);
			getMongoOps().upsert(query, update, alias);
		}
		return state;
	}

	@Override
	public <T> T _get(Serializable id, Class<T> referredClass, String alias) {
		T state = getMongoOps().findById(id, referredClass, alias);
		return state;
	}
	
	
	private String resolvePath(String path) {
		String p = StringUtils.replace(path, "/c/", "/");
		p = StringUtils.replace(p, "/v/", "/");
		return p;
	}

}
