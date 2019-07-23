/**
 *  Copyright 2016-2019 the original author or authors.
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
package com.antheminc.oss.nimbus.test.scenarios.coreviewsame.view;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ButtonGroup;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.QuestionnaireNote;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Tony Lopez
 *
 */
@Model
@Getter
@Setter
public class VMSystemNote {

	@Button(style = Button.Style.PLAIN, type = Button.Type.button)
	@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
	private String closeModal;

	@Section
	private VS vs;

	@Model
	@Getter
	@Setter
	public static class VS {

		private QuestionnaireNote systemNotesRequest;

		@Config(url = "/vpMain/vtMain/vmSystemNote/vs/notes/.m/_process?fn=_set&url=/p/questionnaire_note/_search?fn=example&rawPayload=<!json(/../systemNotesRequest)!>")
		@Grid(headerCheckboxToggleAllPages = true, pageSize = "7", onLoad = true, rowSelection = true, expandableRows = true, postButton = true, postButtonUri = "/petcareassessmentview/vpMain/vtMain/vmSystemNote/vs/add", postButtonTargetPath = "temp_ids", postButtonLabel = "Add Notes")
		@Label("Reserved System Notes")
		@MapsTo.Path(linked = false)
		private List<SystemNoteLineItem> notes;

		@Config(url = "/vpMain/vtMain/vmSystemNote/vs/tempIds/_replace")
		@Config(url = "/vpMain/vtMain/vsMain/petCareForm/petCareAssessmentQuestionnaire/questionnaire_Section4/questionnaireNotes/_process?fn=_systemNotesConversion")
		@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
		private String add;

		@ButtonGroup(cssClass = "oneColumn right")
		private CancelButtonGroup buttonGroup;

		@MapsTo.Path(linked = false)
		private TempIds tempIds;
	}

	@Model
	@Getter
	@Setter
	public static class CancelButtonGroup {

		@Button
		@Label("Cancel")
		@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
		private String cancel;
	}

	@MapsTo.Type(QuestionnaireNote.class)
	@Getter
	@Setter
	public static class TempIds {

		private String[] temp_ids;
	}

	@MapsTo.Type(QuestionnaireNote.class)
	@Getter
	@Setter
	@ToString
	public static class SystemNoteLineItem {

		@Label("Content")
		@GridColumn(placeholder = "--")
		@Path
		private String content;
	}
}
