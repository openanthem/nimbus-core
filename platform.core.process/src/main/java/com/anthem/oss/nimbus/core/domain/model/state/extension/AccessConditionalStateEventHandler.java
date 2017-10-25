package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.R2P;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.Permission;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Rakesh Patel
 *
 */
public class AccessConditionalStateEventHandler implements OnStateLoadHandler<AccessConditional> {
	
	@Override
	public void handle(AccessConditional configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation);
	}

	protected void handleInternal(Param<?> onChangeParam, AccessConditional configuredAnnotation) {
		ClientUser user = UserEndpointSession.getStaticLoggedInUser();
		
		if(CollectionUtils.isEmpty(user.getRoles())) 
			return; 
		
		if(user.getRoles().size() > configuredAnnotation.value().length)
			return; // Since user has more roles then specified in the @AccessConditional, hence default behavior
		
		Set<String> userRoleCodes = user.getRoles().stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
		
		List<R2P> configuredR2PList = Arrays.asList(configuredAnnotation.value());
		Set<String> configuredRoleCodes = configuredR2PList.stream().map((r2p) -> r2p.r()).collect(Collectors.toSet());
		
		boolean b = userRoleCodes.stream().allMatch(roleCode-> configuredRoleCodes.contains(roleCode));
			
		if(!b)
			return;
		
		Set<R2P> r2psToCheck = configuredR2PList.stream().filter((r2p) -> userRoleCodes.contains(r2p.r())).collect(Collectors.toSet());
		
		if(CollectionUtils.isEmpty(r2psToCheck)) // means user role(s) were not listed in the @AccessConditional config, default is allow access
			return;
		
		final Set<Permission> permissions = new HashSet<>();
		r2psToCheck.forEach((r2p) -> permissions.addAll(Arrays.asList(r2p.p())));
		
		List<Permission> sortedPermissions = permissions.stream()
			.sorted(Comparator.comparing(Permission::ordinal))
			.collect(Collectors.toList());
		
		processPermissionForParam(sortedPermissions.get(0), onChangeParam); // since this is sorted list based on least restrictive permissions, only the first permission needs to be checked
		
	}
	
	private void processPermissionForParam(Permission p, Param<?> onChangeParam) {
		if(Permission.READ == p) {
			onChangeParam.findParamByPath("/#/visible").setState(true);
			onChangeParam.findParamByPath("/#/enabled").setState(false);
		}
		else if(Permission.WRITE == p) {
			onChangeParam.findParamByPath("/#/visible").setState(true);
			onChangeParam.findParamByPath("/#/enabled").setState(true);
		}
		// TODO - refactor using factory to delegate to different permission handler as business logic increases and/or permissions set increases
	}
}
