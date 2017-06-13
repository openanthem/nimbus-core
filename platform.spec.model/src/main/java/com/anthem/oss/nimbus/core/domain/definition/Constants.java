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
	
	SEPARATOR_URI("/"),
	
	SEPARATOR_URI_PLATFORM(SEPARATOR_URI.code + MARKER_URI_PLATFORM.code),	/* /p */
	SEGMENT_PLATFORM_MARKER(SEPARATOR_URI_PLATFORM.code + SEPARATOR_URI.code),	/* /p/ */
	
	
	SEPARATOR_URI_VALUE(":"),
	SEPARATOR_CONFIG_ATTRIB("#"),
	SEPARATOR_UNIQUE_KEYGEN("^"),
	
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
	KEY_NAV_ARG_PAGE_ID("pageId"),
	
	KEY_EXECUTE_PROCESS_CTX("processContext"),
	KEY_EXECUTE_EVAL_ARG("eval"),
	
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
