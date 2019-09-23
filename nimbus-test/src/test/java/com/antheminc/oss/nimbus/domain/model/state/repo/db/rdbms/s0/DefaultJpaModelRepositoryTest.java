///**
// * 
// */
//package com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.s0;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import com.antheminc.oss.nimbus.domain.cmd.Action;
//import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
//import com.antheminc.oss.nimbus.domain.model.state.repo.db.rdbms.DefaultJpaModelRepository;
//import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
//import com.antheminc.oss.nimbus.test.domain.support.utils.ExtractResponseOutputUtils;
//import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
//import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0.RefIdMapMainEmbedded;
//import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0.RefIdMapMainEmbeddedPK;
//import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0.RefIdMapMainIdClass;
//import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0.RelatedCoreForwardById;
//import com.antheminc.oss.nimbus.test.scenarios.repo.rdbms.core.s0.RootCore;
//
///**
// * @author Soham.Chakravarti
// *
// */
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class DefaultJpaModelRepositoryTest extends AbstractFrameworkIntegrationTests {
//
//	@Autowired
//	DefaultJpaModelRepository repo;
//
//	@Autowired
//	EntityManagerFactory emFactory;
//	
//	EntityManager em;
//	
//	@Before
//	public void before() {
//		assertNotNull(emFactory);
//		em = emFactory.createEntityManager();
//	}
//	
//	@Test
//	public void t01_forwardById() throws Exception {
//		final String K_URI_CORE = PLATFORM_ROOT + "/rootcore";
//		// _new
//		Object controllerResp_new = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addAction(Action._new).getMock(), 
//				null);
//		
//		Param<RootCore> vp_main = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
//		
//		RootCore core = vp_main.getState();
//		assertNotNull(core);
//		
//		// simulate data entry
//		List<RelatedCoreForwardById> r1s = new ArrayList<>();
//		for(int i=0; i<2; i++) {
//			RelatedCoreForwardById r1 = new RelatedCoreForwardById();
//			r1.setRelatedAttr1("value @ "+new Date());
//			// important to note that parent id has to be set explicitly ..as of now
//			//r1.setCoreId(core.getId());
//			r1s.add(r1);
//		}
//		
//		// update entity in session cache: same as doing:: core.setRelatedObjectsForwardOnly(r1s);
//		controller.handlePost(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(core.getId())
//					.addNested("/relatedObjectsForwardOnly")
//					.addAction(Action._replace).getMock(), 
//				om.writeValueAsString(r1s));
//		
//		// save entity in session cache to db: same as doing:: repo._save(vp_main);
//		controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(core.getId())
//					.addAction(Action._save).getMock(), 
//				null);
//		
//		controller.handlePost(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(core.getId())
//					.addNested("/attr1")
//					.addAction(Action._update).getMock(), 
//				om.writeValueAsString("saved later.."));
//		
//		controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(core.getId())
//					.addAction(Action._save).getMock(), 
//				null);		
//		
//		
//		// make separate db call to assert
//		RootCore actual = em.find(RootCore.class, core.getId());
//		assertNotNull(actual);
//		assertEquals(core.toString(), actual.toString());
//	}
//	
//	@Test
//	public void t02_get_refMap_embedded() throws Exception {
//		RefIdMapMainEmbedded coreExpected = new RefIdMapMainEmbedded();
//		coreExpected.setPk(new RefIdMapMainEmbeddedPK());
//		coreExpected.getPk().setKey1("K1");
//		coreExpected.getPk().setKey2(-1);
//		coreExpected.setAttr1("some value..");
//		
//		EntityTransaction tx = em.getTransaction();
//		tx.begin();
//		em.persist(coreExpected);
//		em.flush();
//		tx.commit();
//		
//		final String K_URI_CORE = PLATFORM_ROOT + "/refidmap_main_embedded_core";
//		
//		Map<String, String> refMap = new HashMap<>();
//		refMap.put("pk.key1", coreExpected.getPk().getKey1());
//		refMap.put("pk.key2", String.valueOf(coreExpected.getPk().getKey2()));
//		
//		Object resp_get = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefMap(refMap)
//					.addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainEmbedded> core_main = ExtractResponseOutputUtils.extractOutput(resp_get);
//		assertNotNull(resp_get);
//		
//		RefIdMapMainEmbedded coreActual = core_main.getState();
//		assertNotNull(coreActual);
//		assertNotNull(coreActual.getId());
//		assertEquals(coreExpected, coreActual);
//		
//		// refId _get call
//		Object resp_get_byId = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(coreActual.getId()).addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainEmbedded> p_core_getById = ExtractResponseOutputUtils.extractOutput(resp_get_byId);
//		assertNotNull(p_core_getById);
//		
//		RefIdMapMainEmbedded coreActual_byId = p_core_getById.getState();
//		assertNotNull(coreActual_byId);
//		assertEquals(coreExpected, coreActual_byId);
//	}
//	
//	@Test
//	public void t03_get_refMap_idclass() throws Exception {
//		RefIdMapMainIdClass coreExpected = new RefIdMapMainIdClass();
//		coreExpected.setKey1("K1");
//		coreExpected.setKey2(-1);
//		coreExpected.setAttr1("some value..");
//		
//		EntityTransaction tx = em.getTransaction();
//		tx.begin();
//		em.persist(coreExpected);
//		em.flush();
//		tx.commit();
//		
//		final String K_URI_CORE = PLATFORM_ROOT + "/refidmap_main_idclass_core";
//		
//		Map<String, String> refMap = new HashMap<>();
//		refMap.put("key1", coreExpected.getKey1());
//		refMap.put("key2", String.valueOf(coreExpected.getKey2()));
//		
//		Object resp_get = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefMap(refMap)
//					.addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainIdClass> core_main = ExtractResponseOutputUtils.extractOutput(resp_get);
//		assertNotNull(resp_get);
//		
//		RefIdMapMainIdClass coreActual = core_main.getState();
//		assertNotNull(coreActual);
//		assertNotNull(coreActual.getId());
//		assertEquals(coreExpected, coreActual);
//		
//		// refId _get call
//		Object resp_get_byId = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(coreActual.getId()).addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainIdClass> p_core_getById = ExtractResponseOutputUtils.extractOutput(resp_get_byId);
//		assertNotNull(p_core_getById);
//		
//		RefIdMapMainIdClass coreActual_byId = p_core_getById.getState();
//		assertNotNull(coreActual_byId);
//		assertEquals(coreExpected, coreActual_byId);
//	}	
//
//	@Test
//	public void t04_new_refMap_embedded() throws Exception {
//		final String K_URI_CORE = PLATFORM_ROOT + "/refidmap_main_embedded_core";
//
//		RefIdMapMainEmbedded coreExpected = new RefIdMapMainEmbedded();
//		coreExpected.setPk(new RefIdMapMainEmbeddedPK());
//		coreExpected.getPk().setKey1("K1");
//		coreExpected.getPk().setKey2(-2);
//
//		
//		Map<String, String> refMap = new HashMap<>();
//		refMap.put("pk.key1", coreExpected.getPk().getKey1());
//		refMap.put("pk.key2", String.valueOf(coreExpected.getPk().getKey2()));
//		
//		Object controllerResp_new = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefMap(refMap).addAction(Action._new).getMock(), 
//				null);
//		
//		Param<RefIdMapMainEmbedded> p_core_actual = ExtractResponseOutputUtils.extractOutput(controllerResp_new);
//		
//		RefIdMapMainEmbedded core_actual = p_core_actual.getState();
//		assertNotNull(core_actual);
//		assertNotNull(core_actual.getId());
//		assertEquals(coreExpected, core_actual);
//		
//		// refIdMap _get call
//		Object resp_get = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefMap(refMap).addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainEmbedded> p_core_getByMap = ExtractResponseOutputUtils.extractOutput(resp_get);
//		assertNotNull(p_core_getByMap);
//		
//		RefIdMapMainEmbedded coreActual_byMap = p_core_getByMap.getState();
//		assertNotNull(coreActual_byMap);
//		assertEquals(coreExpected, coreActual_byMap);
//		
//		// refId _get call
//		Object resp_get_byId = controller.handleGet(
//				MockHttpRequestBuilder.withUri(K_URI_CORE).addRefId(core_actual.getId()).addAction(Action._get).getMock(), 
//				null);
//		
//		Param<RefIdMapMainEmbedded> p_core_getById = ExtractResponseOutputUtils.extractOutput(resp_get_byId);
//		assertNotNull(p_core_getById);
//		
//		RefIdMapMainEmbedded coreActual_byId = p_core_getById.getState();
//		assertNotNull(coreActual_byId);
//		assertEquals(coreExpected, coreActual_byId);
//	}
//}
