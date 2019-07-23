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
package com.antheminc.oss.nimbus.test.scenarios.coreviewsame;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.antheminc.oss.nimbus.test.domain.support.AbstractFrameworkIntegrationTests;
import com.antheminc.oss.nimbus.test.domain.support.utils.JsonUtils;
import com.antheminc.oss.nimbus.test.domain.support.utils.MockHttpRequestBuilder;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.CoreViewSameEntity.Questionnaire_Section4.QuestionnaireNoteLineItem;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.QuestionnaireNote;
import com.antheminc.oss.nimbus.test.scenarios.coreviewsame.core.QuestionnaireNote.NoteOwner;

/**
 * @author Tony Lopez
 *
 */
public class NIMBUS241Test extends AbstractFrameworkIntegrationTests {

	private MockRestServiceServer mockServer;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Before
	public void setup() {
		this.mockServer = MockRestServiceServer.createServer(restTemplate);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void testEmptyRowAdditions() {
		this.mockQuestionnaireNoteControllerCall();
		
		Object createNewView = controller.handlePost(requestOf("/coreviewsame_view/_new"), null);
		Object openAddSystemNoteModal = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vsMain/petCareForm/petCareAssessmentQuestionnaire/questionnaire_Section4/vbg/addSystemNote/_get?b=$execute"), null);
		Object gridOnLoadSystemNotes = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vmSystemNote/vs/notes/_get?b=$execute&pageSize=7&page=0"), null);
		Object submitSystemNoteSelection = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vmSystemNote/vs/add/_get?b=$execute"), "{\"temp_ids\":[\"0\"]}");
		Assert.assertEquals(1, getCoreList(1L).size());
		Object clickRemoveSystemNote = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vsMain/petCareForm/petCareAssessmentQuestionnaire/questionnaire_Section4/questionnaireNotes/0/vlm/remove/_get?b=$execute"), null);
		Assert.assertEquals(0, getCoreList(1L).size());
		Object openAddCustomNoteModal = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vsMain/petCareForm/petCareAssessmentQuestionnaire/questionnaire_Section4/vbg/addCustomNote/_get?b=$execute"), null);
		Assert.assertEquals(0, getCoreList(1L).size());
		Object submitCustomNote = controller.handlePost(requestOf("/coreviewsame_view:1/vpMain/vtMain/vmCustomNote/section/form/vbg/add/_get?b=$execute"), "{\"id\":1,\"content\":\"custom note 1\"}");
		Assert.assertEquals(1, getCoreList(1L).size());
	}
	
	private List<QuestionnaireNoteLineItem> getCoreList(Long id) {
		CoreViewSameEntity core = mongo.findById(id, CoreViewSameEntity.class, "coreviewsame");
		return core.getPetCareForm().getPetCareAssessmentQuestionnaire().getQuestionnaire_Section4().getQuestionnaireNotes();
	}
	
	private HttpServletRequest requestOf(String uri) {
		return MockHttpRequestBuilder.withUri(PLATFORM_ROOT + uri).getMock();
	}
	
	private void mockQuestionnaireNoteControllerCall() {
		List<QuestionnaireNote> response = new ArrayList<>();
		response.add(new QuestionnaireNote());
		response.get(0).setId(1L);
		response.get(0).setContent("Note 1 (2 owners)");
		response.get(0).setOwners(new ArrayList<>());
		response.get(0).getOwners().add(new NoteOwner());
		response.get(0).getOwners().get(0).setName("Owner 1");
		response.get(0).getOwners().add(new NoteOwner());
		response.get(0).getOwners().get(1).setName("Owner 2");
		response.add(new QuestionnaireNote());
		response.get(1).setId(2L);
		response.get(1).setContent("Note 2 (1 owner)");
		response.get(1).setOwners(new ArrayList<>());
		response.get(1).getOwners().add(new NoteOwner());
		response.get(1).getOwners().get(0).setName("Owner 1");
		response.add(new QuestionnaireNote());
		response.get(2).setId(3L);
		response.get(2).setContent("Note 3 (0 owners)");
		response.get(2).setOwners(new ArrayList<>());
		String questionnaireNotesJson = JsonUtils.get().convert(response);

		this.mockServer.expect(requestTo(new StringContains(PLATFORM_ROOT + "/questionnaire_note")))
				.andExpect(method(HttpMethod.POST))
				.andRespond(withSuccess(questionnaireNotesJson, MediaType.APPLICATION_JSON));
	}
}
