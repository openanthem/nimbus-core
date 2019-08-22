/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.antheminc.oss.nimbus.domain.cmd.Action;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.SampleJPARootCoreEntity;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DefaultJPAModelRepositoryTest extends AbstractRdbmsTest {
	
	private SimpleJpaRepository<SampleJPARootCoreEntity, Long> jpaRepo;
	
	@Before
	public void test_context_load() {
		assertNotNull(repo);
		assertNotNull(em);
		
		jpaRepo = new SimpleJpaRepository<>(SampleJPARootCoreEntity.class, em);
	}
	
	@Test
	public void t01_simple_jpa() {
		SampleJPARootCoreEntity core = new SampleJPARootCoreEntity();
		core.setId(1L);
		core.setA1("a1 "+new Date());
		
		repo._save("core", core);
		
		SampleJPARootCoreEntity actual = jpaRepo.findById(core.getId()).get();
		assertEquals(core.getA1(), actual.getA1());
	}
	
	@Test
	public void t02_simple_cmd() {
		HttpServletRequest req_new = MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT)
										.addAction(Action._new)
										.getMock();

		// id generation
		Object resp_new = controller.handleGet(req_new, null);
		assertNotNull(resp_new);
		
		Param<SampleJPARootCoreEntity> pCore = ExtractResponseOutputUtils.extractOutput(resp_new);
		assertNotNull(pCore);
		
		Long id = pCore.findStateByPath("/id");
		assertNotNull(id);
		
		
		// update attribute
		String a1 = "a1 @" + new Date();
		Object resp_update_a1 = controller.handlePost(MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT)
										.addRefId(id)
										.addNested("/a1")
										.addAction(Action._update)
										.getMock(), jsonUtils.convert(a1));
		assertNotNull(resp_update_a1);
		
		
		// save to db
		Object resp_save = controller.handleGet(MockHttpRequestBuilder.withUri(CORE_DOMAIN_ROOT)
				.addRefId(id)
				.addAction(Action._save)
				.getMock(), null);
		assertNotNull(resp_save);
		
		SampleJPARootCoreEntity core = pCore.getState();
		SampleJPARootCoreEntity actual = jpaRepo.findById(core.getId()).get();
		
		assertEquals(id, actual.getId());
		assertEquals(a1, actual.getA1());
	}
}