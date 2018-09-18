/**
 *  Copyright 2016-2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

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
	
	protected static final String USER_DOMAIN_ALIAS = "clientuser";
    protected static final String USER_PARAM_ROOT = PLATFORM_ROOT + "/" + USER_DOMAIN_ALIAS;
    
    protected static final String USERROLE_DOMAIN_ALIAS = "userrole";
    protected static final String USEREOLE_PARAM_ROOT = PLATFORM_ROOT + "/" + USERROLE_DOMAIN_ALIAS;
    
    protected static final String CORE_ACCESS_DOMAIN_ALIAS = "sample_core_access"; 
    protected static final String CORE_PARAM_ACCESS_ROOT = PLATFORM_ROOT + "/" + CORE_ACCESS_DOMAIN_ALIAS;
    
    protected static final String VIEW_ACCESS_DOMAIN_ALIAS = "sample_core_access_view"; 
    protected static final String VIEW_PARAM_ACCESS_ROOT = PLATFORM_ROOT + "/" + VIEW_ACCESS_DOMAIN_ALIAS;

    
    protected static final String CORE_NESTED_CONFIG_DOMAIN_ALIAS = "sample_core_nested"; 
    protected static final String CORE_NESTED_CONFIG_ROOT = PLATFORM_ROOT + "/" + CORE_NESTED_CONFIG_DOMAIN_ALIAS;
	
	protected static final String CORE_ASSOCIATEDPARAM_ROOT = PLATFORM_ROOT + "/" + CORE_ASSOCIATED_DOMAIN_ALIAS;
	protected static final String VIEW_CEU_PARAM_ROOT = PLATFORM_ROOT + "/ceu_sampleassociatedentity";
	
	protected static final String VIEW_WITHBPMN_PARAM_ROOT = PLATFORM_ROOT + "/samplebpmn";
	
	protected static Long domainRoot_refId;
	
	protected static final String BPM_CORE_DOMAIN_ALIAS = "bpmtestmodel";
	protected static final String BPM_CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_CORE_DOMAIN_ALIAS;
	
	protected static final String BPM_SF_DOMAIN_ALIAS = "bpmstatefulmodel";
	protected static final String BPM_SF_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_SF_DOMAIN_ALIAS;
	
	protected static final String BPM_CV_DOMAIN_ALIAS = "ctvsubscriberviewmodel";
	protected static final String BPM_CV_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_CV_DOMAIN_ALIAS;
	
	
	protected static final String RULE_CORE_DOMAIN_ALIAS = "ruletestcoremodel";
	protected static final String RULE_CORE_PARAM_ROOT = PLATFORM_ROOT + "/" + RULE_CORE_DOMAIN_ALIAS;	
	
	protected static final String BPM_DP_DOMAIN_ALIAS = "testtaskcontainermodel";
	protected static final String BPM_DP_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_DP_DOMAIN_ALIAS;	
	
	protected static final String BPM_DPT_DOMAIN_ALIAS = "testusertaskmodel";
	protected static final String BPM_DPT_PARAM_ROOT = PLATFORM_ROOT + "/" + BPM_DPT_DOMAIN_ALIAS;	
	
	protected static final String SAMPLE_DOMAIN_ALIAS = "sample_entity";
	
	protected static Long sampleEntity_refId;
	
	public synchronized Long createOrGetDomainRoot_RefId() {
		if(domainRoot_refId!=null) 
			return domainRoot_refId;
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_PARAM_ROOT).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		domainRoot_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		return domainRoot_refId;
	}
	
	public synchronized Long createOrGetSampleEntity_RefId() {
		final String VIEW_SAMPLE_ENTITY = PLATFORM_ROOT + "/" + SAMPLE_DOMAIN_ALIAS;
		
		if(sampleEntity_refId!=null) 
			return sampleEntity_refId;
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_SAMPLE_ENTITY).addAction(Action._new).getMock();
		Object home_newResp = controller.handleGet(home_newReq, null);
		assertNotNull(home_newResp);
		
		sampleEntity_refId  = ExtractResponseOutputUtils.extractDomainRootRefId(home_newResp);
		return sampleEntity_refId;
	}
	
	public synchronized Long createOrGetSampleEntityWithBPMN_RefId() {
		if(domainRoot_refId!=null) 
			return domainRoot_refId;
		
		MockHttpServletRequest home_newReq = MockHttpRequestBuilder.withUri(VIEW_WITHBPMN_PARAM_ROOT).addAction(Action._new).getMock();
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
