/**
 * 
 */
package com.antheminc.oss.nimbus.app.extension.config;

import org.activiti.engine.impl.persistence.deploy.DefaultDeploymentCache;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;

/**
 * @author Jayant Chaudhuri
 *
 */
public class ActivitiProcessDefinitionCache extends DefaultDeploymentCache<ProcessDefinitionCacheEntry> {
	
  public ProcessDefinitionCacheEntry find(String id) {
	  String processName = id.split(":")[0];
	  for(String key:cache.keySet()) {
		  String processDefinition = key.split(":")[0];
		  if(processDefinition.equals(processName)) {
			  return cache.get(key);
		  }
	  }
	  return null;
  }

}
