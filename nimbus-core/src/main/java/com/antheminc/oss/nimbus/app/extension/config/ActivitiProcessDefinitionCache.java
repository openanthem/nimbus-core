/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.antheminc.oss.nimbus.app.extension.config;

import org.activiti.engine.impl.persistence.deploy.DefaultDeploymentCache;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiProcessDefinitionCache extends DefaultDeploymentCache<ProcessDefinitionCacheEntry> {
	
	  public ProcessDefinitionCacheEntry findByKey(String currentKey) {
		  for(String processDefinition:cache.keySet()) {
			  String key = processDefinition.split(":")[0];
			  if(key.equals(currentKey)) {
				  ProcessDefinitionCacheEntry entry = cache.get(processDefinition);
				  return entry;
			  }
		  }
		  return null;
	  }

}
