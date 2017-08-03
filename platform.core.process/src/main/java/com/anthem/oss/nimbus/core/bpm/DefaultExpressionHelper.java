/**
 * 
 */
package com.anthem.oss.nimbus.core.bpm;

import org.activiti.engine.delegate.DelegateExecution;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandMessageConverter;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecutionContext;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;

/**
 * @author Jayant Chaudhuri
 *
 */
public class DefaultExpressionHelper extends AbstractExpressionHelper {
	
	private final CommandMessageConverter converter;
	
	public DefaultExpressionHelper(BeanResolverStrategy beanResolver) {
		super(beanResolver);
		this.converter = beanResolver.get(CommandMessageConverter.class);
	}
	public String _json(ExecutionContext eCtx, DelegateExecution execution, Object... args) {
		return converter.convert(args[0]);
	}
	
	public Object[] _array(ExecutionContext eCtx, DelegateExecution execution, Object... args) {
		return args;
	}	
	
	public String _buildUrl(ExecutionContext eCtx, DelegateExecution execution, String... args) {
		StringBuilder url = new StringBuilder(args[0]);
		url.append("?");
		if(args.length > 1){
			for(int i = 1; i < args.length-1; i=i+2){
				url.append(args[i]).append("=").append(args[i+1]).append("&");
			}
		}
		return url.toString();
	}	
	
	
	public String _concat(ExecutionContext eCtx, DelegateExecution execution, String... args) {
		StringBuilder result = new StringBuilder();
		for(String arg:args){
			result.append(arg);
		}
		return result.toString();
	}		
	
	
	public Object _getState(ExecutionContext eCtx, DelegateExecution execution, Param<?>... args) {
		return args[0].getState();
	}	
}
