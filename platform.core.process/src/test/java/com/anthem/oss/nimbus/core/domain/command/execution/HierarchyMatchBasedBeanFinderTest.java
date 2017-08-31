package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertNotNull;

import org.apache.tomcat.util.file.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.anthem.oss.nimbus.core.AbstractTestConfigurer;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;
import com.anthem.oss.nimbus.core.domain.definition.Constants;

@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HierarchyMatchBasedBeanFinderTest extends AbstractTestConfigurer{
	
	@Autowired HierarchyMatchBasedBeanFinder hierarchyMatchBasedBeanFinder;

	@Test
	public void test() {
		Command command = prepareCommand("/Anthem/admin/p/testmappedmodel/_process?fn=_set", Behavior.$execute);
		SetFunctionHandler<?,?> functionHandler = hierarchyMatchBasedBeanFinder.findMatchingBean(SetFunctionHandler.class, ".Anthem.admin.p.testmappedmodel._process\\$execute\\?fn=_set");
		assertNotNull(functionHandler);
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
