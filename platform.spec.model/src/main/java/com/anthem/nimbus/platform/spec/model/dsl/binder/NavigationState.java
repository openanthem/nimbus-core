/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.anthem.nimbus.platform.spec.model.dsl.Domain;
import com.anthem.nimbus.platform.spec.model.dsl.Execution;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Jayant Chaudhuri
 *
 */

@Domain("navigationstate")
@Execution.Input.Default @Execution.Output.Default
@ToString
@Getter @Setter
public class NavigationState implements Serializable{
	private static final long serialVersionUID = 1L;
	private PageHolder pageHolder;
}
