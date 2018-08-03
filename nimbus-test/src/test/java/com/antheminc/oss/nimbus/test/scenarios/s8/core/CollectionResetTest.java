/**
 * 
 */
package com.antheminc.oss.nimbus.test.scenarios.s8.core;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.mock.web.MockHttpServletRequest;

import com.antheminc.oss.nimbus.domain.AbstractFrameworkIngerationPersistableTests;
import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.cmd.exec.CommandExecution.MultiOutput;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.support.Holder;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;

/**
 * @author Jayant Chaudhuri
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CollectionResetTest extends AbstractFrameworkIngerationPersistableTests {
	
	protected static final String ROOT_URL = PLATFORM_ROOT + "/maintestmodel";
	
	@Test()
	@SuppressWarnings("unchecked")
	public void t01_stack_overflow() {
		MockHttpServletRequest request = MockHttpRequestBuilder.withUri(ROOT_URL)
					.addAction(Action._new)
					.getMock();
		Holder<MultiOutput> holder = (Holder<MultiOutput>)controller.handlePost(request, null);
		Param para = (Param)holder.getState().getSingleResult();
		Param childPara = para.findParamByPath("/child");
		assertNotNull(childPara);
		
		List list = new ArrayList<ChildTestModel>();
		ChildTestModel c1 =  new ChildTestModel();
		c1.setPara3("para3");
		list.add(c1);
		childPara.setState(list);
		childPara.setState(null);
		c1 =  new ChildTestModel();
		c1.setPara3("para3");
		list.add(c1);
		childPara.setState(list);
	} 

}
