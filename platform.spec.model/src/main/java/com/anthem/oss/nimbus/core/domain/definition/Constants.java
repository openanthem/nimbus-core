/**
 *
 *  Copyright 2012-2017 the original author or authors.
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
/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.definition;

/**
 * @author Soham Chakravarti
 *
 */
public enum Constants {

	MARKER_URI_PLATFORM("p"),
	MARKER_URI_BEHAVIOR("b"),
	MARKER_COLLECTION_ELEM_INDEX("{index}"),
	
	MARKER_PLATFROM_EXPR_PREFIX("<!"),
	MARKER_PLATFROM_EXPR_SUFFIX("!>"),
	
	
	MARKER_SESSION_SELF("#self"),
	MARKER_COMMAND_PARAM_CURRENT_SELF("#this"),
	MARKER_REF_ID("#refId"),
	MARKER_ELEM_ID("#elemId"),
	MARKER_COL_PARAM("col"),
	MARKER_COL_PARAM_EXPR("<!col!>"),
	
	SEPARATOR_URI("/"),
	
	SEPARATOR_URI_PLATFORM(SEPARATOR_URI.code + MARKER_URI_PLATFORM.code),	/* /p */
	SEGMENT_PLATFORM_MARKER(SEPARATOR_URI_PLATFORM.code + SEPARATOR_URI.code),	/* /p/ */
	
	
	SEPARATOR_URI_VALUE(":"),
	SEPARATOR_URI_PARENT(".."),
	SEPARATOR_URI_ROOT_DOMAIN(".d"),
	SEPARATOR_URI_ROOT_EXEC(".e"),
	
	SEPARATOR_CONFIG_ATTRIB("#"),
	SEPARATOR_UNIQUE_KEYGEN("^"),
	SEPARATOR_BEHAVIOR_START("$"),
	
	SEPARATOR_AND("And"),
	SEPARATOR_MAPSTO(".m"),
	
	PREFIX_FLOW("flow_"),
	PREFIX_DEFAULT("default."),
	PREFIX_EVENT("e"),
	PREFIX_EVENT_URI("e"+"_"),
	
	SUFFIX_PROPERTY_STATE("State"),
	
	CODE_VALUE_CONFIG_DELIMITER("-"),
	PARAM_VALUES_URI_PREFIX("*/*/*/p/"),
	PARAM_VALUES_URI_SUFFIX("/_lookup"),
	
	KEY_FUNCTION("fn"),
	KEY_FUNCTION_NAME("name"),
	
	KEY_NAV_ARG_PAGE_ID("pageId"),
	
	KEY_FN_INITSTATE_ARG_TARGET_PATH("target"),
	KEY_FN_INITSTATE_ARG_JSON("json"),
	
	KEY_FN_PARAM_ARG_EXPR("expr"),
	
	KEY_EXECUTE_PROCESS_CTX("processContext"),
	KEY_EXECUTE_EVAL_ARG("eval"),
	KEY_EXECUTE_PROCESS_ID("processId"),
	
	REQUEST_PARAMETER_MARKER("?")
	;
	
	
	
	public final String code;
	
	
	private Constants(String code) {
		this.code = code;
	}
	
	@Override
	public String toString() {
		return "["+name() + " : " +code+"]";
	}
	
}
