/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.command;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Soham Chakravarti
 *
 */
public enum Action {
	
	/* CRUD */
	_get,		//HTTP GET - defaults to _detail
	_save,		//HTTP GET
	_new,		//HTTP POST
	_replace,	//HTTP PUT	- full update
	_update,	//HTTP PATCH- partial update
	_delete, 	//HTTP DELETE
	
	/* transient state */
	_search,
	_config,
	
	/* process */
	_process,	//Allows for custom process/work-flow definitions
	
	/* navigation */
	_nav
	;

	public static final Action DEFAULT = _get;
	
	public static Action getByName(String name) {
		return Stream.of(Action.values())
			.filter((action) -> StringUtils.equals(action.name(), name))
			.findFirst()
			.orElse(null);
	}
}