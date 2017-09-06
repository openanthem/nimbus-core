package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.anthem.oss.nimbus.core.AbstractFrameworkIntegrationTests;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;

import test.com.anthem.oss.nimbus.core.testutils.CommandUtils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HierarchyMatchBasedBeanFinderTest extends AbstractFrameworkIntegrationTests {
	
	@Autowired HierarchyMatchBasedBeanFinder hierarchyMatchBasedBeanFinder;

	@Test
	public void test() {
		final Command command = CommandUtils.prepareCommand("/Acme/admin/p/testmappedmodel/_process?fn=_set", Behavior.$execute);
		assertNotNull(hierarchyMatchBasedBeanFinder.findMatchingBean(SetFunctionHandler.class, this.constructFunctionHandlerKey(command)));
	}
	
	private String constructFunctionHandlerKey(Command command){
		StringBuilder key = new StringBuilder();
		String functionName = command.getFirstParameterValue(Constants.KEY_FUNCTION.code);
		String absoluteUri = command.getAbsoluteUri();
		absoluteUri = absoluteUri.replaceAll(Constants.SEPARATOR_URI.code, "\\.");
		key.append(absoluteUri).append(".").append(command.getAction().toString())
		   .append(Behavior.$execute.getCode())
		   .append(Constants.REQUEST_PARAMETER_MARKER.code).append(Constants.KEY_FUNCTION.code).append("=").append(functionName);
		return key.toString();
	}	
	
}
