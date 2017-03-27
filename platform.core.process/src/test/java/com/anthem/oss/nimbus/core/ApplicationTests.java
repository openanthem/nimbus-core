package com.anthem.oss.nimbus.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.Neo4jConfig;

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("test")
@EnableAutoConfiguration
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}
