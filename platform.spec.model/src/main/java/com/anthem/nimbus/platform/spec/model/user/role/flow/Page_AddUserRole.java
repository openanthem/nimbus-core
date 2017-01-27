/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.user.role.flow;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.client.access.ClientUserRole;
import com.anthem.nimbus.platform.spec.model.dsl.MapsTo;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Button;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Form;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.InputDate;
//import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Mode;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints.AlignOptions;
//import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Mode.Options;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Section;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.TextBox;
//import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ViewParamBehavior;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Dinakar Meda
 *
 */
@ViewConfig @MapsTo.Model(ClientUserRole.class)
@Getter @Setter
public class Page_AddUserRole {

	

	@ViewConfig @MapsTo.Model(ClientUserRole.class)
	@Getter @Setter
	public static class Section_ClientRoleDetails  {

		@Form 
		private Form_AddClientRole userRoleEntry;
	}
	
	
	// 	findParamByPath("/pg2/userRoleSection/userRoleEntry").getBackingModel().setState(clientRole)

	
	@ViewConfig @MapsTo.Model(ClientUserRole.class)
	@Getter @Setter
	public static class Form_AddClientRole {

		@TextBox(hidden=true) private long id;

		@TextBox @NotNull private String name;

		@InputDate @NotNull private LocalDate effectiveDate;

		@InputDate private LocalDate terminationDate;

		@TextBox private String description;

		@Button(url="/_submitUserRole/_process", b="$executeAnd$nav", method="POST") @Hints(align=AlignOptions.Right)
		private String action_addOrUpdateRole;

		@Button(url="/_nav?a=back", b="$nav") @Hints(align=AlignOptions.Left)
		private String searchRoles;
	}
	
	

	@Section
	private Section_ClientRoleDetails userRoleSection;

}
