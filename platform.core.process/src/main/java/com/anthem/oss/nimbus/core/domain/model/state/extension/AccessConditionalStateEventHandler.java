package com.anthem.oss.nimbus.core.domain.model.state.extension;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.anthem.oss.nimbus.core.BeanResolverStrategy;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional;
import com.anthem.oss.nimbus.core.domain.definition.extension.AccessConditional.Permission;
import com.anthem.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.anthem.oss.nimbus.core.domain.model.state.event.StateEventHandlers.OnStateLoadHandler;
import com.anthem.oss.nimbus.core.entity.client.access.ClientAccessEntity;
import com.anthem.oss.nimbus.core.entity.client.access.ClientUserRole;
import com.anthem.oss.nimbus.core.entity.client.user.ClientUser;
import com.anthem.oss.nimbus.core.entity.user.UserRole;
import com.anthem.oss.nimbus.core.session.UserEndpointSession;

/**
 * @author Rakesh Patel
 *
 */
public class AccessConditionalStateEventHandler extends AbstractConditionalStateEventHandler implements OnStateLoadHandler<AccessConditional> {
	
	public AccessConditionalStateEventHandler(BeanResolverStrategy beanResolver) {
		super(beanResolver);
	}
	
	@Override
	public void handle(AccessConditional configuredAnnotation, Param<?> param) {
		handleInternal(param, configuredAnnotation);
	}

	protected void handleInternal(Param<?> onChangeParam, AccessConditional configuredAnnotation) {
		
		if(Permission.WRITE == configuredAnnotation.p())
			return;
		
		ClientUser user = UserEndpointSession.getStaticLoggedInUser();
		
		Set<String> userRoleCodes = user.getRoles().stream().map(UserRole::getRoleCode).collect(Collectors.toSet());
		Set<String> userAuthorities = user.getResolvedAccessEntities().stream().map(ClientAccessEntity::getCode).collect(Collectors.toSet());
		
		if(configuredAnnotation.containsRoles() != null && configuredAnnotation.containsRoles().length > 0){
			boolean isTrue = userRoleCodes.stream().anyMatch(userRole -> Arrays.asList(configuredAnnotation.containsRoles()).contains(userRole));
			if(isTrue)	
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
		else if(configuredAnnotation.containsAuthority() != null && configuredAnnotation.containsAuthority().length > 0){
			boolean isTrue = userAuthorities.stream().anyMatch(userAccessEntity -> Arrays.asList(configuredAnnotation.containsAuthority()).contains(userAccessEntity));
			if(isTrue)	
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
		else if(StringUtils.isNotBlank(configuredAnnotation.whenRoles())){
			boolean isTrue = expressionEvaluator.getValue(configuredAnnotation.whenRoles(), userRoleCodes, Boolean.class);
			if(isTrue)
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
		else if(StringUtils.isNotBlank(configuredAnnotation.whenAuthorities())){
			boolean isTrue = expressionEvaluator.getValue(configuredAnnotation.whenAuthorities(), userAuthorities, Boolean.class);
			if(isTrue)
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
	}
	
	
	private void handlePermission(Permission p, Param<?> onChangeParam) {
		if(Permission.HIDDEN == p) {
			onChangeParam.setVisible(false);
			onChangeParam.setEnabled(false);
		}
		else if(Permission.READ == p) {
			onChangeParam.setVisible(true);
			onChangeParam.setEnabled(false);
		}
	}
		
}
