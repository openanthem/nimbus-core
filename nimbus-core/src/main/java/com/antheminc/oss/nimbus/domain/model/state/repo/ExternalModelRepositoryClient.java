/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.domain.model.state.repo;

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
