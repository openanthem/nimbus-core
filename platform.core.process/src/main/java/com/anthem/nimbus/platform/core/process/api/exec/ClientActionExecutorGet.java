/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.anthem.nimbus.platform.spec.contract.process.HierarchyMatchProcessTaskExecutor;
import com.anthem.nimbus.platform.spec.contract.process.ProcessExecutorEvents;
import com.anthem.nimbus.platform.spec.model.client.ClientEntity;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;
import com.anthem.nimbus.platform.spec.model.command.ExecuteOutput;
import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.process.ResponsePageRest;
import com.anthem.oss.nimbus.core.api.domain.state.QuadModelBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Soham Chakravarti
 *
 */
public class ClientActionExecutorGet extends AbstractProcessTaskExecutor implements HierarchyMatchProcessTaskExecutor {
	
	final String BASEURL = "http://localhost:9090/";
	
	@Autowired QuadModelBuilder qBuilder;
	
	
	@Override
	public <R> R doExecuteInternal(CommandMessage cmdMsg) {
		
		String url = BASEURL + cmdMsg.getCommand().getRootClientAlias() + "/admin/p/cliententity/" + cmdMsg.getCommand().getRefId(Type.DomainAlias)+"/nestedEntity";
			
			RestTemplate restTemplate = new RestTemplate();
			List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
			messageConverters.add(new MappingJackson2HttpMessageConverter());
			restTemplate.setMessageConverters(messageConverters);
			
			ParameterizedTypeReference<ExecuteOutput<ResponsePageRest<ClientEntity>>> typeRef = new ParameterizedTypeReference<ExecuteOutput<ResponsePageRest<ClientEntity>>>() {};
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			ObjectMapper mapper = new ObjectMapper();
			ClientEntity client = null;
			try {
				client = mapper.readValue(cmdMsg.getRawPayload(), ClientEntity.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpEntity<ClientEntity> entity = new HttpEntity<ClientEntity>(client, headers);

			ExecuteOutput<ResponsePageRest<ClientEntity>> result = restTemplate.exchange(url, HttpMethod.POST, entity, typeRef).getBody();
			
			Object cl =  result.getResult().getContent().get(0);
			
			QuadModel<?, ?> q = qBuilder.build(cmdMsg.getCommand(), cConfig->cl);
			
			return (R)q;
		
		
	}


	@Override
	public String getUri() {
		return "*/*/admin/*/p/flow_cliententity*/*addOrg*/_process/*";
	}
	

	@Override
	protected void publishEvent(CommandMessage cmdMsg, ProcessExecutorEvents e) {
		
	}
}
