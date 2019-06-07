/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.support.JustLogit;

/**
 * 
 * @author Dinakar Meda
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(MockitoJUnitRunner.class)
public class LoggingLevelServiceTest {

	@InjectMocks 
	private WebActionController controller;
	
	String packageName = "com.anthem.hooli.thebox";
	
	JustLogit logger = new JustLogit(packageName);
	
	@Test
	public void t00_testUpdateLoggingLevel() {
		Object response = controller.updateLogLevel("ERROR", "com.anthem.hooli");
		String val = (String)Output.class.cast(response).getValue();
		Assert.assertTrue(StringUtils.contains(val, "com.anthem.hooli from: null to: ERROR"));
		
		response = controller.updateLogLevel("INFO", "com.anthem.hooli");
		val = (String)Output.class.cast(response).getValue();
		Assert.assertTrue(StringUtils.contains(val, "com.anthem.hooli from: ERROR to: INFO"));
	}
	
	@Test
	public void t01_testDynamicLevels() {
		if(logger.getLog().isDebugEnabled()) {
			// up the level
			controller.updateLogLevel("ERROR", packageName);

			assertFalse(logger.getLog().isDebugEnabled());
			
		} else {
			
			// down the level
			controller.updateLogLevel("DEBUG", packageName);

			assertTrue(logger.getLog().isDebugEnabled());
		}
	}
	
	@Test
	public void t_02testNegative() {
		try {
			controller.updateLogLevel("INVALID", packageName);
		} catch (Exception ex) {
			fail("No exception should have been thrown for invalid logging level set attempt -> exMsg: "+ex.getMessage());
		}
	}
}
