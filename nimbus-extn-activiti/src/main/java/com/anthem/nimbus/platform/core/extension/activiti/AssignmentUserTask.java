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