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
	@RequestMapping(method = RequestMethod.GET, value = "/p/clientuser/{loginName}")
	ClientUser getUser(@PathVariable("loginName") String loginName) throws EntityNotFoundException;
	
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
