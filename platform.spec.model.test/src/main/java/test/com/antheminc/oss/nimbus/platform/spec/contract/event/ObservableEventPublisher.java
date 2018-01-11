/**
 * 
 */
package test.com.antheminc.oss.nimbus.platform.spec.contract.event;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.antheminc.oss.nimbus.core.FrameworkRuntimeException;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState;
import com.antheminc.oss.nimbus.core.domain.model.state.EntityState.Param;
import com.antheminc.oss.nimbus.core.domain.model.state.ModelEvent;
import com.antheminc.oss.nimbus.core.domain.model.state.internal.AbstractEvent.SuppressMode;
import com.antheminc.oss.nimbus.core.spec.contract.event.StateAndConfigEventListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author Soham Chakravarti
 *
 */
public class ObservableEventPublisher extends Observable implements StateAndConfigEventListener {

	public AtomicInteger counter = new AtomicInteger();
	
	private ObjectMapper om;
	
	public ObservableEventPublisher() {
		this.om = Jackson2ObjectMapperBuilder.json()
					.annotationIntrospector(new JacksonAnnotationIntrospector())
					//.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
					//.autoDetectFields(true)
					//.autoDetectGettersSetters(true)
					//.modules(new JavaTimeModule())
					.build();
	}
	
	@Override
	public boolean shouldSuppress(SuppressMode mode) {
		return SuppressMode.ECHO == mode;
	}	
	
	@Override
	public boolean shouldAllow(EntityState<?> p) {
		return !p.isMapped();
	}
	
	@Override
	public boolean listen(ModelEvent<Param<?>> event) {
		setChanged();
		int curr = counter.incrementAndGet();
		
		String json = convert(event);
		
		System.out.println("@@TEST-> Counter: "+curr+" modelEvent: "+json);
		notifyObservers(event);
		return true;
	}

	public String convert(Object model) {
		if(model==null) return null;
		
		try {
			String json = om.writeValueAsString(model);
			return json;
		} catch (Exception ex) {
			throw new FrameworkRuntimeException("Failed to convert from model to JSON, modelClass: "+ model.getClass()
					+ "\n modelInstance: "+model, ex);
		}
	}
}
