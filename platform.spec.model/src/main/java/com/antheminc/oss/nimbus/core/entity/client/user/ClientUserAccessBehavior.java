///**
// * 
// */
//package com.antheminc.oss.nimbus.core.entity.client.user;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.util.CollectionUtils;
//
//import com.antheminc.oss.nimbus.core.domain.command.Command;
//import com.antheminc.oss.nimbus.core.domain.definition.Constants;
//import com.antheminc.oss.nimbus.core.entity.AbstractEntityBehavior;
//import com.antheminc.oss.nimbus.core.entity.access.Permission;
//import com.antheminc.oss.nimbus.core.entity.access.Role;
//import com.antheminc.oss.nimbus.core.entity.client.access.ClientAccessEntity;
//import com.antheminc.oss.nimbus.core.entity.client.access.ClientUserRole;
//import com.antheminc.oss.nimbus.core.entity.client.access.ClientUserRole.Entry;
//import com.antheminc.oss.nimbus.core.util.JustLogit;
//
//import lombok.Getter;
//import lombok.Setter;
//
///**
// * @author Soham Chakravarti
// *
// */
//
//@Getter @Setter
//public class ClientUserAccessBehavior extends AbstractEntityBehavior<ClientUser, String> {
//	
//	//private static final String DOMAIN_URI_EXP = "/([0-9]*)([a-zA-Z]*)([0-9]+)([a-zA-Z]*)/";
//	//private static final String DOMAIN_URI_SEPERATOR = "/";
//	
//	private JustLogit logit = new JustLogit(this.getClass());
//	
//	private static Map<String, List<String>> permissionToActions = new HashMap<>();
//	
//	static{
//		permissionToActions.put("ACCESS", Arrays.asList("_get","_info","_search","_new","_replace","_update","_delete","_nav","_process"));
//		permissionToActions.put("READ", Arrays.asList("_get","_info","_search","_getAll","_nav","_process"));
//		permissionToActions.put("CREATE", Arrays.asList("_new","_nav","_process"));
//		permissionToActions.put("UPDATE", Arrays.asList("_replace","_update","_nav","_process"));
//		permissionToActions.put("DELETE", Arrays.asList("_delete","_nav","_process"));	
//	}
//
//	public ClientUserAccessBehavior(ClientUser user) {
//		super(user);
//	}
//	
//	public boolean canUserPerform(Command cmd){
//		logit.debug(()->"begin canUserPerform for : ["+cmd+"] ");
//		String path = cmd.getAbsoluteDomainAlias() + Constants.SEPARATOR_URI.code + cmd.getAction().name();
//		return canUserPerformAction(path,cmd.getAction().name());
//	}
//
//	/**
//	 * Determine if user can perform an action based on the restricted permissions for a role.
//	 * @param path
//	 * @param action
//	 * @return
//	 */
//	public boolean canUserPerformAction(String path , String action){
//		logit.debug(()->"begin canUserPerformAction for : ["+path+"] : action : ["+action+"]");
//		return hasRestrictedPermissions(path,action);		
//	}
//	/**
//	 * 
//	 * @param command
//	 * @return
//	 */
//	private boolean hasRestrictedPermissions(String path,String action) {
//        Set<ClientUserRole> grantedRoles = getModel().getGrantedRoles();
//        boolean canRolePerform = true;
//		if (!CollectionUtils.isEmpty(grantedRoles)) {
//			
//			Iterator<ClientUserRole> roleIt = grantedRoles.iterator();
//			while (roleIt.hasNext()) {
//				Role<Entry, ClientAccessEntity> r = roleIt.next();
//				Set<ClientUserRole.Entry> entries = r.getEntries();
//				logit.debug(()->"begin getGrantedPermissionsFromRoleEntry for role: ["+r.getName()+"]");
//				Set<Permission> grantedPermissions = getGrantedPermissionsFromRoleEntries(entries, path);
//				logit.debug(()->"aggregated permissions for all entries for role : ["+r.getName()+"] :: "+grantedPermissions);				
//				
//				if (CollectionUtils.isEmpty(grantedPermissions)){
//					canRolePerform = true;
//				}
//				logit.debug(()->"Permission to actions : "+permissionToActions);
//				boolean doesExist = false;
//				if(!CollectionUtils.isEmpty(permissionToActions) && !CollectionUtils.isEmpty(grantedPermissions)){
//					
//					for(Permission permission : grantedPermissions){ // list of all permissions in all roles for the domainuri 
//						List<String> restrictedActions = permissionToActions.get(permission.getCode());
//						if(!CollectionUtils.isEmpty(restrictedActions)){
//							restrictedActions.forEach((restrictedAction)->restrictedAction.trim());
//							logit.trace(()->"check if permission ["+permission+"] has action ["+action+"]" );
//							if(restrictedActions.contains(action)){
//								logit.debug(()->"permission ["+permission+"] contains action ["+action+"]");
//								doesExist = true;
//							}
//						}
//						
//					}
//				}
//				
//				if(doesExist){
//					logit.debug(()->"["+r.getName()+"] has restrictive permission for performing ["+path+"] and action ["+action+"]");
//						canRolePerform = false;
//					}else{
//						logit.debug(()->"["+r.getName()+"] does not have a restrictive permission for performing ["+path+"] and action ["+action+"]");
//					return canRolePerform;
//				}
//			}
//			return canRolePerform;		
//		}
//		return false; // return false if user is not assigned any role. Look for Role without restricted permission.
//	}
//
//	/**
//	 * 
//	 * @param entries
//	 * @param domainUri
//	 * @return
//	 */
//	private Set<Permission> getGrantedPermissionsFromRoleEntries(Set<ClientUserRole.Entry> entries, String domainUri) {
//		if(!CollectionUtils.isEmpty(entries)) {
//			Set<Permission> entryPermissions = new HashSet<>();
//			for(Entry e : entries) {
//				ClientAccessEntity cae = e.getReferredAccess();
//				if(StringUtils.equalsIgnoreCase(domainUri, StringUtils.prependIfMissing(cae.getDomainUri(), "/")) ){
//					logit.debug(()->"permissions for domainUri : ["+domainUri+"] : "+e.getGrantedPermissions()+" for entry "+e.getId());
//					entryPermissions.addAll(e.getGrantedPermissions());
//				}
//			}
//			return entryPermissions;
//		}
//		return null;
//	}
//	
//	
//}
//
