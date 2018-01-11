/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.todo.model.view;

import com.antheminc.oss.nimbus.core.domain.definition.Domain;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Page;

import lombok.Getter;
import lombok.Setter;

@Domain("flow_todo")
@Getter @Setter
public class FlowTodo {

	@Page PageMainAllTodos mainPage;
}
