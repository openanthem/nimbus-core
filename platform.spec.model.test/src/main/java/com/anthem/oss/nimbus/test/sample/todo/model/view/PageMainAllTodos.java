/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.todo.model.view;

import java.util.List;

import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.Model;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.antheminc.oss.nimbus.test.sample.todo.model.Todo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Model
@Getter @Setter
public class PageMainAllTodos {

	@Model
	@Getter @Setter 
	public static class SectionMyTodos {
		
		@Path(linked=false)
		private List<ViewTodo> viewTodos;
	}
	
	@MapsTo.Type(Todo.class)
	@Getter @Setter 
	public static class ViewTodo {
		
		@Path private String name;
	}
	
	@Section
	private SectionMyTodos myTodos;
}
