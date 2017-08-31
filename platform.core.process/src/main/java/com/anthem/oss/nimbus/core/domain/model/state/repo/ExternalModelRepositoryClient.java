/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
