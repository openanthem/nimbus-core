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
package com.antheminc.oss.nimbus.domain.cmd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.MethodSorters;

import com.antheminc.oss.nimbus.AbstractFrameworkTest;
import com.antheminc.oss.nimbus.InvalidConfigException;
import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandTest extends AbstractFrameworkTest {
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void testRelativeUriComplete() {
		Command in = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = PLATFORM_ROOT + "/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(configUri, out);
	}
	
	@Test
	public void testRelativeUriDomainRootProvided1() {
		Command in = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(CMD_PREFIX + configUri, out);
	}
	
	@Test
	public void testRelativeUriDomainRootProvided2() {
		Command in = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/p/staticCodeValue:200/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(CMD_PREFIX + configUri, out);
	}
	
	@Test
	public void testRelativeUriDomainRootRelative1() {
		Command in = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(PLATFORM_ROOT + "/umcase_view"+configUri, out);
	}
	
	@Test
	public void testRelativeUriDomainRootRelative2() {
		Command in = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(PLATFORM_ROOT + "/umcase_view:100"+configUri, out);
	}
	
	@Test
	public void testToUri() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		assertEquals(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get?b=$execute", cmd.toUri());
	}
	
	@Test
	public void testToUriMultipleBehaviors() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		cmd.getBehaviors().add(Behavior.$nav);
		assertEquals(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get?b=$executeAnd$nav", cmd.toUri());
	}
	
	@Test
	public void testToUriWithEvent() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		cmd.setEvent("someEvent");
		assertEquals(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get/someEvent?b=$execute", cmd.toUri());
	}
	
	@Test
	public void testToUriWithQueryParams() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get?b=$state&abc=cba&abc=xyz&pqr=mnc").getCommand();
		String uriWithQueryParams = cmd.toUri();
		Command cmd2 = CommandBuilder.withUri(uriWithQueryParams).getCommand();
		assertThat(cmd.getRequestParams().size()).isEqualTo(cmd2.getRequestParams().size());
	}
	
	@Test
	public void testToUriWithDefaultBehaviorAndQueryParams() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get?abc=cba&abc=xyz&pqr=mnc").getCommand();
		String uriWithQueryParams = cmd.toUri();
		Command cmd2 = CommandBuilder.withUri(uriWithQueryParams).getCommand();
		assertThat(cmd.getRequestParams().size()).isLessThan(cmd2.getRequestParams().size());
	}
	
	@Test
	public void testBuildRemoteUri() {
		String uri = PLATFORM_ROOT + "/domainRoot_alias/nestedDomain/abc/_new?b=$executeAnd$save&criteria=customer.name.eq(\"John\")";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		String buildUri = cmd.toRemoteUri(Type.ParamName, Action._new, Behavior.$execute);
		
		assertThat(buildUri).isEqualTo(PLATFORM_ROOT + "/domainRoot_alias/nestedDomain/abc/_new?b=$execute");
	}
	
	@Test
	public void testConstructor() {
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/pageCreateCaseInfo/_get").getCommand();
		Command cloned = new Command(cmd);
		assertNotEquals(cmd, cloned);
		assertEquals(cmd.getAbsoluteUri(), cloned.getAbsoluteUri());
		assertEquals(cmd.getAction(), cloned.getAction());
		assertEquals(cmd.getEvent(), cloned.getEvent());
		assertEquals(cmd.getBehaviors(), cloned.getBehaviors());
		assertEquals(cmd.getClientUserId(), cloned.getClientUserId());
		assertNotNull(cloned.getRoot());
	}
	
	@Test
	public void testInvalidCommandMissingAction() {
		expectedEx.expect(InvalidConfigException.class);
		expectedEx.expectMessage("Command with URI: /hooli/1/thebox/p/umcase_view:100 cannot have null Action.");
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100").getCommand();
		cmd.validate();
	}
	
	@Test
	public void testInvalidCommandMissingBehavior() {
		expectedEx.expect(InvalidConfigException.class);
		expectedEx.expectMessage("Command with URI: /hooli/1/thebox/p/umcase_view:100/_get cannot have null Behavior.");
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/umcase_view:100/_get").getCommand();
		cmd.setBehaviors(null);
		cmd.validate();
	}
	
	@Test
	public void testInvalidCommandMissingTenantId() {
		expectedEx.expect(InvalidConfigException.class);
		expectedEx.expectMessage("Command with URI: /hooli/p/umcase_view:100/_get cannot have null TenantId.");
		Command cmd = CommandBuilder.withUri("/hooli/p/umcase_view:100/_get").getCommand();
		cmd.validate();
	}
	
	@Test
	public void testInvalidCommandMissingAppAlias() {
		expectedEx.expect(InvalidConfigException.class);
		expectedEx.expectMessage("Command with URI: /hooli/1/thebox/p/_get cannot have null AppAlias.");
		Command cmd = CommandBuilder.withUri("/hooli/1/p/umcase_view:100/_get").getCommand();
		cmd.validate();
	}
	
	@Test
	public void testInvalidCommandMissingDomainAlias() {
		expectedEx.expect(InvalidConfigException.class);
		expectedEx.expectMessage("Command with URI: /hooli/1/thebox/p/_get cannot have null DomainAlias.");
		Command cmd = CommandBuilder.withUri(PLATFORM_ROOT + "/_get").getCommand();
		cmd.validate();
	}
}
