/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.command.execution;

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;

import com.anthem.oss.nimbus.core.AbstractTestConfigurer;
import com.anthem.oss.nimbus.core.domain.command.Action;
import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;

/**
 * @author Rakesh Patel
 *
 */
@EnableAutoConfiguration
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultActionExecutorUpdateTest extends AbstractTestConfigurer {
	
	@Test
	public void t0_updateParam() {
		
		///anthem/comm/icr/p/flow_umcase/pg1/caseInfo/requestType/_update
		
		Command cmd = CommandBuilder.withUri("Anthem/fep/icr/p/client:5936ef35bc9e4b575f00d1a9/code/_update").getCommand();
		cmd.setAction(Action._update);
		
		
		MultiOutput multiOp = getCommandGateway().execute(cmd, "\"c1_updated\"");
		Boolean b = (Boolean)multiOp.getSingleResult();
		
		assertTrue(b);
	}

}
