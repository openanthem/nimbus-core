/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.comamnd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.anthem.nimbus.platform.spec.model.command.Command;
import com.anthem.nimbus.platform.spec.model.command.CommandBuilder;
import com.anthem.nimbus.platform.spec.model.dsl.Action;
import com.anthem.nimbus.platform.spec.model.dsl.Behavior;
import com.anthem.nimbus.platform.spec.model.dsl.Constants;

/**
 * @author Soham Chakravarti
 *
 */
public class CommandBuilderTest {
	
	@Test
	public void t0_noParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/_new";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		assertSame(Action._new, cmd.getAction());
		assertNull(cmd.getBehaviors());
	}
	
	@Test
	public void t1_withParams() {
		String uri = "/client_xyz/app_abc/p/domainRoot_alias/_new?b=$executeAnd$save";
		Command cmd = CommandBuilder.withUri(uri).getCommand();
		assertNotNull(cmd);
		
		assertEquals("client_xyz", cmd.getRootClientAlias());
		assertEquals("app_abc", cmd.getAppAlias());
		assertEquals("domainRoot_alias", cmd.getRootDomainAlias());
		
		assertSame(Action._new, cmd.getAction());
		assertSame(Behavior.$execute, cmd.getBehaviors().get(0));
		assertSame(Behavior.$save, cmd.getBehaviors().get(1));
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
		
		System.out.println(cmd.toUniqueId());
	}
}
