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
package com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core;

import java.util.List;

import com.antheminc.oss.nimbus.domain.defn.Domain;
import com.antheminc.oss.nimbus.domain.defn.Domain.ListenerType;
import com.antheminc.oss.nimbus.domain.defn.Execution.Config;
import com.antheminc.oss.nimbus.domain.defn.Model;
import com.antheminc.oss.nimbus.domain.defn.Repo;
import com.antheminc.oss.nimbus.domain.defn.Repo.Cache;
import com.antheminc.oss.nimbus.domain.defn.Repo.Database;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Accordion;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.AccordionTab;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Button;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.ButtonGroup;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.CardDetail;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.FieldValue;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Grid;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridColumn;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.GridRowBody;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.Link;
import com.antheminc.oss.nimbus.domain.defn.ViewConfig.LinkMenu;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.entity.AbstractEntity.IdLong;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tony Lopez
 *
 */
@Domain(value = "coreviewsame", includeListeners = { ListenerType.persistence })
@Repo(alias = "coreviewsame", value = Database.rep_mongodb, cache = Cache.rep_device)
@Getter
@Setter
public class CoreViewSameEntity extends IdLong {

	private static final long serialVersionUID = 1L;

	private PetCareForm petCareForm;

	@Model
	@Getter
	@Setter
	public static class PetCareForm {

		@Accordion(showExpandAll = true, showMessages = true)
		private PetCareAssessmentQuestionnaire petCareAssessmentQuestionnaire;
	}

	@Model
	@Getter
	@Setter
	public static class PetCareAssessmentQuestionnaire {

		@AccordionTab
		@Label("Section 4")
		private Questionnaire_Section4 questionnaire_Section4;
	}
	
	@Model
	@Getter
	@Setter
	public static class Questionnaire_Section4 {

		@ButtonGroup(cssClass = "text-sm-right")
		private VBG vbg;

		@Label("Notes")
		@Grid(expandableRows = true)
		private List<QuestionnaireNoteLineItem> questionnaireNotes;

		@Model
		@Getter
		@Setter
		public static class VBG {

			@Label("Add Custom")
			@Button
			@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_get?fn=param&expr=unassignMapsTo()")
			@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
			private String addCustomNote;

			@Label("Add System Note")
			@Button
			@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
			private String addSystemNote;
		}

		@Model
		@Getter
		@Setter
		@ToString
		public static class QuestionnaireNoteLineItem {

			@Label("ID")
			@GridColumn(placeholder = "--")
			private Long id;

			@Label("Content")
			@GridColumn(placeholder = "--")
			private String content;

			@LinkMenu
			private VLM vlm;

			@GridRowBody
			private RowBody rowBody;

			@GridColumn(hidden = true)
			private List<NoteOwnerLineItem> owners;

			@Model
			@Getter
			@Setter
			public static class VLM {

				@Label("Edit")
				@Link
				@Config(url = "/vpMain/vtMain/vmCustomNote/section/form/_get?fn=param&expr=assignMapsTo('/.d/<!#this!>/../../.m')")
				@Config(url = "/vpMain/vtMain/vmCustomNote/_process?fn=_setByRule&rule=togglemodal")
				private String edit;

				@Label("Remove")
				@Link
				@Config(url = "<!#this!>/../../.m/_delete")
				private String remove;
			}

			@Model
			@Getter
			@Setter
			public static class RowBody {

				@CardDetail
				private VS section;

				@Model
				@Getter
				@Setter
				public static class VS {

					@CardDetail.Body
					private CardBody cardBody;

					@Model
					@Getter
					@Setter
					public static class CardBody {

						@Link
						@Label("View Owners")
						@Config(url = "/vpMain/vtMain/viewNoteOwnersModal/vsBody/authNotes/_replace?rawPayload=<!json(/../../../../owners)!>")
						@Config(url = "/vpMain/vtMain/viewNoteOwnersModal/_process?fn=_setByRule&rule=togglemodal")
						private String viewNotes;
					}
				}
			}
		}

		@Model
		@Getter
		@Setter
		@ToString
		public static class NoteOwnerLineItem {

			@CardDetail
			private Card card;

			@Model
			@Getter
			@Setter
			@ToString
			public static class Card {

				@CardDetail.Body
				private Body body;
			}

			@Model
			@Getter
			@Setter
			@ToString
			public static class Body {

				@FieldValue(placeholder = "--")
				@Label("Owner name")
				private String name;
			}
		}
	}
}