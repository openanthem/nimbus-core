/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.session.ModelHolder;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.config.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepository;
import com.anthem.oss.nimbus.core.util.ClassLoadUtils;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component("default.param.state.rep_session")
public class SessionCacheRepository implements ParamStateRepository {
	
    @Autowired
    @Qualifier("sessionRedisTemplate")
    private RedisTemplate<Object,Object> template;
    
    private HashOperations<Object,Object,Object> hashOps;
    
    @PostConstruct
    private void init() {
        hashOps = template.opsForHash();
    }

	/* (non-Javadoc)
	 * @see com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository#_get(com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param)
	 */
	@Override
	public <P> P _get(Param<P> param) {
		String sessionId = getSessionId();
		String path = constructPath(param);
		Object state = hashOps.get(sessionId, path);
		if(state instanceof ModelHolder){
			ModelHolder modelHolder = (ModelHolder)state;
			return (P) ClassLoadUtils.newInstance(modelHolder.getReferredClass());
		}
		return (P)state;
	}

	/* (non-Javadoc)
	 * @see com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository#_set(com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param, java.lang.Object)
	 */
	@Override
	public <P> void _set(Param<P> param, P newState) {
		String sessionId = getSessionId();
		String path = constructPath(param);
		Model<?,?> model = param.getType().findIfNested();
		if(model != null){
			ModelHolder modelHolder = new ModelHolder();
			ModelConfig<?> modelConfig = model.getConfig();
			modelHolder.setReferredClass(modelConfig.getReferredClass());
			hashOps.put(sessionId, path, modelHolder);
		}else{
			if(newState != null)
				hashOps.put(sessionId, path, newState);
			else
				hashOps.delete(sessionId, path);
		}		
	}
	
	private String constructPath(Param<?> param){
		String rootPath = param.getRootParent().getRootDomainUri();
		String path = param.getPath();
		StringBuilder paramPath = new StringBuilder(rootPath);
		paramPath.append(Constants.SEPARATOR_URI.code).append(path);
		path = paramPath.toString();		
		return path;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private String getSessionId(){
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		return requestAttributes.getSessionId();		
	}

}
