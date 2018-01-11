package test.com.antheminc.oss.nimbus.core.testutils;

import java.util.Arrays;

import com.antheminc.oss.nimbus.core.domain.command.Behavior;
import com.antheminc.oss.nimbus.core.domain.command.Command;
import com.antheminc.oss.nimbus.core.domain.command.CommandBuilder;

/**
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public final class CommandUtils {

	private CommandUtils() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * 
	 * @param uri
	 * @param behaviors
	 * @return
	 */
	public static Command prepareCommand(String uri, Behavior... behaviors){
        final Command command = CommandBuilder.withUri(uri).getCommand();
        if(behaviors != null) {
        	Arrays.asList(behaviors).forEach((b) ->command.templateBehaviors().add(b));
        }
        return command;
    }
}
