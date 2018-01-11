package com.antheminc.oss.nimbus.core.domain.model.state.extension;

import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.QuadModel;
import com.antheminc.oss.nimbus.core.entity.AbstractEntity.IdString;
import com.antheminc.oss.nimbus.test.sample.domain.model.core.SampleCoreEntity;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public class RuleStateEventHandlerTest extends AbstractStateEventHandlerTests {

	public static final String ENTITY_CORE = "sample_core";
	public static final String ENTITY_VIEW = "sample_view";
	public static final String ENTITY_BASEPATH = "/" + ENTITY_CORE;
	
	private String REF_ID;
	
	@Override
	protected Command createCommand() {
		final Command cmd = CommandBuilder.withUri("/hooli/thebox/p/" + ENTITY_VIEW + ":"+REF_ID+"/_get").getCommand();
		return cmd;
	}

	private SampleCoreEntity createOrGetCore() {
		
		if (REF_ID != null) {
			return mongo.findById(REF_ID, SampleCoreEntity.class, ENTITY_CORE);
		}
		
		final SampleCoreEntity core = new SampleCoreEntity();
		mongo.insert(core, ENTITY_CORE);
		REF_ID = core.getId();
		assertNotNull(REF_ID);
		
		return core;
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		this.createOrGetCore();
		
		_cmd = createCommand();

		executionContextLoader.clear();
		
		_q = (QuadModel<?, ? extends IdString>)executionContextLoader.load(_cmd).getQuadModel();
		assertNotNull(_q);
		
		_q.getRoot().getExecutionRuntime().onStartCommandExecution(_cmd);
	}
	
	@Test
	public void t01_stateChange() {
		final Param<String> ruleParam = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param");
		final Param<Integer> ruleParam_affectState = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param_affectState");
		
		assertNotNull(ruleParam);
		assertNotNull(ruleParam_affectState);
		Assert.assertEquals(2, (int) ruleParam_affectState.getState());
		
		ruleParam.setState("Hello");
		Assert.assertEquals(3, (int) ruleParam_affectState.getState());
	}
	
	@Test
	public void t02_stateChange_cachedRuleConfig() {
		final Param<String> ruleParam = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param");
		final Param<String> ruleParam2 = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param2");
		final Param<Integer> ruleParam_affectState = _q.getRoot().findParamByPath(ENTITY_BASEPATH + "/rule_param_affectState");
		
		assertNotNull(ruleParam);
		assertNotNull(ruleParam_affectState);
		Assert.assertEquals(2, (int) ruleParam_affectState.getState());
		
		ruleParam.setState("Hello");
		Assert.assertEquals(3, (int) ruleParam_affectState.getState());
		
		ruleParam2.setState("World");
		Assert.assertEquals(4, (int) ruleParam_affectState.getState());
	}
}
