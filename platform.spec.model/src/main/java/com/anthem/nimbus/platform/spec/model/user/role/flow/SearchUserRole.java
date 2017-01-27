package com.anthem.nimbus.platform.spec.model.user.role.flow;

import java.time.LocalDate;

import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Link;

import lombok.Getter;
import lombok.Setter;

@ViewConfig @MapsTo.Model(ClientUserRole.class)
@Getter @Setter
public class SearchUserRole {

	private Long id;

	private String name;

	private LocalDate effectiveDate;

	private LocalDate terminationDate;

	private String description;

	@Link(url="/_editUserRole/_process", method="POST") private String edit;

	@Link(url="/_deleteUserRole/_process", method="POST") private String delete;
	
}
