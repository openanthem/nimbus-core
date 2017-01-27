/**
 * 
 */
package com.anthem.nimbus.platform.core.process.api.domain;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.dsl.CoreDomain;
import com.anthem.nimbus.platform.spec.model.dsl.binder.ProcessConfiguration;
import com.anthem.nimbus.platform.spec.model.dsl.binder.QuadModel;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.util.JustLogit;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewDomain;
import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jayant Chaudhuri
 *
 */
@Component
public class ProcessConfigurationBuilder {

	public static final String SEPARATOR = ".";
	private Map<String, ProcessConfiguration> domainRootToProcessConfiguration = new HashMap<String, ProcessConfiguration>();
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
		StateAndConfig<?, ?> coreModel = quadModel.getCore();
		StateAndConfig<?, ?> viewModel = quadModel.getView();
		String coreModelName = coreModel.getState().getClass().getAnnotation(CoreDomain.class).value();
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
		logit.info(() -> "rule file ######## " + drlPattern);
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
