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
