package com.anthem.nimbus.platform.core.process.api;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import redis.embedded.RedisServer;


/**
 * RedisIntegrationTestConfig class is to provide a redis instance within the scope of a test case.
 * To use this config, add this class in the classes section of @SpringBootTest, or use the component scan.
 * As a post construct , this class with start the redis server on the annotated port and on destroy it will stop
 * the instance of redis. 
 * The embedded redis is being used from an open source library - redis.embedded
 * 
 * @author Swetha Vemuri
 *
 */
@Configuration
@Profile("test")
public class RedisIntegrationTestConfig {
	
	@Value("${spring.redis.port}")
	private int redisPort;
	
	private RedisServer redisServer;
	
	@PostConstruct
	public void startRedis() throws IOException {
		if(redisServer == null){
			redisServer = new RedisServer(redisPort);
			if(!redisServer.isActive()) {
				redisServer.start();
			}
		}
	}

	@PreDestroy
	public void stopRedis() {
		redisServer.stop();
	}
}