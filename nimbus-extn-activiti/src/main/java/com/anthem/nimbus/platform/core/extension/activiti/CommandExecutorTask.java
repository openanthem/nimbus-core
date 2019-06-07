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
/**
 * 
 */
package com.anthem.nimbus.platform.core.extension.activiti;

import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.Runtime;
import org.activiti.designer.integration.servicetask.AbstractCustomServiceTask;
import org.activiti.designer.integration.servicetask.PropertyType;

/**
 * @author Jayant Chaudhuri
 *
 */
@Runtime(javaDelegateExpression="${commandExecutorTaskDelegate}")
public class CommandExecutorTask extends AbstractCustomServiceTask {
	
	@Property(type=PropertyType.MULTILINE_TEXT, displayName="URL", required=true)
	@Help(displayHelpShort="Command URL")
	private String url;	

	@Override
	public String getName() {
		return "Command Executor Task";
	}
	
	@Override
	public String contributeToPaletteDrawer() {
	    return "Nimbus - Service Task";
	}
	
	@Override
	public String getSmallIconPath() {
	    return "icons/serviceTask.png";
	}	

}
