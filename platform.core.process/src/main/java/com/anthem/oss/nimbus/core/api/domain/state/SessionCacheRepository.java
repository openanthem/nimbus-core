/**
 * 
 */
package com.anthem.oss.nimbus.core.api.domain.state;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.anthem.nimbus.platform.spec.model.util.ModelsTemplate;
import com.anthem.nimbus.platform.spec.session.ModelHolder;
import com.anthem.oss.nimbus.core.domain.Action;
import com.anthem.oss.nimbus.core.domain.Constants;
import com.anthem.oss.nimbus.core.domain.model.ModelConfig;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.repo.ParamStateRepository;

/**
 * @author Jayant Chaudhuri
 *
 */
//@Component("default.param.state.rep_session")
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
			return (P) ModelsTemplate.newInstance(modelHolder.getReferredClass());
		}
		return (P)state;
	}

	/* (non-Javadoc)
	 * @see com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository#_set(com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param, java.lang.Object)
	 */
	@Override
	public <P> Action _set(Param<P> param, P newState) {
		String sessionId = getSessionId();
		String path = constructPath(param);
		
		if(param.getType().isNested()){
			Model<?> model = param.getType().findIfNested().getModel();
			ModelHolder modelHolder = new ModelHolder();
			ModelConfig<?> modelConfig = model.getConfig();
			modelHolder.setReferredClass(modelConfig.getReferredClass());
			hashOps.put(sessionId, path, modelHolder);
			
			//TODO change detection
			return Action._replace;
		}else{
			if(newState != null) {
				hashOps.put(sessionId, path, newState);
				return Action._replace;
			}
			else {
				hashOps.delete(sessionId, path);
				return Action._delete;
			}
		}		
	}
	
	private String constructPath(Param<?> param){
		String rootPath = param.getRootModel().getRootDomainUri();
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
