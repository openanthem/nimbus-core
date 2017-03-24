package com.anthem.nimbus.platform.core.process.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BPMEngineConfig.class)
@WebAppConfiguration
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

}

