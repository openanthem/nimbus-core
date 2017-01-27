/**
 * 
 */
package com.anthem.nimbus.platform.spec.model.util;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Soham Chakravarti
 * 
 *
 */
public class CloneObserverRegistry<T> {

	private List<CloneObserver> registry;
	
	
	@Getter @JsonIgnore
	private final transient CollectionsTemplate<List<CloneObserver>, CloneObserver> registryTemplate = CollectionsTemplate
			.array(() -> registry, (r) -> this.registry = r);
	
	

	public interface ClonedObervable<T> {
		public void onCloned(Supplier<T> sourceGetter);
	}
	
	

	@RequiredArgsConstructor
	public class CloneObserver {

		protected final Consumer<T> targetSetter;

		
		/**
		 * 
		 * @param sourceGetter
		 */
		public void react(Supplier<T> sourceGetter) {
			T t = sourceGetter.get();
			targetSetter.accept(t);
		}
	}
	
}
