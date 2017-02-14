/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandMessage;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.definition.InvalidConfigException;
import com.anthem.oss.nimbus.core.util.JustLogit;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@Getter
@Component("default.processGateway")
@EnableConfigurationProperties
@ConfigurationProperties(prefix="dsl.behavior")
@RefreshScope
public class DefaultProcessGateway extends AbstractProcessGateway implements ApplicationContextAware {
	
	public static final String DEFAULT_EXECUTOR_BEAN_PREFIX = "";
	
	@Setter private String executorBeanPrefix = DEFAULT_EXECUTOR_BEAN_PREFIX;
	
	private Collection<ProcessGateway> delegates;
	
	private Map<String, ProcessTaskExecutor> actionProcessExecs = new HashMap<>();
	
	private Map<Behavior, List<Behavior>> extensions = new HashMap<>();
	
	private ApplicationContext ctx;
	
	@Autowired
	private HierarchyMatchBasedBeanFinder hierarchyMatchBeanLoader;
	
	private JustLogit logit = new JustLogit(this.getClass());
	
	/**
	 * Finds beans of type {@linkplain ProcessGateway} and adds to delegate list. <br>
	 * Sub-classes can override this behavior if needed or choose to provide an alternate implementation for {@link DefaultProcessGateway#getDelegates()} 
	 */
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException {
		this.ctx = ctx;
		
		loadDelegates();
	}
	
	//==@EventListener(ContextRefreshedEvent.class)
	protected void loadDelegates() {
		Map<String, ProcessGateway> delegates;
		try {
			delegates = ctx.getBeansOfType(ProcessGateway.class);
		} catch (BeansException ex) {
			logit.info(()->"No bean defined for type: "+ ProcessGateway.class);
			delegates = Collections.emptyMap();
		}
		if(MapUtils.isEmpty(delegates)) return;
		
		delegates.values().removeIf((t)-> (t instanceof DefaultProcessGateway));
		this.delegates = delegates.values();
	} 
	
	/**
	 * Returns true as it can hand-off to configured delegates as needed. Meant for use as the initial gateway in chain.
	 */
	@Override
	public boolean canProcess(Command cmd) {
		return true;
	}
	
	protected ProcessTaskExecutor lookupExecutor(String prefix, String e, Action a, Behavior b) {
		if(e==null) return lookupExecutor(prefix, a, b);
		
		return Optional
			.ofNullable(lookupExecutor(prefix+e, a, b))	//pattern: default.e#pre_new$execute
			.orElse(lookupExecutor(prefix+e));			//pattern: default.e#pre
	}
	
	protected ProcessTaskExecutor lookupExecutor(String prefix, Action a, Behavior b) {
		String abName = prefix + a.name() + b.name();
		ProcessTaskExecutor abExec = lookupExecutor(abName);
		if(abExec!=null) return abExec;
		
		String bName = prefix + b.name();
		ProcessTaskExecutor bExec = lookupExecutor(bName);
		return bExec;
	}

	protected ProcessTaskExecutor lookupExecutor(String name) {
		if(actionProcessExecs.containsKey(name)) {
			return actionProcessExecs.get(name);
		}
		
		if(!ctx.containsBean(name)) return null;
		
		ProcessTaskExecutor exec = ctx.getBean(name, ProcessTaskExecutor.class);
		actionProcessExecs.put(name, exec);
		return exec;
	}
	
	
	
	@EventListener(CommandMessage.class)
	@Async
	public void executeProcessAsync(CommandMessage cmdMsg) {
		executeProcess(cmdMsg);
	}
	
	public ProcessResponse executeProcess(CommandMessage cmdMsg) {
		ProcessGateway delegate = findDelegate(cmdMsg.getCommand());
		if(delegate!=null) return delegate.executeProcess(cmdMsg);
		
		Command cmd = cmdMsg.getCommand();
		
		MultiExecuteOutput multiExecOutput = new MultiExecuteOutput();
		
		// add behavior extenstions if available TODO review
		if(MapUtils.isNotEmpty(extensions)) {
//			ListIterator<Behavior> it = cmd.getBehaviors().listIterator();
//			while(it.hasNext()) {
//				Behavior itNext = it.next();
//				if(CollectionUtils.isNotEmpty(extensions.get(itNext))) {
//					cmd.getBehaviors().addAll(extensions.get(itNext));
//				}
//			}
			extensions.forEach((k,v) -> cmd.getBehaviors().addAll(v));
		}
		
		for(int i=0; i<cmd.getBehaviors().size(); i++) {
			cmd.setCurrentBehaviorIndex(i);
			
			ExecuteOutput.BehaviorExecute<Object> execOutput = executeByBehavior(cmdMsg);
			multiExecOutput.add(execOutput);
		}
		
		ProcessResponse prcsResp = new ProcessResponse();
		prcsResp.setResponse(multiExecOutput);
		
		return prcsResp;
	}
	
	protected ExecuteOutput.BehaviorExecute<Object> executeByBehavior(CommandMessage cmdMsg) {
		final Command cmd = cmdMsg.getCommand();
		final Behavior b = cmd.getCurrentBehavior();
		
		ProcessTaskExecutor resolvedTaskExec = Optional
				.ofNullable((ProcessTaskExecutor)hierarchyMatchBeanLoader.findMatchingBean(HierarchyMatchProcessTaskExecutor.class, cmd))
				.orElse(
					Optional.ofNullable(lookupExecutor(executorBeanPrefix, cmd.getEvent(), cmd.getAction(), b))
						.orElse(
							Optional.ofNullable(lookupExecutor(Constants.PREFIX_DEFAULT.code, cmd.getEvent(), cmd.getAction(), b))
								.orElseThrow(()->new InvalidConfigException("No "+ProcessGateway.class.getSimpleName()+" or "+ProcessTaskExecutor.class.getSimpleName()+" were found for cmd: "+cmd))
						)
				);
		
		
		final Object resp = resolvedTaskExec.doExecute(cmdMsg);
		return new ExecuteOutput.BehaviorExecute<Object>(b, resp);
	}
	
	/**
	 * Returns first matching delegate in the list by calling {@link ProcessGateway#canProcess(Command)} 
	 */
	protected ProcessGateway findDelegate(Command cmd) {
		if(CollectionUtils.isEmpty(getDelegates())) return null;
		
		for(ProcessGateway pg : getDelegates()) {
			if(pg.canProcess(cmd)) return pg;
		}
		
		return null;
	}

}
