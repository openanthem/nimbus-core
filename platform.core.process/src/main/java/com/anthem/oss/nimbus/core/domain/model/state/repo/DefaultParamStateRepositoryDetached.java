/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo;

import java.beans.PropertyDescriptor;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.FrameworkRuntimeException;
import com.anthem.oss.nimbus.core.InvalidOperationAttemptedException;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecutorGateway;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandPathVariableResolver;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;
import com.anthem.oss.nimbus.core.domain.definition.MapsTo.Mode;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.ListElemParam;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.util.JustLogit;
import com.anthem.oss.nimbus.core.utils.JavaBeanHandler;

/**
 * @author Soham Chakravarti
 *
 */
public class DefaultParamStateRepositoryDetached implements ParamStateRepository {

	private JustLogit logit = new JustLogit(getClass());
	
	protected final CommandExecutorGateway gateway;
	
	protected final CommandPathVariableResolver pathVariableResolver;
	
	public DefaultParamStateRepositoryDetached(BeanResolverStrategy beanResolver) {
		this.gateway = beanResolver.get(CommandExecutorGateway.class);
		this.pathVariableResolver = beanResolver.get(CommandPathVariableResolver.class);
	}
	
	@Override
	public <P> P _get(Param<P> param) {
		
		String completeUri = param.getRootExecution().getRootCommand().getRelativeUri(param.getConfig().findIfMapped().getPath().value());
		String resolvedUri = this.pathVariableResolver.resolve(param, completeUri);
		Command cmd = CommandBuilder.withUri(resolvedUri).getCommand();
		CommandMessage cmdMsg = new CommandMessage(cmd, null);
		MultiOutput multiOp = this.gateway.execute(cmdMsg);
		Param<P> pState = (Param<P>)multiOp.getSingleResult();
		return pState.getLeafState();
	
	}
	
	@Override
	public <P> Action _set(Param<P> param, P newState) {
		logit.trace(()->"_set@enter -> param.path: "+param.getPath()+" newState: "+newState);
		
		throw new UnsupportedOperationException("The mapped Detached param only supports _get as of now, _set is not implemented. Param path is: "+param.getPath());
	}

}
