/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.model.command;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.anthem.oss.nimbus.core.domain.command.Command;
import com.anthem.oss.nimbus.core.domain.command.CommandBuilder;

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
