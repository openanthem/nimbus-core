/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.repo;

/**
 * @author Rakesh Patel
 *
 */
//@FeignClient(name="extRepos", url="http://localhost:8080")
public interface ExternalModelRepositoryClient {

	//TODO currently an open issue in spring-cloud-netflix: https://github.com/spring-cloud/spring-cloud-netflix/issues/1047
	//@RequestMapping(method = RequestMethod.POST, value = "/**/_search", consumes = "application/json")
	//public <T> Object _search(@RequestBody(required=false) T criteria);
}
