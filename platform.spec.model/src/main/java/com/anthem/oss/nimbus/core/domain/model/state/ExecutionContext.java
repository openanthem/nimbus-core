/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state;

import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @AllArgsConstructor
public class ExecutionContext {
	private CommandMessage commandMessage;
	private QuadModel<?,?> quadModel;
}
