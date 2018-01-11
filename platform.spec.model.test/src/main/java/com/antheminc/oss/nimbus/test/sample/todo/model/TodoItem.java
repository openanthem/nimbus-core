/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.todo.model;

import java.util.Date;

import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class TodoItem extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private String desc;
	
	private Date dueBy;
	
	private boolean isDone;
}
