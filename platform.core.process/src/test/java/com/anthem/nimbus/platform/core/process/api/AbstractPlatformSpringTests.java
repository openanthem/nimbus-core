/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.anthem.oss.nimbus.core.config.BPMEngineConfig;


/**
 * @author Soham Chakravarti
 *
 */
@RunWith(SpringRunner.class)
@ComponentScan(basePackages={"com.anthem.nimbus.platform.utils.converter", "com.anthem.nimbus.platform.core.process", "com.anthem.nimbus.platform.client.extension.cm"})
@SpringBootTest(classes=BPMEngineConfig.class)
@ActiveProfiles("test")
public abstract class AbstractPlatformSpringTests {
	
}
