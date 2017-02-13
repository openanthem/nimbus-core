/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;

/**
 * @author Rakesh Patel
 *
 */
public interface ProcessGateway {
	
	/**
	 * 
	 * @param cmd
	 * @return
	 */
	public boolean canProcess(Command cmd);
	
	/**
	 * 
	 * @param cmdMsg
	 * @return
	 */
	public Object startProcess(CommandMessage cmdMsg);
	
	/**
	 * 
	 * @param cmdMsg
	 * @return
	 */
	public ProcessResponse executeProcess(CommandMessage cmdMsg);
	
}
