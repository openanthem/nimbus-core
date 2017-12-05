package com.antheminc.oss.nimbus.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * @author Rakesh Patel
 *
 */
@Configuration
@EnableSpringHttpSession
public class HttpSessionConfig {
	
	@Bean
    public CookieSerializer cookieSerializer() {
            DefaultCookieSerializer serializer = new DefaultCookieSerializer();
            serializer.setCookieName("APISESSIONID"); 
            serializer.setCookiePath("/"); 
            return serializer;
    }
	
	@Bean
    public MapSessionRepository mapSessionRepository() {
        MapSessionRepository sessionRepository = new MapSessionRepository();
        return sessionRepository;
    }
	
	@Bean
    public HttpSessionStrategy httpSessionStrategy() {
		CookieHttpSessionStrategy strategy =  new CookieHttpSessionStrategy(); 
		strategy.setCookieSerializer(cookieSerializer());
		return strategy;
    }
}