/**
 * 
 */
package com.anthem.oss.nimbus.core.entity.process;

import java.io.Serializable;

import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.definition.Execution;

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
public class PageNavigation implements Serializable{
	private static final long serialVersionUID = 1L;
	private PageHolder pageHolder;
}
