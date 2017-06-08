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

import com.anthem.oss.nimbus.core.AbstractUnitTest;
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
public class DefaultActionExecutorDeleteTest extends AbstractUnitTest {
	
	@Test
	public void t0_deleteParam() {
		
		Command cmd = CommandBuilder.withUri("Anthem/fep/icr/p/client:5936ef35bc9e4b575f00d1a9/_delete").getCommand();
		cmd.setAction(Action._delete);
		
		MultiOutput multiOp = getCommandGateway().execute(cmd, null);
		Boolean b = (Boolean)multiOp.getSingleResult();
		
		assertTrue(b);
	}

}
