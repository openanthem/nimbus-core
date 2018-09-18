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
/**
 * 
 */
package com.anthem.nimbus.platform.core.extension.activiti;

import org.activiti.designer.integration.annotation.Help;
import org.activiti.designer.integration.annotation.Property;
import org.activiti.designer.integration.annotation.TaskName;
import org.activiti.designer.integration.annotation.TaskNames;
import org.activiti.designer.integration.servicetask.PropertyType;
import org.activiti.designer.integration.usertask.AbstractCustomUserTask;

/**
 * 
 * @author Jayant Chaudhuri
 *
 */
@Help(displayHelpShort = "Represents a task that can be assigned to a user/queue", displayHelpLong = "Represents a task that can be assigned to a user/queue")
@TaskNames(
    {
      @TaskName(locale = "en", name = "Represents a task that can be assigned to a user/queue"),
    }
)
public class AssignmentUserTask extends AbstractCustomUserTask {
	
  public static final String KEY = "assignment"; 

  @Property(type=PropertyType.MULTILINE_TEXT, displayName="URL", required=true)
  @Help(displayHelpShort="Command URL")
  private String url;	
  
  @Property(type=PropertyType.MULTILINE_TEXT, displayName="Exit Condition", required=false)
  @Help(displayHelpShort="Exit Condition")
  private String exitCondition;
  
  @Property(type=PropertyType.MULTILINE_TEXT, displayName="Eval URL", required=false)
  @Help(displayHelpShort="URL to be evaluated on State Change")
  private String evalURLs;
  
  @Override
  public String contributeToPaletteDrawer() {
    return "Nimbus - User Task";
  }

  public String getName() {
    return "Assignment Task";
  }
  
  @Override
  public String getSmallIconPath() {
    return "icons/userTask.png";
  }

}