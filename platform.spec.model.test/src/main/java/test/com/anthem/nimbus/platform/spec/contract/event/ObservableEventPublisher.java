/**
 * 
 */
package test.com.anthem.nimbus.platform.spec.contract.event;

import java.util.Observable;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.anthem.nimbus.platform.spec.contract.event.StateAndConfigEventPublisher;
import com.anthem.nimbus.platform.spec.model.AbstractEvent.SuppressMode;
import com.anthem.nimbus.platform.spec.model.dsl.ModelEvent;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Model;
import com.anthem.nimbus.platform.spec.model.dsl.binder.StateAndConfig.Param;
import com.anthem.nimbus.platform.spec.model.exception.PlatformRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * @author Soham Chakravarti
 *
 */
public class ObservableEventPublisher extends Observable implements StateAndConfigEventPublisher {

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
	public boolean shouldAllow(StateAndConfig<?,?> p) {
		if(p instanceof Param<?>) {
			Param<?> param = (Param<?>) p;
			return !param.getConfig().isView();
		}
		else {
			Model<?,?> param = (Model<?,?>) p;
			return !param.getConfig().isView();
		}
	}
	
	@Override
	public boolean publish(ModelEvent<StateAndConfig<?,?>> modelEvent) {
		setChanged();
		int curr = counter.incrementAndGet();
		
		String json = convert(modelEvent);
		
		System.out.println("@@TEST-> Counter: "+curr+" modelEvent: "+json);
		notifyObservers(modelEvent);
		return true;
	}

	public String convert(Object model) {
		if(model==null) return null;
		
		try {
			String json = om.writeValueAsString(model);
			return json;
		} catch (Exception ex) {
			throw new PlatformRuntimeException("Failed to convert from model to JSON, modelClass: "+ model.getClass()
					+ "\n modelInstance: "+model, ex);
		}
	}
}
