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
		
		if(configuredAnnotation.containsRoles() != null && configuredAnnotation.containsRoles().length > 0){
			boolean isTrue = userRoleCodes.stream().anyMatch(userRole -> Arrays.asList(configuredAnnotation.containsRoles()).contains(userRole));
			if(isTrue)	
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
		else if(StringUtils.isNotBlank(configuredAnnotation.when())){
			boolean isTrue = expressionEvaluator.getValue(configuredAnnotation.when(), userRoleCodes, Boolean.class);
			if(isTrue)
				handlePermission(configuredAnnotation.p(), onChangeParam);
		}
	}
	
	
	private void handlePermission(Permission p, Param<?> onChangeParam) {
		if(Permission.HIDDEN == p) {
			onChangeParam.setVisible(false);
			onChangeParam.setEnable(false);
		}
		else if(Permission.READ == p) {
			onChangeParam.setVisible(true);
			onChangeParam.setEnable(false);
		}
	}
		
}
