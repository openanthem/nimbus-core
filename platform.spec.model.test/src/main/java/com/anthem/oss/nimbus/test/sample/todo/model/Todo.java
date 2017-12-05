/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.todo.model;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.Repo;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Domain("todo")
@Repo(Repo.Database.rep_mongodb)
@Getter @Setter
public class Todo extends AbstractEntity.IdString {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private List<TodoItem> items;
}
