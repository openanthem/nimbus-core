/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.role.flow;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Button;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Form;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Grid;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints.AlignOptions;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Section;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.TextBox;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@ViewConfig 
@Getter @Setter
public class Page_SearchUserRole {

	
	
	@ViewConfig 
	@Getter @Setter
	public static class Section_SearchCriteria  {

		@Form 
		private Form_ClientRoleSearch searchCriteriaEntry;
	}

	
	
	@ViewConfig @MapsTo.Model(ClientUserRole.class)
	@Getter @Setter
	public static class Form_ClientRoleSearch {

		@MapsTo.Path @TextBox @NotNull private String name;

		@Button(url="/_searchRole/_process", b="$executeAnd$nav", method="POST") @Hints(align=AlignOptions.Left)
		private String action_SearchRole;

		@Button(url="/_addUserRole/_process", b="$executeAnd$nav", method="POST") @Hints(align=AlignOptions.Right)
		private String addRole;
	}
	
	
	
	@ViewConfig //@MapsTo.Model(UserRoleSearch.class)
	@Getter @Setter
	public static class Section_searchResults {

		@Grid(onLoad=true, url="/_searchRole/_process")
		private List<SearchUserRole> roles;
		
		//@Grid
		//private Page<ClientRole> pageRoles;
	}
	
	
	
	@Section
	private Section_SearchCriteria searchUserRole;

	@Section 
	private Section_searchResults searchResults;

}
