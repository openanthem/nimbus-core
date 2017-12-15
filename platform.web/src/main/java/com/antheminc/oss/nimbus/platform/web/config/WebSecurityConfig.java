/**
 * 
 */
package com.antheminc.oss.nimbus.platform.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;


/**
 * @author Jayant Chaudhuri
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.headers().frameOptions().disable();
		// Comment out below line when enforcing authentication 
		http.authorizeRequests().anyRequest().permitAll();
	}
	
	@Bean
	@Primary
	public PlatformWebPostAuthenticationFilter authFilter(RedisOperationsSessionRepository redisOperation) {
		return new PlatformWebPostAuthenticationFilter(redisOperation);
	}
	
	@Bean
	public org.springframework.boot.web.servlet.FilterRegistrationBean postAuthenticationFilter(
			PlatformWebPostAuthenticationFilter filter) {
		org.springframework.boot.web.servlet.FilterRegistrationBean registration = new org.springframework.boot.web.servlet.FilterRegistrationBean();
		registration.setFilter(filter);
		registration.setOrder(Ordered.LOWEST_PRECEDENCE);
		return registration;
	}
	
}
