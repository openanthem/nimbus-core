package com.anthem.oss.nimbus.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
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
	@Value("${security.oauth2.resource.tokenInfoUri}")
	private String tokenInfoUri;
	@Value("${security.oauth2.client.clientId}")
	private String clientId;
	@Value("${security.oauth2.client.clientSecret}")
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