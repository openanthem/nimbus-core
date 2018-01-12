/**
 *
 *  Copyright 2012-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.repository;

/**
 * @author Jayant Chaudhuri
 *
 */
public class SessionCacheRepository { //implements ParamStateRepository {
	
//    @Autowired
//    @Qualifier("sessionRedisTemplate")
//    private RedisTemplate<Object,Object> template;
//    
//    private HashOperations<Object,Object,Object> hashOps;
//    
//    @PostConstruct
//    private void init() {
//        hashOps = template.opsForHash();
//    }
//
//	/* (non-Javadoc)
//	 * @see com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository#_get(com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param)
//	 */
//	//@Override
//	public <P> P _get(Param<P> param) {
//		String sessionId = getSessionId();
//		String path = constructPath(param);
//		Object state = hashOps.get(sessionId, path);
//		if(state instanceof ModelHolder){
//			ModelHolder modelHolder = (ModelHolder)state;
//			return (P) ClassLoadUtils.newInstance(modelHolder.getReferredClass());
//		}
//		return (P)state;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.anthem.nimbus.platform.spec.contract.repository.ParamStateRepository#_set(com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param, java.lang.Object)
//	 */
//	//@Override
//	public <P> void _set(Param<P> param, P newState) {
//		String sessionId = getSessionId();
//		String path = constructPath(param);
//		Model<?> model = param.getType().findIfNested();
//		if(model != null){
//			ModelHolder modelHolder = new ModelHolder();
//			ModelConfig<?> modelConfig = model.getConfig();
//			modelHolder.setReferredClass(modelConfig.getReferredClass());
//			hashOps.put(sessionId, path, modelHolder);
//		}else{
//			if(newState != null)
//				hashOps.put(sessionId, path, newState);
//			else
//				hashOps.delete(sessionId, path);
//		}		
//	}
//	
//	private String constructPath(Param<?> param){
//		String rootPath = param.getRootParent().getRootDomainUri();
//		String path = param.getPath();
//		StringBuilder paramPath = new StringBuilder(rootPath);
//		paramPath.append(Constants.SEPARATOR_URI.code).append(path);
//		path = paramPath.toString();		
//		return path;
//	}
//	
//	
//	/**
//	 * 
//	 * @return
//	 */
//	private String getSessionId(){
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		return requestAttributes.getSessionId();		
//	}

}
