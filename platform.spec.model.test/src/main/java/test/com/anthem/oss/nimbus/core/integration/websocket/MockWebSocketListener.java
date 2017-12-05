/**
 * 
 */
package test.com.antheminc.oss.nimbus.core.integration.websocket;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 *
 */
@Getter @RequiredArgsConstructor
public class MockWebSocketListener {
	
	private final StateAndConfigEventListener mockParamEventListener;
	private List<ModelEvent<Param<?>>> events = new LinkedList<>();
	
	public void createPassThrough() {
		create(event->{
			
		});
	}
	
	@SuppressWarnings("unchecked")
	public void create(Consumer<ModelEvent<Param<?>>> cb) {
		when(mockParamEventListener.shouldAllow(any())).thenReturn(true);
		when(mockParamEventListener.shouldSuppress(any())).thenReturn(false);
		
		when(mockParamEventListener.listen(any(ModelEvent.class)))
		.thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				Object[] args = invocation.getArguments();
				ModelEvent<Param<?>> event = (ModelEvent<Param<?>>)args[0];
				
				synchronized (events) {
					events.add(event);	
				}
				
				System.out.println("@@@@ Model Event Path: "+ event.getPath());
				cb.accept(event);
				
				return true;
			}
		});
	}
	
	public void flushEvents() {
		this.events.clear();
	}
}
