/**
 *  Copyright 2016-2018 the original author or authors.
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
 *
 */
public class LoggingLevelService {

	public static Output<String> setLoggingLevel(String level, String packageName) {
		String status = "Log Level updated to - " + level;
		String uri = WebActionController.URI_PATTERN_P+"/loglevel";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		
		Output<String> returnStatus = new Output<>(uri, new ExecutionContext(cmd), Action._get, Behavior.$execute);
		returnStatus.setValue(status);
		
		LoggerContext loggerContext = (LoggerContext)LoggerFactory.getILoggerFactory();
        if (level.equalsIgnoreCase("DEBUG")) {
            loggerContext.getLogger(packageName).setLevel(Level.DEBUG);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("INFO")) {
            loggerContext.getLogger(packageName).setLevel(Level.INFO);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("TRACE")) {
            loggerContext.getLogger(packageName).setLevel(Level.TRACE);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("WARN")) {
            loggerContext.getLogger(packageName).setLevel(Level.WARN);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("ERROR")) {
            loggerContext.getLogger(packageName).setLevel(Level.ERROR);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("OFF")) {
            loggerContext.getLogger(packageName).setLevel(Level.OFF);
            return returnStatus; 
        } else if (level.equalsIgnoreCase("ALL")) {
            loggerContext.getLogger(packageName).setLevel(Level.ALL);
            return returnStatus; 
        } else {
        	returnStatus.setValue("Invalid Log Level - " + level);
            return returnStatus;
        }
	}
}
