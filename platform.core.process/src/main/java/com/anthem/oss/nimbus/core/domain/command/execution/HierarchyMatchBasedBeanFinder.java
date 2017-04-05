/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.Constants;
import com.anthem.oss.nimbus.core.domain.model.state.HierarchyMatch;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@RefreshScope
public class HierarchyMatchBasedBeanFinder implements ApplicationContextAware {
	
	RepositoryService repositoryService;
	
	ApplicationContext ctx;
	
	public HierarchyMatchBasedBeanFinder(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}
	
	@Value("${process.key.regex}") 
	//private Pattern p;
	private String processBeanRegex;
	
	private Map<String,String> processKeyMap = new HashMap<String,String>();
	
	private Set<BeanKeyForMatching> deployedProcessKeys;
	
	public static final String SEPARATOR = "/";

	/**
	 * 
	 * @param command
	 * @param processName
	 * @return
	 */
	public String getSubProcessKey(Command command, String processName){
		String commandUri = command.getAbsoluteAliasTillRootDomain();
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(commandUri).append(SEPARATOR).append(processName);
		return getProcessKey(command,uriBuilder.toString(),false);
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public String getProcessKeyForExecute(Command command){
		String commandUri = command.getAbsoluteAlias();
		return getProcessKey(command,commandUri,true);
	}
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public String getProcessKeyForRootDomain(Command command){
		String commandUri = command.getRootDomainAlias();
		return getProcessKey(command,commandUri,true);
	}
	
	public String getKeyFromCommand(Command command,String commandUri, boolean appendBehaviorAndAction){
		if(commandUri.startsWith(SEPARATOR))
			commandUri = commandUri.substring(1);
		String behavior = getBehaviorKey(command.getCurrentBehavior());
		String action = command.getAction().toString();
		StringBuilder postFix = new StringBuilder();
		postFix.append(action).append(SEPARATOR).append(behavior);
		StringBuilder processUri = new StringBuilder();
		if(StringUtils.isNotBlank(command.getEvent())) {
			processUri.append(command.getEvent()+SEPARATOR);
		}
		processUri.append(commandUri);
		if(appendBehaviorAndAction){
			processUri.append(SEPARATOR).append(postFix.toString());
		}
		String processUriString = processUri.toString();
		return processUriString;
	}
	
	public String getProcessKey(Command command,String commandUri,boolean appendBehaviorAndAction){
		String processUriString = getKeyFromCommand(command, commandUri, appendBehaviorAndAction);
		String processKey = processKeyMap.get(processUriString);
		if(processKey != null)
			return processKey;
		processKey = findMatchingProcessKey(processUriString);
		if(processKey != null){
			processKeyMap.put(processUriString, processKey);
		}
		return processKey;
	}
	
	/**
	 * 
	 * @param processUri
	 * @return
	 */
	private String findMatchingProcessKey(String processUri){
		Set<BeanKeyForMatching> deployedProcessKeys = getActiveProcesskeys();
		for(BeanKeyForMatching deployedKey : deployedProcessKeys){
			if(deployedKey.matches(processUri)){
				return deployedKey.getBeanId();
			}
		}
		return  null;
	}
	
	
	/**
	 * 
	 * @return
	 */
	private Set<BeanKeyForMatching> getActiveProcesskeys(){
		if(deployedProcessKeys != null)
			return deployedProcessKeys;
		List<ProcessDefinition> deployedProcessDef = repositoryService.createProcessDefinitionQuery().active().list();
		if(deployedProcessDef == null || deployedProcessDef.size() == 0)
			return null;
		deployedProcessKeys = new HashSet<BeanKeyForMatching>(); 
		for(ProcessDefinition pd: deployedProcessDef){
			String processKey = pd.getKey();
			deployedProcessKeys.add(new BeanKeyForMatching(processKey));
		}
		return deployedProcessKeys;
	}
	
	/**
	 * 
	 * @param behavior
	 * @return
	 */
	private String getBehaviorKey(Behavior behavior){
		if(behavior==Behavior.$config
				|| behavior==Behavior.$execute
				|| behavior==Behavior.$validate){
			
			return behavior.getCode();
		}
		
		return null;
	}
	
	
	@Getter @Setter
	class BeanKeyForMatching implements Comparator<BeanKeyForMatching>{
		
		private String beanId;
		private String beanIdForMatching;
		private Pattern pattern;
		
		public BeanKeyForMatching(String beanId){
			this.beanId = beanId;
			StringBuilder matchKey = new StringBuilder();
			if(beanId.startsWith("*")) {
				 matchKey.append(".").append(beanId.replaceAll(SEPARATOR, "."));
			}
			else {
				matchKey.append(beanId.replaceAll(SEPARATOR, "."));
			}
			beanIdForMatching = matchKey.toString();
			pattern = Pattern.compile(beanIdForMatching);
		}
		
		@Override
		public int compare(BeanKeyForMatching o1, BeanKeyForMatching o2) {
			return o1.getBeanId().compareTo(o2.getBeanId());
		}
		
		/**
		 * 
		 * @param key
		 * @return
		 */
		public boolean matches(String key){
			return pattern.matcher(key).matches();
		}
	}
	
	public<T extends HierarchyMatch> T findMatchingBean(Class<T> type, Command cmd) {
		Map<String, T> beans = this.ctx.getBeansOfType(type);
		
		if(MapUtils.isEmpty(beans)) return null;
		
		List<String> beanNamesToMatch = new ArrayList<>();
		//String[] cmdUriTokens = StringUtils.split(cmd.getAbsoluteUri(), "/");
		beans.values().forEach((bean) -> {
			//String[] beanUriTokens = StringUtils.split(bean.getUri(), "/");
			//if(cmdUriTokens.length + 1 == beanUriTokens.length) {
				beanNamesToMatch.add(bean.getUri());
			//}
		});
		String matchedBeanName = findMatchingBean(cmd, beanNamesToMatch);
		
		return beans.values().stream().
				filter((bean)-> bean.getUri().equals(matchedBeanName)).
				findFirst().orElse(null);
				
	}
	
	public String findMatchingBean(Command command, List<String> beans) {

		Collections.sort(beans, (o1, o2) -> {
				
				Pattern p1 = createHierarchyMatchRegexPattern(o1);
				Pattern p2 = createHierarchyMatchRegexPattern(o2);
				
				Matcher m1 = p1.matcher(o1);
	            Matcher m2 = p2.matcher(o2);
	            
	            m1.matches();
	            m2.matches();
	            
	            if(m2.groupCount() > m1.groupCount()) {
	            	return 1;
	            }
	            else if(m2.groupCount() < m1.groupCount()) {
	            	return -1;
	            }
	            else{
		            for(int count = m1.groupCount();count > 0; count--) {
		        	   if(m2.group(count).compareTo(m1.group(count)) != 0) {
		        		   return m2.group(count).compareTo(m1.group(count));
		        	   }
		            }
	            }
	            return 0;
	        });
		
		List<BeanKeyForMatching> deployedBeans = new ArrayList<BeanKeyForMatching>();
		beans.forEach((bean)-> deployedBeans.add(new BeanKeyForMatching(bean)));
		
		String beanIdToFind = getKeyFromCommand(command, command.getAbsoluteAlias(), true);
		
		for(BeanKeyForMatching deployedBean : deployedBeans){
			if(deployedBean.matches(beanIdToFind)){
				return deployedBean.getBeanId();
			}
		}
		return null;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.ctx = applicationContext;
	}
	
	
	private Pattern createHierarchyMatchRegexPattern(String beanUri) {
		StringBuilder ptrnStr = new StringBuilder();
		String[] cmdUriTokens = StringUtils.split(beanUri, Constants.SEPARATOR_URI.code);
		for(int i = 0; i < cmdUriTokens.length; i++) {
			if(i > 0) {
				ptrnStr.append(Constants.SEPARATOR_URI.code);
			}
			ptrnStr.append(processBeanRegex);
		}
		return Pattern.compile(ptrnStr.toString());
	}
	
}
