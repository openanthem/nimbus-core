package com.anthem.oss.nimbus.core.integration.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author Rakesh Patel
 *
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
}
