/**
 * 
 */
package com.anthem.oss.nimbus.core.domain.model.state.repo.db;

import java.util.List;

import org.springframework.core.convert.converter.Converter;

import com.anthem.oss.nimbus.core.entity.user.ClientUserGroup;
import com.anthem.oss.nimbus.core.entity.user.GroupUser;

/**
 * @author Rakesh Patel
 *
 */
public class ClientUserGrooupSearchResponseConverter implements Converter<List<ClientUserGroup>, List<GroupUser>> {

	@Override
	public List<GroupUser> convert(List<ClientUserGroup> source) {
		
		ClientUserGroup cug = source.get(0);
		
		return cug.getMembers();
	}

}
