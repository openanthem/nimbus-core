/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.command;

import java.util.HashMap;
import java.util.Map;

import com.anthem.nimbus.platform.spec.model.command.CommandElement.Type;

import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@RequiredArgsConstructor
public class CommandWalker {

	final private Command cmd;
	
	final StringBuilder builder = new StringBuilder();
	
	
	private enum Option {
		alias,
		refId,
		uri;
	}
	
	
	
	@RequiredArgsConstructor
	public class Attacher {
		
		private Map<Type, Option> includeTypes = new HashMap<>();
		
		private boolean includeAction = false;
		
		private boolean includeEvent = false;
		
		
		public Attacher addAction() {
			includeAction = true;
			return this;
		}
		
		public Attacher addEvent() {
			includeEvent = true;
			return this;
		}
		
		@Override
		public String toString() {
			return "";
		}
		
	}
	
	
	public Command getCommand() {
		return cmd;
	}
	
	public static CommandWalker get(Command cmd) {
		return new CommandWalker(cmd);
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public Attacher alias(Type type) {
		Attacher a = this.new Attacher();
		a.includeTypes.put(type, Option.alias);
		return a;
	}
	
}
