///**
// * 
// */
//package com.anthem.oss.nimbus.core.repo;
//
//import java.util.HashSet;
//
//import org.junit.Assert;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.context.annotation.Profile;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.anthem.oss.nimbus.core.config.BPMEngineConfig;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.AccessEntityRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.PlatformRoleRepository;
//import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
//import com.anthem.oss.nimbus.core.entity.access.DefaultRole;
//
//import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;
//
///**
// * @author Soham Chakravarti
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Import(BPMEngineConfig.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Profile("test")
//public class DefaultAccessEntityRepositoryTest {
//
//	@Autowired
//	AccessEntityRepository rep;
//
//	@Autowired
//	PlatformRoleRepository prRep;
//
//	@Test
//	public void test1_createDefaultRole1() {
//		DefaultRole role = createDefaultRole("nurse", "Nurse");
//		prRep.save(role);
//		Assert.assertNotNull(role.getId());
//	}
//
//	@Test
//	public void test1_createDefaultRole2() {
//		DefaultRole role = createDefaultRole("admin", "Admin");
//		prRep.save(role);
//		Assert.assertNotNull(role.getId());
//	}
//
//	public DefaultRole createDefaultRole(String code, String name) {
//		DefaultRole pr = new DefaultRole();
//		pr.setCode(code);
//		pr.setName(name);
//		return pr;
//
//	}
//
//	@Test
//	public void test2_createDefaultRole() {
//
//		DefaultAccessEntity p = rep.findByCode(AccessEntityFactory.createPlatformAndSubTree().getCode());
//		// DefaultAccessEntity p = rep.findByCode("icr");
//		Assert.assertNotNull(p);
//
//		DefaultRole pr = prRep.findByCode("admin");
//		Assert.assertNotNull(pr);
//
//		pr.setEntries(new HashSet<>());
//
//		pr.getEntries().add(new DefaultRole.Entry(p, p.getPermissions()));
//
//		prRep.save(pr);
//	}
//
//	@Test
//	public void test2_createDefaultRole2() {
//
//		// DefaultAccessEntity p =
//		// rep.findByCode(AccessEntityFactory.createDefaultAndSubTree().getCode());
//		DefaultAccessEntity pae = rep.findByCode("nimbus");
//		Assert.assertNotNull(pae);
//
//		DefaultRole pr = prRep.findByCode("admin");
//		Assert.assertNotNull(pr);
//
//		pr.setEntries(new HashSet<>());
//		addRolesRecursively(pr, pae);
//
//		prRep.save(pr);
//	}
//
//	public void addRolesRecursively(DefaultRole pr, DefaultAccessEntity pae) {
//		if (pae != null) {
//
//			pr.getEntries().add(new DefaultRole.Entry(pae, pae.getPermissions()));
//
//			if (pae.getAvailableAccesses() != null) {
//				pae.getAvailableAccesses().forEach((avail_pae) -> addRolesRecursively(pr, avail_pae));
//			}
//		}
//	}
//
//}
