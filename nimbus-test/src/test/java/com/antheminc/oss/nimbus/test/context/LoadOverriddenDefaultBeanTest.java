/**
 * 
 */
package com.antheminc.oss.nimbus.test.context;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.context.DefaultBeanResolverStrategy;
import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.support.pojo.MockInterface;
import com.antheminc.oss.nimbus.test.support.pojo.MockInterfaceOverrideBean;

/**
 * @author Rakesh Patel
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(properties = {"default.bean.prefix=testPrefix"})
public class LoadOverriddenDefaultBeanTest extends AbstractFrameworkIntegrationTests {

	@Autowired
	BeanResolverStrategy beanResolver;
	
	@Test
	public void t0_loadOverridenDefaultBean() {
		
		assertThat(beanResolver).isNotNull();
		assertThat(beanResolver).isInstanceOf(DefaultBeanResolverStrategy.class);
		
		MockInterface mockOverridenBean = beanResolver.get(MockInterface.class);
		assertThat(mockOverridenBean).isNotNull();
		assertThat(mockOverridenBean).isInstanceOf(MockInterfaceOverrideBean.class);
		
	}
	
	
}
