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
package com.anthem.oss.nimbus.core.web.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.domain.command.execution.ValidationException;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;

@FeignClient(name = "platform-management-client")
public interface ClientUserRepoWebAPIClient extends ClientUserRepoAPI<ClientUser> {
	
	@Override
	@RequestMapping(method = RequestMethod.GET, value = "/p/client/user/search", params={"source","idsGuid"})
	ClientUser getUsersByIDSSourceAndGuid(@RequestParam("source") String source, @RequestParam("idsGuid") String idsGuid);
	
	@RequestMapping(method = RequestMethod.POST, value = "/p/client/user/update")
	public void saveUser(@RequestBody ClientUser clientUser) throws EntityNotFoundException;
	
	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/{clientCode}/admin/p/clientuser/_new")
	public String addClientUser(@PathVariable("clientCode") String clientCode, @RequestBody ClientUser clientUser) throws ValidationException , FrameworkRuntimeException;
	
	@Override
	@RequestMapping(value ="/{clientCode}/admin/p/clientuser/_search", method = RequestMethod.POST)
	public Page<ClientUser> getUsersByName(@PathVariable("clientCode") String clientCode, @RequestBody ClientUser clientUser) throws EntityNotFoundException;
	
	//@Override
	@RequestMapping(value ="platform/admin/p/clientuser/{id}", method = RequestMethod.DELETE)
	public void deleteClientUser(@PathVariable("id") Long id) ;
	
	@Override
	@RequestMapping(value ="/{clientCode}/admin/p/clientuser/_delete", method = RequestMethod.POST)
	public void deleteClientUser(@PathVariable("clientCode") String clientCode, @RequestBody ClientUser clientUser);
	
	@Override
	@RequestMapping(value ="/{clientCode}/admin/p/clientuser/_update", method = RequestMethod.POST)
	public String editClientUser(@PathVariable("clientCode") String clientCode, @RequestBody ClientUser clientUser);
	
	@Override
	@RequestMapping(value ="{clientcode}/admin/p/clientuser/{id}", method = RequestMethod.GET)
	public ClientUser getUserById(@PathVariable("id") String id);
}
