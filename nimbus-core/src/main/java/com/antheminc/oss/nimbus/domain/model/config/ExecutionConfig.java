/**
 * 
 */
package com.antheminc.oss.nimbus.domain.model.config;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.antheminc.oss.nimbus.domain.defn.Execution.ConfigPlaceholder;

/**
 * @author Rakesh Patel
 *
 */
public interface ExecutionConfig {

	public List<Annotation> get();

	/**
	 * <p>Retrieve the context details for this execution config instance.
	 * @return the context
	 */
	public Context getContext();

	/**
	 * <p>A context object containing details related to an execution config
	 * instance. Data stored here should be used to support execution
	 * instructions.
	 * @author Tony Lopez
	 * @since 1.3
	 */
	public static class Context {
		private final Map<String, String> placeholders;

		public Context() {
			this.placeholders = new HashMap<>();
		}

		/**
		 * <p>Add a placeholder to the context.
		 * @param placeholder the placeholder to add
		 */
		public void addPlaceholder(ConfigPlaceholder placeholder) {
			this.placeholders.put(placeholder.name(), placeholder.value());
		}

		/**
		 * <p>Add placeholders to the context.
		 * @param placeholders the placeholder to add
		 */
		public void addPlaceholders(ConfigPlaceholder[] placeholders) {
			Stream.of(placeholders).forEach(this::addPlaceholder);
		}

		/**
		 * <p>Retrieve a placeholder value by it's name.
		 * @param placeholderName the name of the placeholder for which to
		 *            retrieve
		 */
		public String getPlaceholder(String placeholderName) {
			return this.placeholders.get(placeholderName);
		}

		/**
		 * <p>Get the map representation of the placeholders, with the key being
		 * the placeholder name and the value being the value to resolve the key
		 * to.
		 */
		public Map<String, String> getPlaceholderMap() {
			return Collections.unmodifiableMap(this.placeholders);
		}
	}
}
