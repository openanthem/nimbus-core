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
package com.antheminc.oss.nimbus.test.scenarios.coreviewsame.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.antheminc.oss.nimbus.context.BeanResolverStrategy;
import com.antheminc.oss.nimbus.domain.cmd.exec.AbstractFunctionHandler;
import com.antheminc.oss.nimbus.domain.cmd.exec.ExecutionContext;
import com.antheminc.oss.nimbus.domain.defn.extension.Content.Label;
import com.antheminc.oss.nimbus.domain.model.config.extension.LabelStateEventHandler;
import com.antheminc.oss.nimbus.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.NoteOwnerLineItem;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.NoteOwnerLineItem.Card;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem.RowBody;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem.RowBody.VS;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem.RowBody.VS.CardBody;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.QuestionnaireNote;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.QuestionnaireNote.NoteOwner;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Tony Lopez
 *
 */
public class QuestionnaireNoteConversionFunctionHandler<T, R> extends AbstractFunctionHandler<T, R> {

	public static final Logger LOG = LoggerFactory.getLogger(QuestionnaireNoteConversionFunctionHandler.class);

	public static final String DASH = "-";

	private final ObjectMapper objectMapper;
	private final LabelStateEventHandler labelStateEventHandler;

	public QuestionnaireNoteConversionFunctionHandler(BeanResolverStrategy beanResolver) {
			this.objectMapper = beanResolver.get(ObjectMapper.class);
			this.labelStateEventHandler = beanResolver.get(LabelStateEventHandler.class);
		}

	private void initializeDefaultLabel(Object obj) {

		Param<?> param = (Param<?>) obj;
		param.setLabels(new HashSet<>());
		Set<Label> labels = null;
		if (param.getConfig() != null && param.getConfig().getLabels() != null) {
			labels = param.getConfig().getLabels();
		}
		if (labels != null) {
			for (Label label : labels) {
				this.labelStateEventHandler.addLabelToState(label, param);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public R execute(ExecutionContext eCtx, Param<T> actionParameter) {

		// Determine whether or not any selections from the authorizations grid
		// were made.
		int[] temp_ids = null;
		try {
			JsonNode root = this.objectMapper.readTree(eCtx.getCommandMessage().getRawPayload());
			temp_ids = this.objectMapper.treeToValue(root.path("temp_ids"), int[].class);
		} catch (IOException e) {
			LOG.error("Failed to parse temp_ids. eCtx: {}", eCtx, e);
		}

		if (null != temp_ids && temp_ids.length > 0) {

			// Gather the services to add
			List<QuestionnaireNote> available = (List<QuestionnaireNote>) actionParameter.getRootDomain()
					.findStateByPath("/vpMain/vtMain/vmSystemNote/vs/notes/.m");
			List<QuestionnaireNote> selected = new ArrayList<>();
			for (int id : temp_ids) {
				selected.add(available.get(id));
			}

			// Convert the authorizations into services
			List<QuestionnaireNoteLineItem> toAdd = new ArrayList<>();
			for (QuestionnaireNote selectedNote : selected) {
				QuestionnaireNoteLineItem addee = this.convertToQuestionnaireNoteLineItem(selectedNote);
				toAdd.add(addee);
			}

			// Get already added services
			List<QuestionnaireNoteLineItem> existingNotes = (List<QuestionnaireNoteLineItem>) actionParameter
					.getLeafState();
			if (CollectionUtils.isEmpty(existingNotes)) {
				existingNotes = new ArrayList<>();
			}

			// Add the selected services
			for (QuestionnaireNoteLineItem addee : toAdd) {
				QuestionnaireNoteLineItem foundEqualItem = null;
				if (!existingNotes.isEmpty()) {
					foundEqualItem = existingNotes.stream()
							.filter(s -> s.getId() != null && s.getId().equals(addee.getId()))
							.filter(s -> s.getContent() != null && s.getContent().equals(addee.getContent()))
							.findFirst()
							.orElse(null);
				}

				if (foundEqualItem == null && addee.getId() != null) {
					actionParameter.findIfCollection().add(addee);
					// TODO The following is a fix for
					// https://jira.anthem.com/browse/CMDM-25133.
					// This should be handled by the framework, but adding it
					// here as a temporary
					// solution due to release. This should be removed once it
					// is resolved in the
					// framework.
					int lastIdx = actionParameter.findIfCollection().size() - 1;
					actionParameter.findIfCollection().findParamByPath("/" + lastIdx + "/vlm")
							.traverseChildren(this::initializeDefaultLabel);
					actionParameter.findIfCollection().findParamByPath("/" + lastIdx + "/rowBody/section/cardBody")
							.traverseChildren(this::initializeDefaultLabel);
				}
			}
		}

		return null;
	}

	/**
	 * Performs a custom conversion with some business logic applied.
	 * 
	 * @param source The core entity to convert.
	 * @return The view object.
	 */
	public static QuestionnaireNoteLineItem convertToQuestionnaireNoteLineItem(QuestionnaireNote source) {
		final QuestionnaireNoteLineItem target = new QuestionnaireNoteLineItem();

		BeanUtils.copyProperties(source, target, "owners");

		List<NoteOwnerLineItem> noteOwners = new ArrayList<>();
		if (null != source.getOwners()) {
			for(NoteOwner noteOwner: source.getOwners()) {
				NoteOwnerLineItem.Body convertedNoteOwner = new NoteOwnerLineItem.Body();
				BeanUtils.copyProperties(noteOwner, convertedNoteOwner);
				
				NoteOwnerLineItem noteOwnerLineItem = new NoteOwnerLineItem();
				noteOwnerLineItem.setCard(new Card());
				noteOwnerLineItem.getCard().setBody(convertedNoteOwner);
				noteOwners.add(noteOwnerLineItem);
			}
		}
		target.setOwners(noteOwners);
		
		if (null == target.getRowBody()) {
			target.setRowBody(new RowBody());
			target.getRowBody().setSection(new VS());
			target.getRowBody().getSection().setCardBody(new CardBody());
		}

		CardBody cardBody = target.getRowBody().getSection().getCardBody();
		BeanUtils.copyProperties(source, cardBody);

		return target;
	}
}
