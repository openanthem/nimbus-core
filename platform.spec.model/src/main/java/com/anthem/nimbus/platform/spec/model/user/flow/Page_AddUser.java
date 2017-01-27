/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.flow;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.dsl.Model;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Button;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ComboBox;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Form;
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
@ViewConfig @MapsTo.Model(ClientUser.class)
@Getter @Setter
public class Page_AddUser {

	
	
	@ViewConfig @MapsTo.Model(ClientUser.class)
	@Getter @Setter
	public static class Section_UserDetails  {

		@Form 
		private Form_AddUser userEntry;

	}
	
	

	@ViewConfig @MapsTo.Model(ClientUser.class)
	@Getter @Setter
	public static class Form_AddUser {

		@TextBox @NotNull @MapsTo.Path("name/lastName") 
		private String lastName;

		@TextBox @MapsTo.Path("name/middleName") 
		private String middleName;

		@TextBox @NotNull @MapsTo.Path("name/firstName") 
		private String firstName;

		@TextBox @NotNull @MapsTo.Path("loginName") private String loginName;

		@TextBox @NotNull @MapsTo.Path("email") private String email;

		@Model.Param.Values(Values.ClientUserRole.class)

		@ComboBox @NotNull private String clientUserRole;

		@Button(url="/_submitClientUser/_process", b="$executeAnd$nav",method="POST") @Hints(align=AlignOptions.Right)
		private String action_addUser;
    }
	


	@Section
	private Section_UserDetails userSection;

}
