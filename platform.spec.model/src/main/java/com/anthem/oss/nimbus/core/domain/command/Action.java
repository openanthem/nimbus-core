/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command;

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
	
	/* search */
	_search,
	
	/* process */
	_process,	//Allows for custom process/work-flow definitions
	//_lifecycle,
	
	/* navigation */
	_nav
	;

	public static final Action DEFAULT = _get;
}