///**
// * 
// */
//package test.com.anthem.nimbus.platform.spec.model.client;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.anthem.oss.nimbus.core.entity.access.DefaultAccessEntity;
//import com.anthem.oss.nimbus.core.entity.access.Permission;
//import com.anthem.oss.nimbus.core.entity.client.Client;
//import com.anthem.oss.nimbus.core.entity.client.ClientEntity;
//import com.anthem.oss.nimbus.core.entity.client.ClientEntity.Type;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
//import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
//
//import test.com.anthem.nimbus.platform.spec.model.access.AccessEntityFactory;
//
///**
// * @author Soham Chakravarti
// *
// */
//public class TestClientFactory {
//	
//	public static String getOrg1_code() {
//		return "comm";
//	}
//
//	public static String getOrg2_code() {
//		return "fep";
//	}
//	
//	private static Map<String, List<String>> _org2appMappings = new HashMap<>();
//	static {
//		_org2appMappings.put(getOrg1_code(), Arrays.asList(AccessEntityFactory.getApp1_code(), AccessEntityFactory.getApp2_code()));
//		_org2appMappings.put(getOrg2_code(), Arrays.asList(AccessEntityFactory.getApp1_code()));
//	}
//	
//	public static Client createClient() {
//		Client c = new Client();
//		c.setCode("antm");
//		c.setName("Anthem");
//		return c;
//	}
//	
//	public static Client createClient2() {
//		Client c = new Client();
//		c.setCode("agp");
//		c.setName("Amerigroup");
//		return c;
//	}
//	
//	public static ClientEntity createClientOrg1() {
//		ClientEntity org1 = new ClientEntity();
//		org1.setCode(getOrg1_code());
//		org1.setType(Type.ORG);
//		return org1;
//	}
//	
//	public static ClientEntity createClientOrg2() {
//		ClientEntity org2 = new ClientEntity();
//		org2.setCode(getOrg2_code());
//		org2.setType(Type.ORG);
//		return org2;
//	}
//	
//	public static ClientUserRole createClientRoleClinician() {
//		ClientUserRole cr = new ClientUserRole();
//		cr.setCode("clinician");
//		cr.setName("Anthem Clinician");
//		return cr;
//	}
//	
//	public static ClientUserRole createClientRoleIntake() {
//		ClientUserRole cr = new ClientUserRole();
//		cr.setCode("intake");
//		cr.setName("Anthem Intake");
//		return cr;		
//	}
//	
////	public static ClientUserIDS createClientUserIDS(){
////		ClientUserIDS cuIDS  = new ClientUserIDS();
////		cuIDS.setLoginName("AC63348");
////		cuIDS.setIdsGuid("111111111");
////		cuIDS.setSource("Google");
////		return cuIDS;
////	}
////	
////	public static ClientUserIDS createClientUserIDS_2(){
////		ClientUserIDS cuIDS  = new ClientUserIDS();
////		cuIDS.setLoginName("ben@facebook.com");
////		cuIDS.setIdsGuid("111111111");
////		cuIDS.setSource("Facebook");
////		return cuIDS;
////	}
////	
////	public static ClientUserIDS createClientUserIDS_3(){
////		ClientUserIDS cuIDS  = new ClientUserIDS();
////		cuIDS.setLoginName("ben@facebook.com");
////		cuIDS.setIdsGuid("111111111");
////		cuIDS.setSource("Google");
////		return cuIDS;
////	}
//	
//	public interface Callback {
//		public DefaultAccessEntity find(String code);
//	}
//	
//	public static void createSelectedAccesses(ClientEntity org, Callback cb) {
//		List<String> appCodes = _org2appMappings.get(org.getCode());
//		
//		for(int i=0; i<appCodes.size(); i++) {
//			
//			DefaultAccessEntity app = cb.find(appCodes.get(i));
//			ClientAccessEntity cae = new ClientAccessEntity(app);
////			org.addSelectedAccess(cae);
//			addAccessAndPermissionRecursively(app, cae, i);
//		}
//	}
//	
//	private static void addAccessAndPermissionRecursively(DefaultAccessEntity pae, ClientAccessEntity cae, int i) {
//		if(pae==null) return;
//		
//		if(pae.getAvailableAccesses()!=null) {
//			for(DefaultAccessEntity availableAe : pae.getAvailableAccesses()) {
//				
//				ClientAccessEntity caeNew = cae.addSelectedAccess(availableAe);	
//				addAccessAndPermissionRecursively(availableAe, caeNew, i);
//				
//				//if(i==1) break;
//			}
//		}
//		
//		if(pae.getAvailablePermissions()==null) return;
//		
//		for(Permission availablePermission : pae.getAvailablePermissions()) {
//			//==??cae.addPermission(availablePermission);
//			
//			//if(i==1) break;
//		}
//	}
//}
