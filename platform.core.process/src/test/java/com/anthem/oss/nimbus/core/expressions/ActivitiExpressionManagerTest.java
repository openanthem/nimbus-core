package com.anthem.oss.nimbus.core.expressions;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.anthem.oss.nimbus.core.bpm.activiti.ActivitiExpressionManager;

@RunWith(SpringJUnit4ClassRunner.class)
public class ActivitiExpressionManagerTest {
	
	ActivitiExpressionManager expMgr;
	
	@Before
	public void setUp() {
		expMgr = new ActivitiExpressionManager();
		expMgr.setFunctionToBeanMap(new HashMap<String,String>());
	}
	
	@Test
	public void t01_evaluate_noArgs() {
		String expression = expMgr.evaluate("${!{_fireAllRules()!}}");
		assertEquals("${defaultExpressionHelper._fireAllRules(processGatewayContext.processEngineContext.commandMsg,execution)}",expression);
	}
	
	@Test
	public void t02_evaluate_withArgs() {
		String expression = expMgr.evaluate("${!{_get('/cmcase','#DomainAlias#')!}}");
		assertEquals("${defaultExpressionHelper._get(processGatewayContext.processEngineContext.commandMsg,execution,defaultExpressionHelper.getResolvedUri(processGatewayContext.processEngineContext.commandMsg,'/cmcase'),'#DomainAlias#')}",expression);
	}
	
	@Test
	public void t03_evaluate_withPayload() {
		String expression = expMgr.evaluate("${!{_search('/cmcase','{\"status\" : \"Active\"}')!}}");
		assertEquals("${defaultExpressionHelper._search(processGatewayContext.processEngineContext.commandMsg,execution,defaultExpressionHelper.getResolvedUri(processGatewayContext.processEngineContext.commandMsg,'/cmcase'),'{\"status\" : \"Active\"}')}",expression);
	}
	
	@Test
	public void t04_evaluate_fail() {
		String ip = "${_search('/cmcase','{\"status\" : \"Active\"}')}";
		try {
			expMgr.evaluate(ip);
		} catch(IllegalArgumentException e) {
			assertSame("Expression :"+ip+" is not valid", e.getMessage());
		}
	}	
}
