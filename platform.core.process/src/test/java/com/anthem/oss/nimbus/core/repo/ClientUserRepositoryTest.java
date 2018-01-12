/**
 *
 *  Copyright 2012-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
///**
// * 
// */
//package com.anthem.oss.nimbus.core.repo;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.util.List;
//
//import org.junit.Assert;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.ClientUserRoleRepository;
//import com.anthem.oss.nimbus.core.domain.model.state.repo.clientmanagement.PlatformUserRepository;
//import com.anthem.oss.nimbus.core.entity.access.Role;
//import com.anthem.oss.nimbus.core.entity.access.Role.Entry;
//import com.anthem.oss.nimbus.core.entity.client.Client;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
//import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
//import com.anthem.oss.nimbus.core.entity.client.user.TestClientUserFactory;
//import com.anthem.oss.nimbus.core.entity.user.DefaultUser;
//
//import test.com.anthem.nimbus.platform.spec.model.client.TestClientFactory;
//import test.com.anthem.nimbus.platform.spec.model.user.TestPlatformUserFactory;
//
///**
// * @author AC63348
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@ActiveProfiles("test")
//public class ClientUserRepositoryTest {
//	
//	@Autowired PlatformUserRepository pRep;
//
//	@Autowired ClientUserRepository cuRep;
//	
//	@Autowired ClientUserRoleRepository crRep;
//	
//	@Autowired ClientRepository cRep;
//	
//	@Test
//	public void testCreateDefaultUser() {
//		assertNotNull(TestClientUserFactory.createDefaultUser().getLoginId());
//	}
//	
//	//@Test
//	public void test_01_createClientUser1() {
//		DefaultUser pu = pRep.save(TestPlatformUserFactory.createUser());
//		Assert.assertNotNull(pu);
//
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		
////		Set<ClientUserIDS> cuIdsSet = new HashSet<ClientUserIDS>();
////		ClientUserIDS cuIds = TestClientFactory.createClientUserIDS();
////		cuIdsSet.add(cuIds);
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("AC12345");
//		//cu.setClientUserIDS(cuIdsSet);
//		cuRep.save(cu);
//		
//	}
//	
//	//@Test
//	public void test_02_createClientUser2() {
//		DefaultUser pu = pRep.findByEmail(TestPlatformUserFactory.createUser().getEmail());
//		Assert.assertNotNull(pu);
//
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		//ClientRole cr = crRep.findOne(crRep.findByCode(TestClientFactory.createClientRoleClinician().getCode()).getId());
//		//Assert.assertNotNull(cr);
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("AD10998");
//		cuRep.save(cu);
//		
//	}
//	
//	@Test
//	public void test_03_createClientUser3() {
//		DefaultUser pu = pRep.save(TestPlatformUserFactory.createUser3());
//		Assert.assertNotNull(pu);
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		ClientUserRole cr = crRep.findOne(crRep.findByCode(TestClientFactory.createClientRoleClinician().getCode()).getId());
//		Assert.assertNotNull(cr);
//		cu.addGrantedRoles(cr);
//		System.out.println("GRanted ROles:"+cu.getGrantedRoles());
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("Rakesh");
//		cuRep.save(cu);
//		
//	}
//	
//	//@Test
//	public void test_04_createClientUser4() {
//		DefaultUser pu = pRep.save(TestPlatformUserFactory.createUser2());
//		Assert.assertNotNull(pu);
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		ClientUserRole cr = crRep.findOne(crRep.findByCode(TestClientFactory.createClientRoleIntake().getCode()).getId());
//		Assert.assertNotNull(cr);
//		cu.addGrantedRoles(cr);
//		System.out.println("GRanted ROles:"+cu.getGrantedRoles());
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("Syed");
//		cuRep.save(cu);
//		
//	}
//	
//	@Test
//	public void test_05_getClientUserRelations(){
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		System.out.println("Client:"+c+"ID:"+c.getId());
//		Assert.assertNotNull(c);
//		List<ClientUser> clientUsers = cuRep.findByClient(c);
//		Assert.assertNotNull(clientUsers);
//		Assert.assertTrue(clientUsers.size() > 0);
//		System.out.println("ClientUser1"+clientUsers.get(0));
//	}
//	
//	/*Update All ClientUSers with Clinician Role*/
////	@Test
//	public void test_06_assignExistingRoletoClientUser() {
//		
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		List<ClientUser> clientUser = cuRep.findByClient(c);
//		System.out.println("client user::"+clientUser.size());
//		Assert.assertNotNull(clientUser);
//		
//		ClientUserRole cr = crRep.findOne(crRep.findByCode(TestClientFactory.createClientRoleClinician().getCode()).getId());
//		System.out.println("Clinician Role:"+cr);
//		Assert.assertNotNull(cr);
//				
//		clientUser.forEach((user)-> {
//			System.out.println("Clinet User:"+user);
//			user.addGrantedRoles(cr);
//			cuRep.save(user);
//		});
//		
//	}
//	
//	//@Test
//	public void test_07_getAllGrantedRolesforAClientUser() {
//		// TODO - removed this for neo4j to mongodb change - need to revisit
//		//List<Role<com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole.Entry, ClientAccessEntity>> cu = cuRep.getGrantedRolesForClientUser(cuRep.findByLoginName("AC63348"));
//		List<Role<com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole.Entry, ClientAccessEntity>> cu = null;
//		
//		
//		System.out.println(cu);
//		Assert.assertNotNull(cu);
//		System.out.println("Role Name::"+cu.get(0).getCode());
//		Assert.assertNotNull(cu.get(0));
//		Assert.assertTrue(cu.size() == 2);
//		
//	}
//	
//	@Test
//	public void test_08_getClientUserforClient() {
//		List<ClientUser> cu = cuRep.findByClientId(cRep.findByCode("antm").getId());
//		System.out.println("No of Client Users for Client-antm"+cu.size());
//		Assert.assertNotNull(cu);
//		
//		List<ClientUser> cu1 = cuRep.findByClient(cRep.findByCode("antm"));
//		Assert.assertNotNull(cu1);
//		System.out.println("No of Client Users for Client-antm"+cu1.size());
//		System.out.println("cu1"+cu1);
//		System.out.println("CLient User"+cu1.get(0).getLoginId());
//		System.out.println(cu1.get(0).getGrantedRoles());
//	}
//	
////	@Test
//	public void test_09_getclientUserbyName(){
//		ClientUser cu = cuRep.findOne(cuRep.findByLoginName("AC63348").getId());
//		Assert.assertNotNull(cu.getGrantedRoles());
//		Assert.assertTrue(cu.getGrantedRoles().size() > 1);
//		System.out.println(cu);
//		for(ClientUserRole r : cu.getGrantedRoles()){
//			Assert.assertNotNull(r);
//			System.out.println("Role Name::"+r.getName());
//			System.out.println(r.getEntries());
//			for(Entry e : r.getEntries()){
//				Assert.assertNotNull(r.getEntries());
//				System.out.println("Granted Permissions"+e.getGrantedPermissions());
//				System.out.println("Referred Access"+e.getReferredAccess());
//				System.out.println("Permissions"+e.getPermissions());
//				System.out.println("------End of Entry"+e.getId());
//			}
//			System.out.println("*************");
//		}
//	}
//	
//	@Test
//	public void test_10_updateClientUserWithIDS(){
////		ClientUser cu = cuRep.findByLoginName("AC12345");
////		Assert.assertNotNull(cu);
////		Set<ClientUserIDS> cuIdsSet = cu.getClientUserIDS();
////		Assert.assertNotNull(cuIdsSet);
////		
////		ClientUserIDS cuIds = TestClientFactory.createClientUserIDS_2();
////		cuIdsSet.add(cuIds);
////		
////		cuRep.save(cu);
////		
////		ClientUser db_cu = cuRep.findByLoginName("AC12345");
////		Assert.assertNotNull(db_cu);
////		Set<ClientUserIDS> db_cuIdsSet = db_cu.getClientUserIDS();
////		Assert.assertNotNull(db_cuIdsSet);
//		
//	}
//	
//	@Test
//	public void test_11_updateClientUser2WithIDS2(){
////		ClientUser cu = cuRep.findByLoginName("AD10998");
////		Assert.assertNotNull(cu);
////		Set<ClientUserIDS> cuIdsSet = cu.getClientUserIDS();
////		if(cuIdsSet == null || cuIdsSet.size() == 0)
////			cuIdsSet = new HashSet<ClientUserIDS>();
////		ClientUserIDS cuIds1 = TestClientFactory.createClientUserIDS();
////		ClientUserIDS cuIds2 = TestClientFactory.createClientUserIDS_3();
////		cuIdsSet.add(cuIds1);
////		cuIdsSet.add(cuIds2);
////		
////		cu.setClientUserIDS(cuIdsSet);
////		cuRep.save(cu);
////		
////		ClientUser db_cu = cuRep.findByLoginName("AD10998");
////		Assert.assertNotNull(db_cu);
////		Set<ClientUserIDS> db_cuIdsSet = db_cu.getClientUserIDS();
////		Assert.assertNotNull(db_cuIdsSet);
//		
//	}
//	
//	
//	
//	@Test
//	public void tets_12_findClientUserByIDS_guid_source(){
//		// TODO - removed this for neo4j to mongodb change - need to revisit
//		//ClientUser cu = cuRep.findByIDSSourceAndGuid("Google","111111111");
//		ClientUser cu = null;
//		Assert.assertNotNull(cu);
//		System.out.println("No of Client Users with IDS with GUID#111111111 and source 'Google'::");
//		
//	}
//	
//	@Test
//	public void test_13_createClientUser(){
//		DefaultUser pu = pRep.save(TestPlatformUserFactory.createUser4());
//		Assert.assertNotNull(pu);
//		
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("ben");
//		
//		cuRep.save(cu);
//	}
//	
//	@Test
//	public void test_14_createClientUser(){
//		DefaultUser pu = pRep.findByEmail(TestPlatformUserFactory.createUser3().getEmail());
//		Assert.assertNotNull(pu);
//		
//		ClientUser cu = new ClientUser();
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		
//		cu.setClient(c);
//		cu.setPlatformUser(pu);
//		cu.setLoginId("jayant.chaudhuri@gmail.com");
//		
//		cuRep.save(cu);
//	}
//}
