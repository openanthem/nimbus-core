package com.anthem.nimbus.platform.spec.model.client.user;

import java.util.Arrays;
import java.util.HashSet;

import com.anthem.nimbus.platform.spec.model.access.Permission;
import com.anthem.nimbus.platform.spec.model.access.PlatformAccessEntity;
import com.anthem.nimbus.platform.spec.model.client.Client;
import com.anthem.nimbus.platform.spec.model.client.access.ClientAccessEntity;
import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;

public class TestClientUserFactory {
	
	public static ClientUser createCoreClientUser(){
		Client c = new Client();
		c.setCode("abc");
		c.setName("abc");
        
		ClientUser cu = new ClientUser();
		cu.setClient(c);
        cu.setLoginName("swetha");
       
        c.addClientUserRole(createClientUserRole1());
        c.addClientUserRole(createClientUserRole2());
       
        return cu;
	}
	
	private static ClientUserRole createClientUserRole1(){
		ClientUserRole abcCUR1 = new ClientUserRole();
        abcCUR1.setCode("abcpcr");
        abcCUR1.setName("PCR");
        abcCUR1.setEntries(new HashSet<>());
        return abcCUR1;
	}
	
	private static ClientUserRole createClientUserRole2(){
		ClientUserRole abcCUR2 = new ClientUserRole();
        abcCUR2.setCode("abcintake");
        abcCUR2.setName("abc-intake");
        abcCUR2.setEntries(new HashSet<>());
        return abcCUR2;
	}
	
	public static ClientUser createClientUser(){
		
		ClientUser cu = createCoreClientUser();
		PlatformAccessEntity patae = createPatientAccess();	
		PlatformAccessEntity patnameae = createPatientNameAccess();
		PlatformAccessEntity casestatusae = createCaseStatusAccess();
        
        ClientAccessEntity patcae = new ClientAccessEntity(patae);
        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
        
        ClientUserRole abcCUR1 = createClientUserRole1();
        ClientUserRole abcCUR2 = createClientUserRole2();
        
    //    abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, null));
//        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        
        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(),
				new Permission.Create()))));
//        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        cu.addGrantedRoles(abcCUR1);
        cu.addGrantedRoles(abcCUR2);
        return cu;
	}
	
	public static ClientUser createClientUser2(){
		
		ClientUser cu = createCoreClientUser();
		PlatformAccessEntity patae = createPatientAccess();	
		PlatformAccessEntity patnameae = createPatientNameAccess();
		PlatformAccessEntity casestatusae = createCaseStatusAccess();
        
        ClientAccessEntity patcae = new ClientAccessEntity(patae);
        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
        
        ClientUserRole abcCUR1 = createClientUserRole1();
        ClientUserRole abcCUR2 = createClientUserRole2();
        
        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Update()))));
        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        
  //      abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create()))));
        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        cu.addGrantedRoles(abcCUR1);
        cu.addGrantedRoles(abcCUR2);
        return cu;
	}
	
	public static ClientUser createClientUser3(){
		
		ClientUser cu = createCoreClientUser();
		PlatformAccessEntity patae = createPatientAccess();	
		PlatformAccessEntity patnameae = createPatientNameAccess();
		PlatformAccessEntity casestatusae = createCaseStatusAccess();
        
        ClientAccessEntity patcae = new ClientAccessEntity(patae);
        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
        
        ClientUserRole abcCUR1 = createClientUserRole1();
        ClientUserRole abcCUR2 = createClientUserRole2();
        
        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create(), new Permission.Update()))));
        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        
        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(),new Permission.Create(),new Permission.Update()))));
        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        cu.addGrantedRoles(abcCUR1);
        cu.addGrantedRoles(abcCUR2);
        return cu;
	}
	
	public static ClientUser createClientUser4(){
		
		ClientUser cu = createCoreClientUser();
		PlatformAccessEntity patae = createPatientAccess();	
		PlatformAccessEntity patnameae = createPatientNameAccess();
		PlatformAccessEntity casestatusae = createCaseStatusAccess();
        
        ClientAccessEntity patcae = new ClientAccessEntity(patae);
        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
        
        ClientUserRole abcCUR1 = createClientUserRole1();
        ClientUserRole abcCUR2 = createClientUserRole2();
        
     //   abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Read()))));
        abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        
   //     abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Update()))));
        abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        
        cu.addGrantedRoles(abcCUR1);
        cu.addGrantedRoles(abcCUR2);
        return cu;
	}
	
public static ClientUser createClientUser5(){
		
		ClientUser cu = createCoreClientUser();
		PlatformAccessEntity patae = createPatientAccess();	
		PlatformAccessEntity patnameae = createPatientNameAccess();
		PlatformAccessEntity casestatusae = createCaseStatusAccess();
        PlatformAccessEntity navStateae = createNavStateAccess();
        ClientAccessEntity patcae = new ClientAccessEntity(patae);
        ClientAccessEntity patnamecae = new ClientAccessEntity(patnameae);
        ClientAccessEntity casestatuscae = new ClientAccessEntity(casestatusae);
        ClientAccessEntity navStatecae = new ClientAccessEntity(navStateae);
        
        ClientUserRole abcCUR1 = createClientUserRole1();
        ClientUserRole abcCUR2 = createClientUserRole2();
        
        abcCUR1.getEntries().add(new ClientUserRole.Entry(patcae, new HashSet<Permission>(Arrays.asList(new Permission.Create()))));
      //  abcCUR1.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
   //     abcCUR1.getEntries().add(new ClientUserRole.Entry(navStatecae, navStatecae.getPermissions()));
        
        abcCUR2.getEntries().add(new ClientUserRole.Entry(patcae,new HashSet<Permission>(Arrays.asList(new Permission.Read(), new Permission.Create()))));
    //    abcCUR2.getEntries().add(new ClientUserRole.Entry(casestatuscae, casestatuscae.getPermissions()));
        cu.addGrantedRoles(abcCUR1);
        cu.addGrantedRoles(abcCUR2);
        return cu;
	}
	
	private static PlatformAccessEntity createPatientAccess(){
		PlatformAccessEntity patae = new PlatformAccessEntity.Feature();
		patae.setCode("patient");
		patae.setName("Create Patient");
		patae.setDomainUri("/patient");
		patae.addAvailablePermission(new Permission.Access());
		patae.addAvailablePermission(new Permission.Read());
		patae.addAvailablePermission(new Permission.Create());
		patae.addAvailablePermission(new Permission.Update());
		patae.addAvailablePermission(new Permission.Delete());
		
		return patae;
	}
	
	private static PlatformAccessEntity createNavStateAccess(){
		PlatformAccessEntity patae = new PlatformAccessEntity.Feature();
		patae.setCode("nav");
		patae.setName("nav");
		patae.setDomainUri("/navigationState");
		patae.addAvailablePermission(new Permission.Access());
		patae.addAvailablePermission(new Permission.Read());
		patae.addAvailablePermission(new Permission.Create());
		patae.addAvailablePermission(new Permission.Update());
		patae.addAvailablePermission(new Permission.Delete());
		
		return patae;
	}
	private static PlatformAccessEntity createPatientNameAccess(){
		PlatformAccessEntity patnameae = new PlatformAccessEntity.Feature();
		patnameae.setCode("patientName");
		patnameae.setName("Set Patient FN");
		patnameae.setDomainUri("/patient/firstName");
		patnameae.addAvailablePermission(new Permission.Read());
		patnameae.addAvailablePermission(new Permission.Create());
		patnameae.addAvailablePermission(new Permission.Update());
		
		return patnameae;
	}
	
	private static PlatformAccessEntity createCaseStatusAccess(){
		PlatformAccessEntity casestatusae = new PlatformAccessEntity.Feature();
		casestatusae.setCode("cmcase-status");
		casestatusae.setName("Create Status");
		casestatusae.setDomainUri("/status");
		casestatusae.addAvailablePermission(new Permission.Read());
		casestatusae.addAvailablePermission(new Permission.Create());
		
		return casestatusae;
	}
}
