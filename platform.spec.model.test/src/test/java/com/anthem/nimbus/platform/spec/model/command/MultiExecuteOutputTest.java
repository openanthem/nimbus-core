/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.command;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.anthem.oss.nimbus.core.domain.command.Behavior;
import com.anthem.oss.nimbus.core.domain.command.execution.ExecuteOutput.BehaviorExecute;

import test.com.anthem.nimbus.platform.utils.JsonUtils;

/**
 * @author Soham Chakravarti
 *
 */
public class MultiExecuteOutputTest {/*

	@Test
	public void t1_json() {
		MultiExecuteOutput mExec = new MultiExecuteOutput();
		
		BehaviorExecute<UMCase> be1 = new BehaviorExecute<>(Behavior.$execute);
		be1.setResult(TestUMCaseFactory.create());
		
		BehaviorExecute<String> be2 = new BehaviorExecute<>(Behavior.$nav);
		be2.setResult("pg1");
		
		mExec.add(be1);
		mExec.add(be2);
		
		String json = JsonUtils.get().convert(mExec);
		assertNotNull(json);
		
		System.out.println(json);
	}
*/}
