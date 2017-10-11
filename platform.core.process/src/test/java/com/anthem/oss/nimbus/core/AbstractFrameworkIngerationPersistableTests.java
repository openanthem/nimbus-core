/**
 * 
 */
package com.anthem.oss.nimbus.core;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.springframework.mock.web.MockHttpServletRequest;

import com.anthem.oss.nimbus.core.domain.command.Action;

import test.com.anthem.nimbus.platform.utils.ExtractResponseOutputUtils;
import test.com.anthem.nimbus.platform.utils.MockHttpRequestBuilder;

/**
 * @author Soham Chakravarti
 *
 */
public abstract class AbstractFrameworkIngerationPersistableTests extends AbstractFrameworkIntegrationTests {
	
	protected static final String CORE_DOMAIN_ALIAS = "sample_core";
	protected static final String CORE_ASSOCIATED_DOMAIN_ALIAS = "sample_coreassociatedentity";
	
	protected static final String VIEW_DOMAIN_ALIAS = "sample_view";
	
	protected static final String CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + CORE_DOMAIN_ALIAS;
	protected static final String VIEW_PARAM_ROOT = PLATFORM_ROOT + "/" + VIEW_DOMAIN_ALIAS;
	
	protected static final String CORE_ASSOCIATEDPARAM_ROOT = PLATFORM_ROOT + "/" + CORE_ASSOCIATED_DOMAIN_ALIAS;
	protected static final String VIEW_CEU_PARAM_ROOT = PLATFORM_ROOT + "/ceu_sampleassociatedentity";
	
	protected static String domainRoot_refId;
	
	protected static final String BPM_CORE_DOMAIN_ALIAS = "bpmtestmodel";
	protected static final String BPM_CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_CORE_DOMAIN_ALIAS;
	
	protected static final String BPM_SF_DOMAIN_ALIAS = "bpmstatefulmodel";
	protected static final String BPM_SF_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_SF_DOMAIN_ALIAS;
	
	protected static final String BPM_CV_DOMAIN_ALIAS = "ctvsubscriberviewmodel";
	protected static final String BPM_CV_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_CV_DOMAIN_ALIAS;
	
	
	protected static final String RULE_CORE_DOMAIN_ALIAS = "ruletestcoremodel";
	protected static final String RULE_CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + RULE_CORE_DOMAIN_ALIAS;	
	
	
	public synchronized String createOrGetDomainRoot_RefId() {
		if(domainRoot_refId!=null) 
			return domainRoot_refId;
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		return domainRoot_refId;
	}
	
	@Override @After
	public void tearDown() {
		super.tearDown();
		
		domainRoot_refId = null;
	}
}
