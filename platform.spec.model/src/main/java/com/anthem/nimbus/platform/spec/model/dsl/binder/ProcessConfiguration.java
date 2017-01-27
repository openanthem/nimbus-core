/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.dsl.binder;

import org.drools.KnowledgeBase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Jayant Chaudhuri
 *
 */
@Getter @Setter
public class ProcessConfiguration {
	
	private KnowledgeBase coreRulesContainer;
	
	private KnowledgeBase viewRulesContainer;
	
}
