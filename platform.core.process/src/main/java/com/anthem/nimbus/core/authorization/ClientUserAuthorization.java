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
package com.anthem.nimbus.core.authorization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput;
import com.anthem.oss.nimbus.core.domain.model.state.EntityNotFoundException;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Rakesh Patel
 *
 */
@ConfigurationProperties(prefix="user")
@Getter @Setter
public class ClientUserAuthorization implements AuthorizationService {

	public RestTemplate restTemplate = new RestTemplate();
	
	protected JustLogit logit = new JustLogit(this.getClass());	

	Map<String, List<String>> permissionToActions;
	
	@Override
	public boolean hasAccess(Command command) throws AuthorizationException {
		//return validateUserAccess(command);
		
		ClientUser cu = (ClientUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Assumes authentication has created the principal of type ClientUser
		
//		clientUserAccessBehavior clientUserAccessBehavior = cu.newBehaviorInstance(ClientUserAccessBehavior.class);
//		return clientUserAccessBehavior.canUserPerform(command);
		return true;
		
	}
	
	 

	private boolean validateUserAccess(Command command) throws AuthorizationException {
		ClientUser clientUser;
		Map<String, String> loginName= new HashMap<>();
		loginName.put("loginName", "swetha");
		String url = "http://localhost:9090/p/clientuser/{loginName}";
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);

		ParameterizedTypeReference<ExecuteOutput<ClientUser>> typeRef = new ParameterizedTypeReference<ExecuteOutput<ClientUser>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ClientUser> entity = new HttpEntity<>(headers);

		ExecuteOutput<ClientUser> response = restTemplate.exchange(url, HttpMethod.GET, entity, typeRef,loginName).getBody();
		clientUser = response.getResult();
		
		//String url = "platform-management-client/platform/admin/p/client/{username}";
		//clientUser = restTemplate.getForObject(url,ClientUser.class,loginName);
		//System.out.println(response.getResult());
		try {
			System.out.println("step 2 - in validateUserAccess");
		//	clientUser = clientUserRepoApi.getUser(command.getClientUserId());
			System.out.println("step 4- after feign client call");
			Assert.notNull(clientUser, "ClientUser cannot be null - returned null for: " + command.getClientUserId());
		} catch (EntityNotFoundException e) {
			throw new UserNotFoundException("clientUserRepoApi.getUser failed to return ClientUser for: " + command, e);
		}
//			}
		if (clientUser==null){
			throw new UserNotFoundException("No  matching client user found in our records");
		}
//		ClientUserAccessBehavior clientUserAccessBehavior = clientUser.newBehaviorInstance(ClientUserAccessBehavior.class);
//		return clientUserAccessBehavior.canUserPerform(command);
		return true;
	}
}
