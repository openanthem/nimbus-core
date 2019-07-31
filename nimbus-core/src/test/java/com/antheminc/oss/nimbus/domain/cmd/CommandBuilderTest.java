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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.antheminc.oss.nimbus.domain.cmd.CommandElement.Type;
import com.antheminc.oss.nimbus.domain.defn.Constants;

/**
 * IMP: Don't add spring context dependency in this class. If you need to test something related to command but with spring context
 * better to find a correct test class if one exists or create new and provide necessary comments for how/when to use that class
 * 
 * @author Soham Chakravarti
 * @author Rakesh Patel
 */
public class CommandBuilderTest {

	@Test
	public void t0_multiple() {
		String uri = "/Anthem/fep/p/dashboard/landingPage/tileCreateCase/sectionCreateCase/umCaseLists.m/_process?fn=_set&url=/Anthem/fep/p/umcase/_search?fn=example";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertSame(Action._process, cmd.getAction());
		assertNotNull(cmd.getBehaviors());
	}

	@Test
	public void t0_multiple_withClient() {
		String uri = "/Anthem/fep/p/dashboard/landingPage/tileCreateCase/sectionCreateCase/umCaseLists.m/_process?fn=_set&url=/Anthem/fep/p/umcase/_search?fn=example";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertSame(Action._process, cmd.getAction());
		assertNotNull(cmd.getBehaviors());
	}
	
	@Test
	public void t0_multiple_split() {
		String uri = "/Anthem/fep/p/dashboard/landingPage/tileCreateCase/sectionCreateCase/umCaseLists.m/_process?fn=_set&url=/Anthem/fep/p/umcase/_search?fn=example";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertSame(Action._process, cmd.getAction());
		
		String url = cmd.getRequestParams().get("url")[0];
		assertEquals("/Anthem/fep/p/umcase/_search?fn=example", url);
		
		Command subCmd = CommandBuilder.withUri(url).getCommand();
		assertNotNull(subCmd);
		assertSame(Action._search, subCmd.getAction());
	}
	
	
	@Test
	public void t0_noParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/_new";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		assertSame(Action._new, cmd.getAction());
	}
	
	@Test
	public void t1_withParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/_new?b=$executeAnd$save&criteria=customer.name.eq(\"John\")";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		assertSame(Action._new, cmd.getAction());
		assertSame(Behavior.$execute, cmd.getBehaviors().get(0));
		assertSame(Behavior.$save, cmd.getBehaviors().get(1));
		
		assertNotNull(cmd.getRequestParams().get("criteria"));
		assertEquals(cmd.getFirstParameterValue("criteria"), "customer.name.eq(\"John\")");
	}

	@Test
	public void t2_nestedDomainNoParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/param_alias/_new";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		System.out.println("cmd.getAbsoluteAlias(): "+cmd.getAbsoluteAlias());
		System.out.println("cmd.getAbsoluteAliasTillRootDomain(): "+cmd.getAbsoluteAliasTillRootDomain());
		System.out.println("cmd.getAbsoluteDomainAlias(): "+cmd.getAbsoluteDomainAlias());
		System.out.println("cmd.getAbsoluteDomainUri(): "+cmd.getAbsoluteDomainUri());
		System.out.println("cmd.getAbsoluteUri(): "+cmd.getAbsoluteUri());
		System.out.println("cmd.getAbsoluteUri(Type): "+cmd.getAbsoluteUri(Type.DomainAlias));
		System.out.println("cmd.getAlias(Type): "+cmd.getAlias(Type.DomainAlias));
		System.out.println("cmd.getAliasUri(Type): "+cmd.getAliasUri(Type.DomainAlias));
		System.out.println("cmd.getRootDomainAlias(): "+cmd.getRootDomainAlias());
		System.out.println("cmd.getRootDomainUri(): "+cmd.getRootDomainUri());
		
		assertSame(Action._new, cmd.getAction());
		assertNotNull(cmd.getBehaviors());
	}
	
	@Test
	public void t2_simple_with_lazyParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/_new";
		Map<String, String[]> rParams = new HashMap<>();
		rParams.put(Constants.MARKER_URI_BEHAVIOR.code, new String[]{"$executeAnd$save"});
		
		Command cmd = CommandBuilder.withUri(uri).addParams(rParams).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		assertSame(Action._new, cmd.getAction());
		assertSame(Behavior.$execute, cmd.getBehaviors().get(0));
		assertSame(Behavior.$save, cmd.getBehaviors().get(1));
		
	}
	
	
	@Test
	public void t3_linked_commandElements_are_generated() {
		String uri = "/xyz/admin/p/flow_patientenrollment/_loadTask:582363676028/_process";
		//?b=
		
		Map<String, String[]> rParams = new HashMap<>();
		rParams.put(Constants.MARKER_URI_BEHAVIOR.code, new String[]{"$executeAnd$config"});
		
		Command cmd = CommandBuilder.withUri(uri).addParams(rParams).getCommand();
		assertNotNull(cmd);
		
		assertNotNull(cmd.getElement(Type.ProcessAlias));
		
		assertNotNull(cmd.getRefId(Type.ProcessAlias));
		
		System.out.println(cmd.getRefId(Type.ProcessAlias));
	}

}
