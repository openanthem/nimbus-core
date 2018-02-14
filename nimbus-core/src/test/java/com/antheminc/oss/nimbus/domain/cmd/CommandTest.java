/**
 *  Copyright 2016-2018 the original author or authors.
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

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Soham Chakravarti
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandTest {

	@Test
	public void t0_relativeUri_complete() {
		Command in = CommandBuilder.withUri("/anthem/fep/icr/p/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/anthem/fep/icr/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		assertEquals(configUri, out);
	}
	
	@Test
	public void t0_relativeUri_domainRoot_provided_1() {
		Command in = CommandBuilder.withUri("/anthem/fep/icr/p/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/p/staticCodeValue/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		System.out.println(out);
		assertEquals("/anthem/fep/icr"+configUri, out);
	}
	
	@Test
	public void t0_relativeUri_domainRoot_provided_2() {
		Command in = CommandBuilder.withUri("/anthem/fep/icr/p/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/p/staticCodeValue:200/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		System.out.println(out);
		assertEquals("/anthem/fep/icr"+configUri, out);
	}
	
	@Test
	public void t0_relativeUri_domainRoot_relative_1() {
		Command in = CommandBuilder.withUri("/anthem/fep/icr/p/umcase_view/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		System.out.println(out);
		assertEquals("/anthem/fep/icr/p/umcase_view"+configUri, out);
	}
	
	@Test
	public void t0_relativeUri_domainRoot_relative_2() {
		Command in = CommandBuilder.withUri("/anthem/fep/icr/p/umcase_view:100/pageCreateCaseInfo/tileCreateCaseInfo/sectionUMCaseInfo/formUMCaseInfo/_get").getCommand();
		
		String configUri = "/_search?fn=lookup&where=staticCodeValue.paramCode.eq('/requestType')";
		String out = in.getRelativeUri(configUri);
		System.out.println(out);
		assertEquals("/anthem/fep/icr/p/umcase_view:100"+configUri, out);
	}
	
}
