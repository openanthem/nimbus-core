package test.com.antheminc.oss.nimbus.platform.utils;

import java.util.function.Function;

/**
 * Utility class containing common path methods used within the framework.
 * 
 * @author Tony Lopez (AF42192)
 *
 */
public class PathUtils {

	/**
	 * <p>Iterates through the values of <tt>arr</tt> and uses each element to invoke <tt>finderFn</tt>. The Function <tt>finderFn</tt> is 
	 * a search function that expects a key <b>K</b> provided by <tt>arr</tt>.</p>
	 * 
	 * <p>The first non-null value retrieved as a result of <tt>finderFn</tt> will be returned. If <tt>arr</tt> is null or no value is found,
	 * <tt>null</tt> will be returned.</p>
	 * 
	 * @param arr the array of key objects
	 * @param finderFn the search function to evaluate
	 * @return the first found element as a result of <tt>finderFn</tt>, otherwise null 
	 */
	public static <T, K> T findFirstByPath(K[] arr, Function<K, T> finderFn) {
		if (null == arr) {
			return null;
		}
		for (final K key : arr) {
			final T found = finderFn.apply(key);
			if (null != found) {
				return found;
			}
		}
		return null;
	}
}
