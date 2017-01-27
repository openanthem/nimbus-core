/**
 * 
 */
package com.anthem.nimbus.platform.spec.contract.process;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandMessage;

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
