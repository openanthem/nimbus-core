/**
 *  Copyright 2016-2019 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.antheminc.oss.nimbus.support;

/**
 * <p>A utility class that provides support for primitive Java types.
 * 
 * @author Tony Lopez
 *
 */
public class PrimitiveUtils {

	private PrimitiveUtils() {
	}

	private static boolean DEFAULT_BOOLEAN;
	private static byte DEFAULT_BYTE;
	private static short DEFAULT_SHORT;
	private static int DEFAULT_INT;
	private static long DEFAULT_LONG;
	private static float DEFAULT_FLOAT;
	private static double DEFAULT_DOUBLE;

	/**
	 * <p>Get the default value as defined by the JVM for {@code clazz}, if it
	 * is supported. If the provided type for {@code clazz} is not supported, an
	 * {@link IllegalArgumentException} will be thrown.
	 * @param clazz the type for which to retrieve the default value
	 * @return the default value for {@code clazz}.
	 */
	public static Object getDefaultValue(Class<?> clazz) {
		if (clazz.equals(boolean.class)) {
			return DEFAULT_BOOLEAN;
		} else if (clazz.equals(byte.class)) {
			return DEFAULT_BYTE;
		} else if (clazz.equals(short.class)) {
			return DEFAULT_SHORT;
		} else if (clazz.equals(int.class)) {
			return DEFAULT_INT;
		} else if (clazz.equals(long.class)) {
			return DEFAULT_LONG;
		} else if (clazz.equals(float.class)) {
			return DEFAULT_FLOAT;
		} else if (clazz.equals(double.class)) {
			return DEFAULT_DOUBLE;
		} else {
			throw new IllegalArgumentException("Class type " + clazz + " not supported");
		}
	}
}
