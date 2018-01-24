/**
 *  Copyright 2016-2018 the original author or authors.
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
package com.anthem.oss.nimbus.core.repo;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import com.antheminc.oss.nimbus.app.extension.config.BPMEngineConfig;

/**
 * @author Soham Chakravarti
 *
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
@Import(BPMEngineConfig.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Profile("test")
public class ClientAccessEntityRepositoryTest {
	
	

//	@Autowired ClientRepository cRep;
//	
//	@Autowired ClientEntityRepository rep;
//	
//	@Autowired ClientAccessEntityRepository caRep;
//	
//	@Autowired AccessEntityRepository aeRep;
//	
//	@Autowired ClientRoleRepository crRep;
//	
//	@Autowired PermissionRepository pRep;
//	
//	@Test
//	public void test_1_associateAccesses() {
//		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Assert.assertNotNull(c);
//		
//		c.getNestedEntities()
//			.forEach((org) -> TestClientFactory.createSelectedAccesses(org, (aeCode) -> aeRep.findOne(aeRep.findByCode(aeCode).getId(),10)));
//		
//		c.getNestedEntities()
//			.forEach((org) -> rep.save(org));
//	}
//	
//	@Test
//	public void test_2_createClientRole() {
//		Client c1 = cRep.findByCode(TestClientFactory.createClient().getCode());
//		Client c = cRep.findOne(c1.getId(),10);
//		Assert.assertNotNull(c);
//		Assert.assertNotNull(c.getNestedEntities());
//		System.out.println("Orgs for client"+c.getNestedEntities());
//		System.out.println("Client Access Entity"+c.getSelectedAccesses());
//		
//		ClientRole cr = TestClientFactory.createClientRoleClinician();
//		ClientRole cr2 = TestClientFactory.createClientRoleIntake();
//		cr.setAssociatedClient(c);
//		cr2.setAssociatedClient(c);
//		cr.setEntries(new HashSet<>());
//		cr2.setEntries(new HashSet<>());
//		
//
//		createRoleEntries(c, cr);
//		createRoleEntries(c, cr2);
//		
//		crRep.save(cr);
//		crRep.save(cr2);
//		
//	}
	
	
	/*To test if user is able to add selected permissions to unavailable permissions i.e not available in platform level
	@Test
	public void test_3_updateClientAccessEntity(){
		
		ClientAccessEntity caDb = caRep.findOne(caRep.findByCode("um-app").getId(),5);
		System.out.println(caDb);
		System.out.println("Referring access for um-app::"+caDb.getReferringAccess());
		Assert.assertNotNull(caDb.getSelectedAccesses());
		for(ClientAccessEntity scae : caDb.getSelectedAccesses()){
			Assert.assertNotNull(scae);
			if(StringUtils.equals("pcr-review", scae.getCode())) {
				System.out.println("Client Access Entity for PCR-Review Feature:"+scae);
				System.out.println("PCR-Review permissions"+scae.getPermissions());
				System.out.println("Referring Access - Available Permissions::"+scae.getReferringAccess().getAvailablePermissions());
				scae.addSelectedPermission(new Permission.Create());
				scae.addSelectedPermission(new Permission.Update());
				scae.addSelectedPermission(new Permission.Access());
				ClientAccessEntity pcrReviewDbUpdate = caRep.save(scae);
				System.out.println("pcrReviewDbUpdate::"+pcrReviewDbUpdate);
				//Only 2 permission is available for pcr-review. so all 3 permissions are not added.
				Assert.assertTrue(pcrReviewDbUpdate.getSelectedPermissions().size() == 2);
			}
		}
	}
	@Test
	public void test_4_createClientRole2() {
		Client c = cRep.findByCode(TestClientFactory.createClient().getCode());
		//Client c = cRep.findOne(c1.getId(),5);
		Assert.assertNotNull(c);
		Assert.assertNotNull(c.getNestedEntities());
		System.out.println("Orgs for client"+c.getNestedEntities());
		System.out.println("Client Access Entity"+c.getSelectedAccesses());
		ClientRole cr = TestClientFactory.createClientRoleIntake();
	//	cr.setCode("Intake");
	//	cr.setName("Acme Intake");
		cr.setEntries(new HashSet<>());
		
		
		if(c.getNestedEntities() != null && c.getNestedEntities().size() > 0) {
			for(ClientEntity ce : c.getNestedEntities()){
				for(ClientAccessEntity scae : rep.findOne(ce.getId(),4).getSelectedAccesses()){
					System.out.println("SCAE::"+scae);
					if(scae != null && scae.getCode().equals("um-app")){
						
						for(ClientAccessEntity scae2 : scae.getSelectedAccesses()){
							System.out.println("SCAE2::"+scae2);
							if(scae2 != null && scae2.getSelectedPermissions() != null){
								System.out.println("SCA2 Permissions::"+scae2.getSelectedPermissions());
								//for(Permission p : scae2.getSelectedPermissions()){
									//if(p.getCode().equals("ACCESS")){
										cr.getEntries().add(new ClientRole.Entry(scae2,scae2.getSelectedPermissions()));
									//}
								//}
							}
						}
					}
				}
			}
		}
		
		
		crRep.save(cr);
		Assert.assertNotNull(crRep.findByCode(TestClientFactory.createClientRoleIntake().getCode()));
	}
	
	//@Test
	public void test_5_deleteSelectedPermission(){
		ClientAccessEntity cae = caRep.findOne(caRep.findByCode("pcr-review").getId(), 3);
		ClientAccessEntity caeBeforeDel = caRep.findOne(caRep.findByCode("pcr-review").getId(), 3);
		
		System.out.println("CAE"+cae);
		System.out.println("cae getselected permissions:"+cae.getSelectedPermissions());
		Set<Permission> selectedPermissionsToDel = new HashSet<Permission>();
		if(cae.getSelectedPermissions() != null && cae.getSelectedPermissions().size() >0) {
			
			for(Permission selectedPermission : cae.getSelectedPermissions()) {
				if(StringUtils.equalsIgnoreCase("ACCESS", selectedPermission.getCode())) {
					System.out.println("selected Permission to delete"+selectedPermission);
					System.out.println("CAE for selected permission delete"+cae);
					selectedPermissionsToDel.add(selectedPermission);
					//caRep.removeSelectedPermissions(selectedPermission,cae);
					pRep.delete(selectedPermission);
				}			
			}
			System.out.println("Before Del Size:"+cae.getSelectedPermissions().size());
			System.out.println("Before Del Selected Permission:"+cae.getSelectedPermissions());
		
			
			cae.getSelectedPermissions().removeAll(selectedPermissionsToDel);	
		//	caRep.removeSelectedPermissions(selectedPermissionsToDel.iterator().next(), cae);
			System.out.println("cae"+cae);
			ClientAccessEntity afterDel = caRep.save(cae);
			System.out.println(afterDel);
			System.out.println("After Del:"+afterDel.getSelectedPermissions().size());
			Assert.assertTrue(caeBeforeDel.getSelectedPermissions().size() - afterDel.getSelectedPermissions().size() == 1);
		}		
	}
	
	@Test 
	public void test_6_getPermissionsforAClientRole() {
		ClientRole cr = crRep.findOne(crRep.findByCode("intake").getId(), 5);
		Assert.assertNotNull(cr);
		//Assert.assert
		System.out.println(cr);
		System.out.println("Cr Entries"+cr.getEntries());
		Assert.assertNotNull(cr.getEntries());
		for(Entry e : cr.getEntries()) {
			System.out.println("Entry ::"+e);
		//	Assert.assertNotNull(e);
			System.out.println("Get Referred Access::"+e.getReferredAccess().getCode()+"::\t"+e.getReferredAccess());
			System.out.println("Get Granted Permissions::"+e.getGrantedPermissions());
			System.out.println("Get Permissions::"+e.getPermissions());
		//	Assert.assertNotNull(e.getGrantedPermissions());
			if(e.getGrantedPermissions() != null && e.getGrantedPermissions().size() >0){
				for(Permission p : e.getGrantedPermissions()) {
					System.out.println("Granted Permission::::"+p);
				}
			}
			
		}
		
	}
	
	@Test
	public void test_7_getSelectedPermissions() {
		ClientAccessEntity cae = caRep.findOne(caRep.findByCode("pcr-review").getId(), 3);
		Assert.assertNotNull(cae);
		Assert.assertNotNull(cae.getSelectedPermissions());
		System.out.println("Selected Permissions"+cae.getSelectedPermissions());
		
	}
	*/
	
//	private void createRoleEntries(Client client, ClientRole cr) {
//		for(ClientEntity ce:  client.getNestedEntities()) {
//			addRoleEntriesRecursively(ce.getSelectedAccesses(), cr);
//		}
//	}
//	
//	private void addRoleEntriesRecursively(Set<ClientAccessEntity> selectedClientAccessEntities, ClientRole cr) {
//		for(ClientAccessEntity cae: selectedClientAccessEntities) {
//			if((cae.getCode().equalsIgnoreCase("UMCase") && cr.getCode().equalsIgnoreCase("clinician")) ||
//					((cae.getCode().equalsIgnoreCase("Member") || cae.getCode().equalsIgnoreCase("Provider")) 
//							&& cr.getCode().equalsIgnoreCase("intake"))) {
//				continue;
//			}
//			
//			cr.getEntries().add(new ClientRole.Entry(cae, cae.getPermissions()));
//			if(!CollectionUtils.isEmpty(cae.getSelectedAccesses())) {
//				addRoleEntriesRecursively(cae.getSelectedAccesses(), cr);
//			}
//		}
//	}
}
