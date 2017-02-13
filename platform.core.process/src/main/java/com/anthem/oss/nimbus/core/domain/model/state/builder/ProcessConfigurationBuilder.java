/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.builder;

import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Component;

import com.anthem.nimbus.platform.spec.model.dsl.binder.ProcessConfiguration;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.definition.Domain;
import com.anthem.oss.nimbus.core.domain.model.state.DomainState.Model;
import com.anthem.oss.nimbus.core.domain.model.state.QuadModel;
import com.anthem.oss.nimbus.core.util.JustLogit;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component
public class ProcessConfigurationBuilder {

	private Map<String,ProcessConfiguration> domainRootToProcessConfiguration = new HashMap<String,ProcessConfiguration>();
	public static final String SEPARATOR = ".";
	private JustLogit logit = new JustLogit(getClass());
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public ProcessConfiguration getProcessConfiguration(Command command,QuadModel<?,?> quadModel){
		String commandUri = command.getAbsoluteDomainAlias(); 
		commandUri = commandUri.substring(1);
		commandUri = commandUri.replaceAll("/", SEPARATOR);
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(commandUri);
		String uri = uriBuilder.toString();
		ProcessConfiguration processConfiguration = domainRootToProcessConfiguration.get(uri);
		if(processConfiguration != null)
			return processConfiguration;
		processConfiguration = findMatchingProcessConfiguration(quadModel);
		if(processConfiguration != null){
			domainRootToProcessConfiguration.put(uri, processConfiguration);
		}
		return processConfiguration;
	}
	
	/**
	 * 
	 * @param uri
	 * @return
	 */
	private ProcessConfiguration findMatchingProcessConfiguration(QuadModel<?,?> quadModel){
		Model<?> coreModel = quadModel.getCore();
		Model<?> viewModel = quadModel.getView();
		String coreModelName = coreModel.getState().getClass().getAnnotation(Domain.class).value();
		String viewModelName = viewModel.getState().getClass().getAnnotation(ViewDomain.class).value();
		StringBuilder coreModelSearchString = new StringBuilder();
		coreModelSearchString.append("rules-sample/").append(coreModelName).append(".drl");
		StringBuilder viewModelSearchString = new StringBuilder();
		viewModelSearchString.append("rules-sample/flow_").append(viewModelName).append(".drl");
		ProcessConfiguration pc = new ProcessConfiguration();
		KnowledgeBase corekb = loadKnowledgeBase(coreModelSearchString.toString());
		KnowledgeBase viewkb = loadKnowledgeBase(viewModelSearchString.toString());
		pc.setCoreRulesContainer(corekb);
		pc.setViewRulesContainer(viewkb);
		return pc;		
	}
	
	/**
	 * 
	 * @param drlPattern
	 * @return
	 */
	private KnowledgeBase loadKnowledgeBase(String drlPattern){
		try {		
			KnowledgeBuilder kbBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			kbBuilder.add(ResourceFactory.newClassPathResource(drlPattern), ResourceType.DRL);
			KnowledgeBase kb = kbBuilder.newKnowledgeBase();
			kb.addKnowledgePackages(kbBuilder.getKnowledgePackages());
			return kb;
		} catch (Exception e) {
			logit.error(()->"Error loading drl file ");
			return null;
		}		
	}
	
}
