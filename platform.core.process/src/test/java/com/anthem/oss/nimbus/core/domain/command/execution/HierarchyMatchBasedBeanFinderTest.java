package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.fail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import com.anthem.oss.nimbus.core.AbstractUnitTest;
import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.execution.process.SetFunctionHandler;

@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HierarchyMatchBasedBeanFinderTest extends AbstractUnitTest{
	
	@Autowired HierarchyMatchBasedBeanFinder hierarchyMatchBasedBeanFinder;

	@Test
	public void test() {
		Command command = prepareCommand("/Anthem/admin/p/testmappedmodel/_process?fn=_set", Behavior.$execute);
		hierarchyMatchBasedBeanFinder.findMatchingBean(SetFunctionHandler.class, command);
		fail("Not yet implemented");
	}
	
	public static void main(String[] args){
//		Pattern pattern = Pattern.compile("default._process$execute?fn=_set");
//		System.out.println(pattern.matcher("._set").matches());
		Pattern pattern = Pattern.compile("(.*?)._process\\.\\$execute\\?fn=_set");
		Matcher matcher  = pattern.matcher("anthem.admin.p.testmappedmodel._process.$execute?fn=_set");
		System.out.println(matcher.matches());
		while(matcher.find()){
			System.out.println(matcher.start()+","+matcher.end());
		}
		
	}

}
