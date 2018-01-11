/**
 * 
 */
package com.antheminc.oss.nimbus.core.domain.model.state.internal;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.antheminc.oss.nimbus.core.domain.command.Action;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.command.CommandMessage;
import com.antheminc.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.core.domain.model.config.ParamValue;

/**
 * @author Rakesh Patel
 *
 */
public class StaticCodeValueBasedCodeToLabelConverter extends RepoBasedConverter {

	
	@SuppressWarnings("unchecked")
	@Override
	public String serialize(String input) {
		//TODO - need to make this generic
		Command cmd = CommandBuilder.withUri("Anthem/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramValues.any().code.eq('"+input+"')").getCommand();
		cmd.setAction(Action._search);
		
		CommandMessage cmdMsg = new CommandMessage();
		cmdMsg.setCommand(cmd);
		
		MultiOutput multiOp = gateway.execute(cmdMsg);
		
		List<ParamValue> paramValues = (List<ParamValue>)multiOp.getSingleResult();
		
		if(CollectionUtils.isEmpty(paramValues))
			return input;
		
		return paramValues.stream()
					.filter((pv) -> StringUtils.equalsIgnoreCase(input, (String)pv.getCode()))
					.map((pv)-> pv.getLabel())
					.findFirst()
					.orElse(input);
					
		
	}
	
	@Override
	public String deserialize(String input) {
		throw new UnsupportedOperationException("RepoBasedCodeToDescConverter.deserialize is not implemented since it is not needed.");
	}
	
}
