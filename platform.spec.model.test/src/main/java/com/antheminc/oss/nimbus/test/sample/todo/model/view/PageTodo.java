/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.todo.model.view;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.test.sample.todo.model.Todo;
import com.antheminc.oss.nimbus.test.sample.todo.model.TodoItem;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(Todo.class)
@Getter @Setter
public class PageTodo {

	@MapsTo.Type(Todo.class)
	@Getter @Setter
	public static class SectionItems {
		
		@Path private List<TodoItem> items;	
	}
	
	@Path private String name;
	
	private SectionItems itemsSection;
}
