/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.flow;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
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
public class Page_SearchUser {


	
	@ViewConfig 
	@Getter @Setter
	public static class Section_SearchCriteria  {

		@Form 
		private Form_UserSearch searchCriteriaEntry;
    }
	
	

	@ViewConfig @MapsTo.Model(ClientUser.class)
	@Getter @Setter
	public static class Form_UserSearch {

		@MapsTo.Path @TextBox @NotNull private String loginName;

		@Button(url="/_searchClientUser/_process", b="$executeAnd$nav", method="POST") @Hints(align=AlignOptions.Left)
		private String action_SearchUser;

		@Button(url="/_addClientUser/_process", b="$executeAnd$nav", method="POST") @Hints(align=AlignOptions.Right)
		private String addUser;
	}

	

	@ViewConfig //@MapsTo.Model(UserSearch.class)
	@Getter @Setter
	public static class Section_searchResults {

		@Grid(onLoad=true, url="/_searchClientUser/_process")
		private List<SearchUser> users;
		
		//@Grid
		//private Page<ClientUser> pageUsers;
	}
	
	

	@Section
	private Section_SearchCriteria searchUser;

	@Section 
	private Section_searchResults searchResults;

}
