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

import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import com.antheminc.oss.nimbus.channel.web.WebActionController;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.Output;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

/**
 * Update Logging Level at runtime.
 * 
 * @author Dinakar Meda
 * @author Soham Chakravarti
 *
 */
public class LoggingLevelService {

	private static final JustLogit logit = new JustLogit(LoggingLevelService.class);
	
	public static Output<String> setLoggingLevel(String levelInput, String packageName) {
		
		String uri = WebActionController.URI_PATTERN_P+"/loglevel";
		Command cmd = CommandBuilder.withUri(uri).getCommand();

		Output<String> returnStatus = new Output<>(uri, new ExecutionContext(cmd), Action._get, Behavior.$execute);
		
		// validate logback implementation
		ILoggerFactory loggerFactory = LoggerFactory.getILoggerFactory();
		if(!LoggerContext.class.isInstance(loggerFactory)) {
			String msg = "Only logback implemenation is suppported to change logging level dynamically, but found logger of type: "+loggerFactory.getClass();
			logit.info(()->msg);
			
			returnStatus.setValue(msg);
			return returnStatus;
		}
		
		
		LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
		
		Level currLevel = loggerContext.getLogger(packageName).getLevel();
		Level newLevel = Level.toLevel(levelInput, currLevel);
		
		loggerContext.getLogger(packageName).setLevel(newLevel);
		
		String msg = "Log Level updated for: "+packageName+" from: "+currLevel+" to: "+newLevel+" for passed in levelInput: "+levelInput;
		logit.info(()->msg);
		
		returnStatus.setValue(msg);
		return returnStatus;
		
	}
}
