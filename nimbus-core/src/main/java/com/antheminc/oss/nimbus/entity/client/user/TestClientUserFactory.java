package com.antheminc.oss.nimbus.entity.client.user;
/**
 *  Copyright 2016-2019 the original author or authors.
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
//package com.anthem.oss.nimbus.core.entity.client.user;
//
//import java.util.Arrays;
//import java.util.HashSet;
//
//import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
//import com.anthem.oss.nimbus.core.entity.access.Permission;
//import com.anthem.oss.nimbus.core.entity.client.Client;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
//import com.anthem.oss.nimbus.core.entity.person.Name;
//
//public class TestClientUserFactory {
//	
//	public static ClientUser createDefaultUser() {
//		ClientUser cu = new ClientUser();
//		cu.setLoginId("Nimbus_User");
//		
//		Name.IdString nm = new Name.IdString();
//		nm.setFirstName("Test_FirstName");
//		nm.setLastName("Test_LastName");
//		cu.setName(nm);
//		cu.setEmail("testUser@anthem.com");
//		
//		return cu;
//	}
//	
//	public static ClientUser createCoreClientUser(){
//		Client c = new Client();
//		c.setCode("abc");
//		c.setName("abc");
//        
//		ClientUser cu = new ClientUser();
//		cu.setClient(c);
//        cu.setLoginId("swetha");
//       
////        c.addClientUserRole(createClientUserRole1());
////        c.addClientUserRole(createClientUserRole2());
//       
//        return cu;
//	}
//	
//	private static ClientUserRole createClientUserRole1(){
//		ClientUserRole abcCUR1 = new ClientUserRole();
//        abcCUR1.setCode("abcpcr");
//        abcCUR1.setName("PCR");
//        abcCUR1.setEntries(new HashSet<>());
//        return abcCUR1;
//	}
//	
//	private static ClientUserRole createClientUserRole2(){
//		ClientUserRole abcCUR2 = new ClientUserRole();
//        abcCUR2.setCode("abcintake");
//        abcCUR2.setName("abc-intake");
//        abcCUR2.setEntries(new HashSet<>());
//        return abcCUR2;
//	}
//	
//	public static ClientUser createClientUser(){
//		
//		ClientUser cu = createCoreClientUser();
//		DefaultAccessEntity patae = createPatientAccess();	
//		DefaultAccessEntity patnameae = createPatientNameAccess();
//		DefaultAccessEntity casestatusae = createCaseStatusAccess();
//        
//        ClientAccessEntity patcae = new ClientAccessEntity(patae);
//        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
//        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
//        
//        ClientUserRole abcCUR1 = createClientUserRole1();
//        ClientUserRole abcCUR2 = createClientUserRole2();
//        
//    //    abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, null));
////        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(),
//				new Permission.Create()))));
////        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        cu.addGrantedRoles(abcCUR1);
//        cu.addGrantedRoles(abcCUR2);
//        return cu;
//	}
//	
//	public static ClientUser createClientUser2(){
//		
//		ClientUser cu = createCoreClientUser();
//		DefaultAccessEntity patae = createPatientAccess();	
//		DefaultAccessEntity patnameae = createPatientNameAccess();
//		DefaultAccessEntity casestatusae = createCaseStatusAccess();
//        
//        ClientAccessEntity patcae = new ClientAccessEntity(patae);
//        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
//        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
//        
//        ClientUserRole abcCUR1 = createClientUserRole1();
//        ClientUserRole abcCUR2 = createClientUserRole2();
//        
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Update()))));
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        
//  //      abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create()))));
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        cu.addGrantedRoles(abcCUR1);
//        cu.addGrantedRoles(abcCUR2);
//        return cu;
//	}
//	
//	public static ClientUser createClientUser3(){
//		
//		ClientUser cu = createCoreClientUser();
//		DefaultAccessEntity patae = createPatientAccess();	
//		DefaultAccessEntity patnameae = createPatientNameAccess();
//		DefaultAccessEntity casestatusae = createCaseStatusAccess();
//        
//        ClientAccessEntity patcae = new ClientAccessEntity(patae);
//        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
//        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
//        
//        ClientUserRole abcCUR1 = createClientUserRole1();
//        ClientUserRole abcCUR2 = createClientUserRole2();
//        
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create(), new Permission.Update()))));
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(),new Permission.Create(),new Permission.Update()))));
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        cu.addGrantedRoles(abcCUR1);
//        cu.addGrantedRoles(abcCUR2);
//        return cu;
//	}
//	
//	public static ClientUser createClientUser4(){
//		
//		ClientUser cu = createCoreClientUser();
//		DefaultAccessEntity patae = createPatientAccess();	
//		DefaultAccessEntity patnameae = createPatientNameAccess();
//		DefaultAccessEntity casestatusae = createCaseStatusAccess();
//        
//        ClientAccessEntity patcae = new ClientAccessEntity(patae);
//        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
//        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
//        
//        ClientUserRole abcCUR1 = createClientUserRole1();
//        ClientUserRole abcCUR2 = createClientUserRole2();
//        
//     //   abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Read()))));
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        
//   //     abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Update()))));
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        
//        cu.addGrantedRoles(abcCUR1);
//        cu.addGrantedRoles(abcCUR2);
//        return cu;
//	}
//	
//public static ClientUser createClientUser5(){
//		
//		ClientUser cu = createCoreClientUser();
//		DefaultAccessEntity patae = createPatientAccess();	
//		DefaultAccessEntity patnameae = createPatientNameAccess();
//		DefaultAccessEntity casestatusae = createCaseStatusAccess();
//        DefaultAccessEntity navStateae = createNavStateAccess();
//        ClientAccessEntity patcae = new ClientAccessEntity(patae);
//        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
//        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
//        ClientAccessEntity navStatecae = new ClientAccessEntity(navStateae);
//        
//        ClientUserRole abcCUR1 = createClientUserRole1();
//        ClientUserRole abcCUR2 = createClientUserRole2();
//        
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Create()))));
//      //  abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//   //     abcCUR1.getEntries().add(new ClientUserRole.Entry(navStatecae, navStatecae.getPermissions()));
//        
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create()))));
//    //    abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
//        cu.addGrantedRoles(abcCUR1);
//        cu.addGrantedRoles(abcCUR2);
//        return cu;
//	}
//	
//	private static DefaultAccessEntity createPatientAccess(){
//		DefaultAccessEntity patae = new DefaultAccessEntity.Feature();
//		patae.setCode("patient");
//		patae.setName("Create Patient");
//		patae.setDomainUri("/patient");
//		patae.addAvailablePermission(new Permission.Access());
//		patae.addAvailablePermission(new Permission.Read());
//		patae.addAvailablePermission(new Permission.Create());
//		patae.addAvailablePermission(new Permission.Update());
//		patae.addAvailablePermission(new Permission.Delete());
//		
//		return patae;
//	}
//	
//	private static DefaultAccessEntity createNavStateAccess(){
//		DefaultAccessEntity patae = new DefaultAccessEntity.Feature();
//		patae.setCode("nav");
//		patae.setName("nav");
//		patae.setDomainUri("/navigationState");
//		patae.addAvailablePermission(new Permission.Access());
//		patae.addAvailablePermission(new Permission.Read());
//		patae.addAvailablePermission(new Permission.Create());
//		patae.addAvailablePermission(new Permission.Update());
//		patae.addAvailablePermission(new Permission.Delete());
//		
//		return patae;
//	}
//	private static DefaultAccessEntity createPatientNameAccess(){
//		DefaultAccessEntity patnameae = new DefaultAccessEntity.Feature();
//		patnameae.setCode("patientName");
//		patnameae.setName("Set Patient FN");
//		patnameae.setDomainUri("/patient/firstName");
//		patnameae.addAvailablePermission(new Permission.Read());
//		patnameae.addAvailablePermission(new Permission.Create());
//		patnameae.addAvailablePermission(new Permission.Update());
//		
//		return patnameae;
//	}
//	
//	private static DefaultAccessEntity createCaseStatusAccess(){
//		DefaultAccessEntity casestatusae = new DefaultAccessEntity.Feature();
//		casestatusae.setCode("cmcase-status");
//		casestatusae.setName("Create Status");
//		casestatusae.setDomainUri("/status");
//		casestatusae.addAvailablePermission(new Permission.Read());
//		casestatusae.addAvailablePermission(new Permission.Create());
//		
//		return casestatusae;
//	}
//}
