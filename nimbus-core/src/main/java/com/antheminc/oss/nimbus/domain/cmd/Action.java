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
package com.antheminc.oss.nimbus.domain.cmd;

import java.util.EnumSet;
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
	
	private static final EnumSet<Action> CRUD_ACTIONS = EnumSet.range(_get, _delete);
	
	public static Action getByName(String name) {
		return Stream.of(Action.values())
			.filter((action) -> StringUtils.equals(action.name(), name))
			.findFirst()
			.orElse(null);
	}
	
	
	public boolean isCrud() {
		return CRUD_ACTIONS.contains(this);
	}
}