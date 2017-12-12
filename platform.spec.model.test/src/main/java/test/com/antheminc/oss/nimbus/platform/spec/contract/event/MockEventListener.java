/**
 * 
 */
package test.com.antheminc.oss.nimbus.platform.spec.contract.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

/**
 * MockEventListener for testing the emit event scenarios
 * 
 * @author Swetha Vemuri
 *
 */
@Configuration
@ActiveProfiles({ "test", "build" })
public class MockEventListener implements StateAndConfigEventListener {

	private List<ModelEvent<Param<?>>> modelEvent;

	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return true;
	}

	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		if (modelEvent != null)
			modelEvent.add(event);
		else {
			modelEvent = new ArrayList<>();
			modelEvent.add(event);
		}

		return true;
	}

	public List<ModelEvent<Param<?>>> getModelEvent() {
		return this.modelEvent;
	}

	public void flushEvents() {
		if (getModelEvent() != null) {
			this.modelEvent = null;
		}
	}

}
