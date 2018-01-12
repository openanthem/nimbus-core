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
package com.anthem.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

/**
 * @author Rohit Bajaj
 * 
 * Enable this configuration to:
 * 1. Switch on security at api layer.
 * 2. Use zuul token relay for jwt token authentication.
 *
 */
//@Configuration
//@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter 
{
//	@Value("${security.oauth2.resource.tokenInfoUri}")
	private String tokenInfoUri;
//	@Value("${security.oauth2.client.clientId}")
	private String clientId;
//	@Value("${security.oauth2.client.clientSecret}")
	private String clientSecret;
	 
	@Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }
    @Bean
    @Primary
    public RemoteTokenServices tokenServices() {
    	RemoteTokenServices defaultTokenServices = new RemoteTokenServices();
    	defaultTokenServices.setClientId(clientId);
    	defaultTokenServices.setClientSecret(clientSecret);
    	defaultTokenServices.setCheckTokenEndpointUrl(tokenInfoUri);
        return defaultTokenServices;
    }
   
}