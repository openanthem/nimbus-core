package com.anthem.nimbus.platform.spec.model.user.flow;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Link;

import lombok.Getter;
import lombok.Setter;

@ViewConfig
@Getter @Setter @MapsTo.Model(ClientUser.class)
public class SearchUser {

	private Long id;

	private String lastName;

	private String middleName;

	private String firstName;

	private String loginName;

	private String email;

	@Link(url="/_editClientUser/_process", method="POST") private String edit;

	@Link(url="/_deleteClientUser/_process", method="POST") private String delete;

}
