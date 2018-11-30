package com.antheminc.oss.nimbus.domain.cmd.exec.internal.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Rakesh Patel
 *
 */
public class SetByRuleFunctionalHandlerTest extends AbstractFrameworkIngerationPersistableTests {

	@Test
	public void t0_nestedConfigPath() {
		Param<?> p = excuteNewConfig();
		assertNotNull(p);
		
		Param<String> testParam = p.findParamByPath("/paramWithSetByRuleConfig");
		assertNotNull(testParam);
		testParam.setState("test");
		
		Param<Long> idParam = p.findParamByPath("/id");
		assertNotNull(idParam);
		
		
		p = excuteGetConfig(idParam.getLeafState());
		assertNotNull(p);
		
		Param<String> testParam1 = p.findParamByPath("/paramWithSetByRuleConfig");
		assertNotNull(testParam1);
		assertThat(testParam1.getLeafState()).isEqualTo("setFromRule1");
		
		Param<String> testParam2 = p.findParamByPath("/testParam");
		assertNotNull(testParam2);
		assertThat(testParam2.getLeafState()).isEqualTo("setFromRule2");
		
		
	}
	
	private Param<?> excuteNewConfig() {
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT)
				.addAction(Action._new)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
	
	private Param<?> excuteGetConfig(Long id) {
		final MockHttpServletRequest req = MockHttpRequestBuilder.withUri(CORE_NESTED_CONFIG_ROOT)
				.addRefId(id)
				.addAction(Action._get)
				.getMock();

		final Object resp = controller.handleGet(req, null);
		Param<?> p = ExtractResponseOutputUtils.extractOutput(resp);
		return p;
	}
}
