/**
 * 
 */
package com.antheminc.oss.nimbus.test.sample.um.model.view;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.antheminc.oss.nimbus.core.domain.definition.MapsTo;
import com.antheminc.oss.nimbus.core.domain.definition.MapsTo.Path;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Button;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.ComboBox;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Hints.AlignOptions;
import com.antheminc.oss.nimbus.test.sample.um.model.core.Patient;
import com.antheminc.oss.nimbus.test.sample.um.model.core.UMCase;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.InputDate;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.Section;
import com.antheminc.oss.nimbus.core.domain.definition.ViewConfig.TextBox;

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

		@Button(url="{:id}/_findPatient/_process") @Hints(value=AlignOptions.Left)
		private String action_FindPatient;

		@Button(url="/_nav?a=next") @Hints(value=AlignOptions.Right)
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
