//package com.anthem.oss.nimbus.core.repo;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.time.LocalDate;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.junit.Assert;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientEntityRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserGroupRepository;
//import com.anthem.oss.nimbus.core.entity.client.Client;
//import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
//import com.anthem.oss.nimbus.core.entity.client.ClientEntity.Type;
//import com.anthem.oss.nimbus.core.entity.person.Address;
//import com.anthem.oss.nimbus.core.entity.user.AbstractUserGroup.Status;
//import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;
//
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ActiveProfiles("test")
//public class ClientEntityRepositoryTest {
//
//	@Autowired ClientRepository cRep;
//	
//	@Autowired ClientEntityRepository ceRep;
//	
//	@Autowired ClientUserGroupRepository cuGroupRepo;
//	
//	@Test
//	public void testCreateClient(){
//
//		Client c = new Client();
//		c.setCode("Anthem");
//		c.setName("anthem");
//		c.setBusinessType("Health Payer");
//		c.setFedTaxID("1111111");
//		c.setType(Type.CLIENT);
//		Address.IdString a = new Address.IdString();
//		a.setStreet1("108 Leigus Rd");
//		a.setCity("Wallingford");
//		a.setCountryCd("US");
//		a.setStateCd("CT");
//		a.setType(Address.Type.MAILING);
//		a.setZip("06492");
//		c.setAddress(a);
//		Client c1 = cRep.save(c);
//		System.out.println(c1.getId());
//	
//	}
//	
//	@Test
//	public void testCreateClientOrg(){
//
//		Client c = cRep.findByCode("Anthem");
//		assertNotNull(c);
//		
//		ClientEntity org = new ClientEntity();
//		
//		org.setType(Type.ORG);
//		
//		org.setCode("AETNA_ORG4");
//		org.setName("aetna org 4");
//		//org.setEffectiveDate(LocalDate.now());
//		//org.setTerminationDate(LocalDate.now());
//		org.setDescription("test");
//		org.setStatus(com.anthem.oss.nimbus.core.entity.client.ClientEntity.Status.ACTIVE);
//		
//		
//		
//		Set<ClientEntity> orgs = new HashSet<>();
//		orgs.add(org);
//		org = cRep.save(c);
//		System.out.println(org.getId());
//	}
//	
//	
//	//@Test
//	public void getClientByCode(){
//		Client c = cRep.findOne(cRep.findByCode("antm").getId());
//		System.out.println(c);
//	}
//	
//	//@Test
//	public void getAllClients(){
//		PageRequest p = new PageRequest(0,10);
//		Page<ClientEntity> ce = ceRep.findAll(p);
//		Assert.assertNotNull(ce);
//	}
//	
//	//@Test
//	public void getClientByCode2(){
//		Client c = cRep.findOne(cRep.findByCode("agp").getId());
//		System.out.println(c);
//	}
//	
//	
//	
////	@Test
////	public void getAllClients() {
////		PageRequest pageReq = new PageRequest(0, Integer.MAX_VALUE);
////		Page<ClientEntity> ce = ceRep.findAll(pageReq);
////		System.out.println(ce);
////	}
//	
//}
