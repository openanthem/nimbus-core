/**
 * 
 */
package com.anthem.oss.nimbus.test.sample.um.model.view;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Button;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.ComboBox;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Hints.AlignOptions;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.InputDate;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.Section;
import com.anthem.nimbus.platform.spec.model.view.ViewConfig.TextBox;
import com.anthem.oss.nimbus.core.domain.MapsTo;
import com.anthem.oss.nimbus.core.domain.MapsTo.Path;
import com.anthem.oss.nimbus.test.sample.um.model.Patient;
import com.anthem.oss.nimbus.test.sample.um.model.UMCase;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Soham Chakravarti
 *
 */
@MapsTo.Type(UMCase.class)
@Getter @Setter
public class Page_FindPatient {
	

	
	@MapsTo.Type(UMCase.class)
	@Getter @Setter
	public static class Section_CaseInfo  {

		@Path @ComboBox private String requestType;

		@Path @ComboBox private String caseType;
		//@MapsTo.Path private DateRange serviceDate;
    }
	
	

	@MapsTo.Type(value=Patient.class)
	@Getter @Setter
	public static class Form_PatientInfo {

		@Path @NotNull @TextBox private String subscriberId;

		@Path @TextBox private String firstName;

		@Path @TextBox private String lastName;

		@Path @InputDate private LocalDate dob;

		@Button(url="{:id}/_findPatient/_process") @Hints(align=AlignOptions.Left)
		private String action_FindPatient;

		@Button(url="/_nav?a=next") @Hints(align=AlignOptions.Right)
		private String next;
	}
    

	
	@Getter @Setter
	public static class Section_PatientInfo {

		//@Form 
		//@Path(linked=false) private Form_PatientInfo patientInfoEntry;
	}

	
	
	@Section
	private Section_CaseInfo caseInfo;

	@Section
	private Section_PatientInfo patientInfo;

}
