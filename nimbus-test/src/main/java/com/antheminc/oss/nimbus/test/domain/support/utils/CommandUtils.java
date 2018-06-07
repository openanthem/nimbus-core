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
package com.antheminc.oss.nimbus.test.domain.support.utils;

import java.util.Arrays;

import com.antheminc.oss.nimbus.domain.cmd.Behavior;
import com.antheminc.oss.nimbus.domain.cmd.Command;
import com.antheminc.oss.nimbus.domain.cmd.CommandBuilder;

/**
 * 
 * @author Tony Lopez
 *
 */
public final class CommandUtils {

	private CommandUtils() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @param uri
	 * @param behaviors
	 * @return
	 */
	public static Command prepareCommand(String uri, Behavior... behaviors){
        final Command command = CommandBuilder.withUri(uri).getCommand();
        if(behaviors != null) {
        	Arrays.asList(behaviors).forEach((b) ->command.templateBehaviors().add(b));
        }
        return command;
    }
}
