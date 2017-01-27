/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Jayant Chaudhuri
 *
 */

public class PlatformProcessDAO {
	
	private JdbcTemplate template;
	
	@Value("${process.database.taskUpdateQuery}") 
	private String taskUpdateQuery;
	
	@Value("${process.database.executionUpdateQuery}") 
	private String executionUpdateQuery;
	

	public PlatformProcessDAO(DataSource processDataSource){
		template = new JdbcTemplate(processDataSource);
	}
	
	
	public void updatePageTaskForExecution(String runtimeId, String executionId, 
			String oldTaskId, String newTaskId, String newTaskName){
		template.update(taskUpdateQuery, newTaskId, newTaskName, runtimeId);
		template.update(executionUpdateQuery,newTaskId,executionId,oldTaskId);
	}

}
