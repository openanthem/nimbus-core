/**
 * 
 */
package test.com.anthem.nimbus.platform.utils;

import com.anthem.nimbus.platform.spec.model.dsl.binder.Holder;
import com.anthem.oss.nimbus.core.domain.command.execution.CommandExecution.MultiOutput;

/**
 * @author Swetha Vemuri
 *
 */
public class ExtractResponseOutputUtils {

	public static String extractDomainRootRefId(Object controllerResp) {
		return MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getRootDomainId();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp) {
		return (T)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(0).getValue();
	}
	
	public static <T> T extractOutput(Object controllerResp, int j) {
		return extractOutput(controllerResp, 0, j);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T extractOutput(Object controllerResp, int i, int j) {
		return (T)((MultiOutput)MultiOutput.class.cast(Holder.class.cast(controllerResp).getState()).getOutputs().get(i)).getOutputs().get(j).getValue();
	}
}
