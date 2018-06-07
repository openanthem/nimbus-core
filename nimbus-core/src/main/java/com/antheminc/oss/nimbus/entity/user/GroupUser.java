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
package com.antheminc.oss.nimbus.entity.user;

import java.time.LocalDate;

import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Swetha Vemuri
 *
 */
@Model("groupuser")
@Getter @Setter
public class GroupUser extends IdLong {

	private static final long serialVersionUID = 1L;

	private String userId;
	
	private boolean admin;
	
	private LocalDate effectiveDate;
	
	private boolean retired;
	
	private LocalDate retireDate;
}
