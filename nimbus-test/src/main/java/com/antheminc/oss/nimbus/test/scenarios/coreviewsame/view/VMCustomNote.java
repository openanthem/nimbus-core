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

import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.MapsTo;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Nature;
import com.antheminc.oss.nimbus.domain.defn.MapsTo.Path;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ButtonGroup;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Form;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Section;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.TextBox;
import com.antheminc.oss.nimbus.domain.defn.extension.ActivateConditional;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Model
@Getter
@Setter
public class VMCustomNote {

	@Button(style = Button.Style.PLAIN, type = Button.Type.button)
	@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
	private String closeModal;

	@Section
	private VS section;

	@MapsTo.Type(CoreViewSameEntity.class)
	@Model
	@Getter
	@Setter
	public static class VS {

		@Form(cssClass = "questionGroup")
		@Path(value = "/petCareForm/petCareAssessmentQuestionnaire/questionnaire_Section4/questionnaireNotes", nature = Nature.TransientColElem)
		@ActivateConditional(when = "isAssigned()", targetPath = "/vbg/edit")
		@ActivateConditional(when = "!isAssigned()", targetPath = "/vbg/add")
		private VF form;
	}

	@MapsTo.Type(QuestionnaireNoteLineItem.class)
	@Model
	@Getter
	@Setter
	public static class VF {

		@Label("ID")
		@TextBox
		@Path
		private Long id;

		@Label("Content")
		@TextBox
		@Path
		private String content;

		@ButtonGroup(cssClass = "oneColumn")
		private VBG vbg;
	}

	@Model
	@Getter
	@Setter
	public static class VBG {

		@Label("Add")
		@Button(style = Button.Style.PRIMARY, type = Button.Type.submit)
		@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_update")
		@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_get?fn=param&expr=flush()")
		@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
		private String add;

		@Label("Edit")
		@Button(style = Button.Style.PRIMARY, type = Button.Type.submit)
		@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_update")
		@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_get?fn=param&expr=flush()")
		@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
		private String edit;

		@Label("Cancel")
		@Button(style = Button.Style.SECONDARY, type = Button.Type.reset)
		private String cancel;
	}
}