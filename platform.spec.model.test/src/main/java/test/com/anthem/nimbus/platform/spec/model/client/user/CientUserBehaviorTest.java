/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.client.user;

import org.junit.Assert;
import org.junit.Test;

import com.anthem.nimbus.platform.spec.model.client.user.ClientUser;
import com.anthem.nimbus.platform.spec.model.client.user.ClientUserAccessBehavior;

/**
 * @author Soham Chakravarti
 *
 */
public class CientUserBehaviorTest {

	@Test
	public void testInstantiation() {
		ClientUser user = new ClientUser();
		ClientUserAccessBehavior b = user.newBehaviorInstance(ClientUserAccessBehavior.class);
		
		Assert.assertNotNull(b);
	//	Assert.assertNotNull(b.permissionToActions);
		Assert.assertSame(user, b.getModel());
	}
}
